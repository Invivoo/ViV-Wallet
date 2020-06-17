package com.invivoo.vivwallet.api.infrastructure.lynx;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.ActivityToActionMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration.*;

@Service
public class LynxConnector {

    private final RestTemplate restTemplate;
    private final ActivityToActionMapper activityToActionMapper;

    public LynxConnector(@Qualifier(LYNX_CONNECTOR_REST_TEMPLATE) RestTemplate restTemplate, ActivityToActionMapper activityToActionMapper) {
        this.restTemplate = restTemplate;
        this.activityToActionMapper = activityToActionMapper;
    }

    public List<Action> findActions(){
        List<Activity> activities = findActivities();
        List<Action> actions = activities.stream()
                                         .map(activityToActionMapper::convert)
                                         // .filter(...) todo filter on actStatus Held (why do Not Held or planned statuses have actDate and valDate
                                         .filter(Optional::isPresent)
                                         .map(Optional::get)
                                         .collect(Collectors.toList());
        actions.forEach(action -> setVivFromRelatedActivities(action, activities));
        return actions;
    }

    public List<Activity> findActivities() {
        UriComponents lynxActionsUri = UriComponentsBuilder.fromHttpUrl("http://172.18.0.11:9000/api/report/getinfo")
                                                  .queryParam("name", "viv")
                                                  .queryParam("begin_date", "2020-01-01")
                                                  .queryParam("end_date", "2020-01-31")
                                                  .queryParam("stageid", 39)
                                                  .build();
        ResponseEntity<Activities> response = restTemplate.getForEntity(lynxActionsUri.toString(), Activities.class);
        if(HttpStatus.OK != response.getStatusCode()){
            return Collections.emptyList();
        }
        return Optional.ofNullable(response.getBody())
                       .map(Activities::getActivities)
                       .orElse(Collections.emptyList());
    }

    private void setVivFromRelatedActivities(Action action, List<Activity> activities) {
        BigDecimal actionVivValue = BigDecimal.valueOf(action.getType().getValue());
        if(!action.getType().isSharedByAchievers()){
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
