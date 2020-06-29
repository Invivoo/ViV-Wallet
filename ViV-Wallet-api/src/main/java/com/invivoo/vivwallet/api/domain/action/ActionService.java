package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActionService {

    private final ActionRepository actionRepository;
    private final LynxConnector lynxConnector;

    public ActionService(ActionRepository actionRepository, LynxConnector lynxConnector) {
        this.actionRepository = actionRepository;
        this.lynxConnector = lynxConnector;
    }

    public List<Action> getActionsOrderedByDateDesc() {
       return actionRepository.findAllByOrderByDateDesc();
    }

    public List<Action> findAll(){
        return actionRepository.findAll();
    }

    public List<Action> findAllByAchiever(User achiever) {
        return actionRepository.findAllByAchieverOrderByDateDesc(achiever);
    }

    public void saveAll(List<Action> actions) {
        actionRepository.saveAll(actions);
    }

    public List<Action> updateFromLynx() {
        List<Action> actionsFromLynx = lynxConnector.findActions();
        List<Action> unpaidActions = actionRepository.findAllByPaymentIsNull();
        unpaidActions.forEach(action -> updateActionFromLynx(action, actionsFromLynx));
        actionRepository.saveAll(unpaidActions);
        return actionsFromLynx;
    }

    private void updateActionFromLynx(Action action, List<Action> actionsFromLynx) {
        Optional<Action> actionFromLynxOpt = actionsFromLynx.stream()
                                                            .filter(a -> a.getLynxActivityId().equals(action.getLynxActivityId()))
                                                            .findFirst();
        if (actionFromLynxOpt.isEmpty()) {
            action.setDeleted(true);
            return;
        }
        Action actionFromLynx = actionFromLynxOpt.get();
        action.setDate(actionFromLynx.getDate());
        action.setType(actionFromLynx.getType());
        action.setViv(actionFromLynx.getViv());
        action.setContext(actionFromLynx.getContext());
        action.setAchiever(actionFromLynx.getAchiever());
        action.setCreator(actionFromLynx.getCreator());
    }

}
