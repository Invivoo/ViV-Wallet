package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final LynxConnector lynxConnector;

    public ActionService(ActionRepository actionRepository, LynxConnector lynxConnector) {
        this.actionRepository = actionRepository;
        this.lynxConnector = lynxConnector;
    }

    public List<Action> findAllById(Collection<Long> actionIds) {
        return actionRepository.findAllById(actionIds);
    }

    public List<Action> findAllByOrderByDateDesc() {
        return actionRepository.findAllByOrderByDateDesc();
    }

    public List<Action> findAllByAchiever(User achiever) {
        return actionRepository.findAllByAchieverOrderByDateDesc(achiever);
    }

    public void saveAll(List<Action> actions) {
        actionRepository.saveAll(actions);
    }

    public List<Action> updateFromLynx() {
        return updateActions(lynxConnector.findActions());
    }

    public List<Action> updateFromLynxActivities(Activities activities) {
        List<Action> actionsFromLynx = lynxConnector.getActionsFromActivities(activities.getActivities());
        return updateActions(actionsFromLynx);
    }

    private List<Action> updateActions(List<Action> actionsFromLynx) {
        Map<String, Action> actionByUniqueKey = actionRepository.findAll()
                                                                .stream()
                                                                .collect(Collectors.toMap(this::getActionUniqueKey, a -> a));
        List<Action> actionsToSave = actionsFromLynx.stream()
                                                    .map(action -> new AbstractMap.SimpleEntry(action, actionByUniqueKey.get(getActionUniqueKey(action))))
                                                    .map(this::toActionToSave)
                                                    .collect(Collectors.toList());
        return actionRepository.saveAll(actionsToSave);
    }

    private Action toActionToSave(AbstractMap.SimpleEntry<Action, Action> existingActionForActionFromLynx) {
        Action actionFromLynx = existingActionForActionFromLynx.getKey();
        Action actionFromVivWallet = existingActionForActionFromLynx.getValue();
        if (actionFromVivWallet == null) {
            return actionFromLynx;
        } else if (actionFromVivWallet.getPayment() != null) {
            return actionFromVivWallet;
        }
        return actionFromLynx.toBuilder()
                             .id(actionFromVivWallet.getId())
                             .build();
    }

    private String getActionUniqueKey(Action a) {
        return a.getLynxActivityId().toString() + "_" + a.getAchiever().getId().toString();
    }
}
