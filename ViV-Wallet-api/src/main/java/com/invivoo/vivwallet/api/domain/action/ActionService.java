package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActionService {

    public static final LocalDateTime MINIMUM_SART_VALUE_DATE_VALID_FOR_JPA_QUERY = LocalDateTime.of(2000, Month.JANUARY, 1, 0, 0);
    private final ActionRepository actionRepository;
    private final LynxConnector lynxConnector;

    public ActionService(ActionRepository actionRepository, LynxConnector lynxConnector) {
        this.actionRepository = actionRepository;
        this.lynxConnector = lynxConnector;
    }

    public List<Action> findAllByOrderByDateDesc() {
        return actionRepository.findAllByIsDeletedFalseOrderByDateDesc();
    }

    public List<Action> findAllByAchieverOrderByDateDesc(User achiever) {
        LocalDateTime startValueDate = Optional.ofNullable(achiever.getVivInitialBalanceDate()).orElse(MINIMUM_SART_VALUE_DATE_VALID_FOR_JPA_QUERY);
        ArrayList<Action> actions = new ArrayList<>(actionRepository.findAllByAchieverAndValueDateAfter(achiever, startValueDate));
        getActionForInitialBalance(achiever).ifPresent(actions::add);
        actions.sort(Comparator.comparing(Action::getDate).reversed());
        return actions;
    }

    public List<Action> findAllByAchieverAndIdIn(User achiever, List<Long> actionIds) {
        return actionRepository.findAllByAchieverAndIdIn(achiever, actionIds);
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

    public void deleteAll() {
        actionRepository.deleteAll();
    }

    protected Optional<Action> getActionForInitialBalance(User achiever) {
        if (achiever.getVivInitialBalance() == 0) {
            return Optional.empty();
        }
        return Optional.of(Action.builder()
                                 .id(Action.ACTION_FOR_INITIAL_BALANCE_ID)
                                 .achiever(achiever).type(ActionType.INITIAL_BALANCE)
                                 .status(ActionStatus.PAYABLE)
                                 .vivAmount(achiever.getVivInitialBalance())
                                 .date(achiever.getVivInitialBalanceDate())
                                 .context(String.format("Cumul des actions avant le %s", achiever.getVivInitialBalanceDate().format(
                                         DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
                                 .build());
    }

    private List<Action> updateActions(List<Action> actionsFromLynx) {
        actionRepository.deleteAllByStatus(ActionStatus.TO_VALIDATE);
        Map<String, Action> validatedActions = actionRepository.findAllByLynxActivityIdIn(actionsFromLynx.stream()
                                                                                                            .map(Action::getLynxActivityId)
                                                                                                            .collect(
                                                                                                                                       Collectors.toList()))
                                                                  .stream()
                                                                  .collect(Collectors.toMap(this::getActionUniqueKey, a -> a));
        List<Action> actionsToSave = actionsFromLynx.stream()
                                                    .filter(actionFromLynx -> !validatedActions.containsKey(getActionUniqueKey(
                                                            actionFromLynx)))
                                                    .collect(Collectors.toList());
        return actionRepository.saveAll(actionsToSave);
    }

    private String getActionUniqueKey(Action a) {
        return a.getLynxActivityId().toString() + "_" + a.getType() + a.getAchiever().getId().toString();
    }

}
