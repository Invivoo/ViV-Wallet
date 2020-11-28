package com.invivoo.vivwallet.api.infrastructure.lynx;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.ActivityToActionMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration.LYNX_CONNECTOR_REST_TEMPLATE;

@Service
public class LynxConnector {

    public static final String ACTIVITY_HELD_STATUS = "Held";
    private final RestTemplate restTemplate;
    private String vivApiUrl;
    private final ActivityToActionMapper activityToActionMapper;

    public LynxConnector(@Qualifier(LYNX_CONNECTOR_REST_TEMPLATE) RestTemplate restTemplate, @Value("lynx.vivApiUrl") String vivApiUrl, ActivityToActionMapper activityToActionMapper) {
        this.restTemplate = restTemplate;
        this.vivApiUrl = vivApiUrl;
        this.activityToActionMapper = activityToActionMapper;
    }

    public List<Action> findActions() {
        List<Activity> activities = findActivities();
        return getActionsFromActivities(activities);
    }

    public List<Action> getActionsFromActivities(List<Activity> activities) {
        List<Activity> activitiesWithType = activities.stream()
                                                      .filter(activity -> Objects.nonNull(activity.getType())) // todo find why activity do not have type
                                                      .collect(Collectors.toList());
        List<Action> actions = activitiesWithType.stream()
                                                 .filter(activity -> ACTIVITY_HELD_STATUS.equals(activity.getStatus())) //todo validate this filter
                                                 .map(activityToActionMapper::convert)
                                                 .filter(Optional::isPresent)
                                                 .map(Optional::get)
                                                 .collect(Collectors.toList());
        actions.forEach(action -> setVivFromRelatedActivities(action, activitiesWithType));
        return actions;
    }

    protected List<Activity> findActivities() {
        UriComponents lynxActionsUri = UriComponentsBuilder.fromHttpUrl(vivApiUrl)
                                                           .queryParam("name", "viv")
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
        BigDecimal actionVivValue = BigDecimal.valueOf(action.getType().getValue());
        if (!action.getType().isSharedByAchievers()) {
            action.setViv(actionVivValue);
            return;
        }
        long count = activities.stream()
                               .filter(activity -> activity.getId().equals(action.getLynxActivityId()))
                               .count();
        BigDecimal sharedActionVivValue = actionVivValue.divide(BigDecimal.valueOf(count), 0, RoundingMode.HALF_UP);
        action.setViv(sharedActionVivValue);
    }
}
