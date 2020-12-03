package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    @Transactional
    public List<Action> updateFromLynx() {
        return updateActions(lynxConnector.findActions());
    }

    @Transactional
    public List<Action> updateFromLynxActivities(Activities activities) {
        List<Action> actionsFromLynx = lynxConnector.getActionsFromActivities(activities.getActivities());
        return updateActions(actionsFromLynx);
    }

    private List<Action> updateActions(List<Action> actionsFromLynx) {
        actionRepository.deleteAllByPaymentIsNull();
        Map<String, Action> paidActionsByLynxId = actionRepository.findAllByLynxActivityIdInAndPaymentIsNotNull(actionsFromLynx.stream()
                                                                                                                               .map(Action::getLynxActivityId)
                                                                                                                               .collect(Collectors.toList()))
                                                                  .stream()
                                                                  .collect(Collectors.toMap(this::getActionUniqueKey, a -> a));
        List<Action> actionsToSave = actionsFromLynx.stream()
                                                    .filter(actionFromLynx -> !paidActionsByLynxId.containsKey(getActionUniqueKey(actionFromLynx)))
                                                    .collect(Collectors.toList());
        return actionRepository.saveAll(actionsToSave);
    }

    private String getActionUniqueKey(Action a) {
        return a.getLynxActivityId().toString() + "_" + a.getAchiever().getId().toString();
    }
}
