package com.invivoo.vivwallet.api.infrastructure.lynx;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.ActivityToActionMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.LynxUserToUserMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.LynxUser;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.LynxUsersResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration.LYNX_CONNECTOR_REST_TEMPLATE;

@Service
public class LynxConnector {

    public static final String ACTIVITY_HELD_STATUS = "Held";
    public static final String ACTIVITY_IS_OK = "OK";
    private final RestTemplate restTemplate;
    private final ActivityToActionMapper activityToActionMapper;
    private final LynxUserToUserMapper lynxUserToUserMapper;
    private final String vivApiUrl;
    private final String userApiUrl;

    public LynxConnector(@Qualifier(LYNX_CONNECTOR_REST_TEMPLATE) RestTemplate restTemplate,
                         @Value("${lynx.vivApiUrl}") String vivApiUrl,
                         @Value("${lynx.userApiUrl}") String userApiUrl,
                         ActivityToActionMapper activityToActionMapper,
                         LynxUserToUserMapper lynxUserToUserMapper) {
        this.restTemplate = restTemplate;
        this.vivApiUrl = vivApiUrl;
        this.userApiUrl = userApiUrl;
        this.activityToActionMapper = activityToActionMapper;
        this.lynxUserToUserMapper = lynxUserToUserMapper;
    }

    public List<Action> findActions() {
        List<Activity> activities = findActivities();
        return getActionsFromActivities(activities);

    }

    public List<Action> getActionsFromActivities(List<Activity> activities) {
        List<Activity> validatedActivities = activities.stream()
                                                       .filter(this::isValid)
                                                       .collect(Collectors.toList());
        List<Action> actions = validatedActivities.stream()
                                                  .map(activityToActionMapper::convert)
                                                  .filter(Optional::isPresent)
                                                  .map(Optional::get)
                                                  .filter(a -> a.getValueDate()
                                                                .isAfter(Optional.ofNullable(a.getAchiever().getVivInitialBalanceDate())
                                                                                 .orElse(LocalDateTime.MIN)))
                                                  .collect(Collectors.toList());
        actions.forEach(action -> setVivFromRelatedActivities(action, validatedActivities));
        return actions;
    }

    private boolean isValid(Activity activity) {
        return isStatusHeldAndValidityOk(activity)
               && isValueDateAfterNow(activity)
               && isTypeAndOwnerPresent(activity);
    }

    private boolean isStatusHeldAndValidityOk(Activity activity) {
        return ACTIVITY_HELD_STATUS.equals(activity.getStatus())
               && ACTIVITY_IS_OK.equals(activity.getValidity());
    }

    private boolean isValueDateAfterNow(Activity activity) {
        return Optional.ofNullable(activity.getValueDate())
                       .filter(LocalDateTime.now()::isAfter)
                       .isPresent();
    }

    private boolean isTypeAndOwnerPresent(Activity activity) {
        return Objects.nonNull(activity.getType()) && StringUtils.isNotBlank(
                activity.getOwner());
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
        if (!action.getType().isSharedByAchievers()) {
            action.setVivAmount((action.getType().getValue()));
            return;
        }
        int count = (int) activities.stream()
                                    .filter(activity -> activity.getId().equals(action.getLynxActivityId()))
                                    .map(Activity::getOwner)
                                    .distinct()
                                    .count();
        action.setVivAmount(action.getType().getValue() / count);
    }

    public List<User> findUsers() {
        return getLynxUsers().stream()
                             .map(lynxUserToUserMapper::convert)
                             .collect(Collectors.toList());
    }

    private List<LynxUser> getLynxUsers() {
        UriComponents lynxUserUri = UriComponentsBuilder.fromHttpUrl(userApiUrl)
                                                        .build();
        ResponseEntity<LynxUsersResponse> response = restTemplate.getForEntity(lynxUserUri.toString(), LynxUsersResponse.class);
        if (HttpStatus.OK != response.getStatusCode()) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(response.getBody())
                       .map(LynxUsersResponse::getUsers)
                       .orElse(Collections.emptyList());
    }
}
