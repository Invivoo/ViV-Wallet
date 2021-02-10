package com.invivoo.vivwallet.api.infrastructure.lynx;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.ActivityToActionMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration.LYNX_CONNECTOR_REST_TEMPLATE;

@Service
public class LynxConnector {

    public static final List<Integer> WORKING_HOURS = Arrays.asList(9, 10, 11, 14, 15, 16, 17);

    public static final String ACTIVITY_HELD_STATUS = "Held";
    public static final String ACTIVITY_IS_OK = "OK";
    public static final List<ActionType> ACTION_TYPES_WITH_PAYMENT_DEPENDING_ON_ROLE_AND_HOUR = Arrays.asList(ActionType.TECHNICAL_ASSESSMENT, ActionType.COACHING);
    private final RestTemplate restTemplate;
    private String vivApiUrl;
    private final ActivityToActionMapper activityToActionMapper;

    public LynxConnector(@Qualifier(LYNX_CONNECTOR_REST_TEMPLATE) RestTemplate restTemplate, @Value("${lynx.vivApiUrl}") String vivApiUrl, ActivityToActionMapper activityToActionMapper) {
        this.restTemplate = restTemplate;
        this.vivApiUrl = vivApiUrl;
        this.activityToActionMapper = activityToActionMapper;
    }

    public List<Action> findActions() {
        List<Activity> activities = findActivities();
        return getActionsFromActivities(activities);
    }

    public List<Action> getActionsFromActivities(List<Activity> activities) {
        LocalDateTime validityDate = LocalDateTime.now();
        List<Activity> validatedActivities = activities.stream()
                                                       .filter(activity -> Objects.nonNull(activity.getType()) && StringUtils.isNotBlank(activity.getOwner()))
                                                       .filter(activity -> Optional.ofNullable(activity.getValueDate()).filter(validityDate::isAfter).isPresent())
                                                       .filter(activity -> ACTIVITY_HELD_STATUS.equals(activity.getStatus()) && ACTIVITY_IS_OK.equals(activity.getValidity()))
                                                       .collect(Collectors.toList());
        List<Action> actions = validatedActivities.stream()
                                                  .map(activityToActionMapper::convert)
                                                  .filter(Optional::isPresent)
                                                  .map(Optional::get)
                                                  .filter(a -> a.getValueDate().isAfter(Optional.ofNullable(a.getAchiever().getVivInitialBalanceDate())
                                                                                           .orElse(LocalDateTime.MIN)))
                                                  .collect(Collectors.toList());
        actions.forEach(action -> setVivFromRelatedActivities(action, validatedActivities));
        return actions;
    }

    protected List<Activity> findActivities() {
        UriComponents lynxActionsUri = UriComponentsBuilder.fromHttpUrl(vivApiUrl)
                                                           .build();
        ResponseEntity<Activities> response = restTemplate.getForEntity(lynxActionsUri.toString(), Activities.class);
        if (HttpStatus.OK != response.getStatusCode()) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(response.getBody())
                       .map(Activities::getActivities)
                       .orElse(Collections.emptyList());
    }

    private void setVivFromRelatedActivities(Action action, List<Activity> activities) {
        if (isNotAnActionDoneDuringWorkingHoursToPay(action)) {
            action.setVivAmount(0);
            action.setContext(String.format("Durant les heures de travail - %s", action.getContext()));
            return;
        }
        if (!action.getType().isSharedByAchievers()) {
            action.setVivAmount((action.getType().getValue()));
            return;
        }
        int count = (int) activities.stream()
                                    .filter(activity -> activity.getId().equals(action.getLynxActivityId()))
                                    .count();
        action.setVivAmount(action.getType().getValue() / count);
    }

    private boolean isNotAnActionDoneDuringWorkingHoursToPay(Action action) {
        return WORKING_HOURS.contains(action.getDate().getHour())
               && ACTION_TYPES_WITH_PAYMENT_DEPENDING_ON_ROLE_AND_HOUR.contains(action.getType())
               && !action.getAchiever()
                         .getRoles()
                         .stream()
                         .map(Role::getType)
                         .allMatch(RoleType.CONSULTANT::equals);
    }
}
