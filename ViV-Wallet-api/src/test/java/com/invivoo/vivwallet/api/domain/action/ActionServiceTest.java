package com.invivoo.vivwallet.api.domain.action;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ActionServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Mock
    private LynxConnector lynxConnector;

    @Test
    public void should_return_empty_list_when_no_actions_for_the_user() {
        //Given
        User user = User.builder().id((long) 1).build();
        ActionService actionService = new ActionService(actionRepository, lynxConnector);

        //When
        List<Action> actions = actionService.findAllByAchieverOrderByDateDesc(user);

        //Then
        assertThat(actions).isEmpty();
    }

    @Test
    public void should_return_only_actions_of_user_when_existing_actions_for_the_user() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).achiever(user).date(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0)).build();
        Action action2 = Action.builder().id((long) 2).achiever(user).date(LocalDateTime.of(2020, Month.JUNE, 1, 12, 0)).build();
        List<Action> expectedActions = Arrays.asList(action2, action1);
        when(actionRepository.findAllByAchieverAndValueDateAfter(user, LocalDateTime.MIN)).thenReturn(expectedActions);
        ActionService actionService = new ActionService(actionRepository, lynxConnector);

        //When
        List<Action> actions = actionService.findAllByAchieverOrderByDateDesc(user);

        //Then
        assertThat(actions).containsExactly(action2, action1);
    }

    @Test
    public void should_return_payable_action_representing_initial_balance_with_actions_in_desc_order_when_user_has_initial_balance_actions_before_and_after() {
        //Given
        User user = User.builder().id((long) 1).vivInitialBalance(10).vivInitialBalanceDate(LocalDateTime.of(2021, 1, 1, 0, 0)).build();
        Action actionBefore = Action.builder().id((long) 1).achiever(user).date(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0)).build();
        Action actionAfter = Action.builder().id((long) 2).achiever(user).date(LocalDateTime.of(2021, Month.JUNE, 1, 12, 0)).build();
        when(actionRepository.findAllByAchieverAndValueDateAfter(user, user.getVivInitialBalanceDate())).thenReturn(Arrays.asList(
                actionAfter,
                actionBefore));
        ActionService actionService = new ActionService(actionRepository, lynxConnector);
        Action expectedActionForInitialBalance = Action.builder()
                                                       .id(Action.ACTION_FOR_INITIAL_BALANCE_ID)
                                                       .achiever(user).type(ActionType.INITIAL_BALANCE)
                                                       .vivAmount(user.getVivInitialBalance())
                                                       .date(user.getVivInitialBalanceDate())
                                                       .status(ActionStatus.PAYABLE)
                                                       .context("Cumul des actions avant le 01/01/2021")
                                                       .build();

        //When
        List<Action> actions = actionService.findAllByAchieverOrderByDateDesc(user);

        //Then
        assertThat(actions).containsExactly(actionAfter, expectedActionForInitialBalance, actionBefore);
    }

    @Test
    public void should_return_payable_action_representing_initial_balance_when_user_has_initial_balance() {
        //Given
        User user = User.builder().id((long) 1).vivInitialBalance(10).vivInitialBalanceDate(LocalDateTime.of(2021, 1, 1, 0, 0)).build();
        when(actionRepository.findAllByAchieverAndValueDateAfter(user,
                                                                 user.getVivInitialBalanceDate())).thenReturn(Collections.emptyList());
        ActionService actionService = new ActionService(actionRepository, lynxConnector);
        Action expectedActionForInitialBalance = Action.builder()
                                                       .id(Action.ACTION_FOR_INITIAL_BALANCE_ID)
                                                       .achiever(user)
                                                       .type(ActionType.INITIAL_BALANCE)
                                                       .vivAmount(user.getVivInitialBalance())
                                                       .type(ActionType.INITIAL_BALANCE)
                                                       .date(user.getVivInitialBalanceDate())
                                                       .status(ActionStatus.PAYABLE)
                                                       .context("Cumul des actions avant le 01/01/2021")
                                                       .build();

        //When
        List<Action> actions = actionService.findAllByAchieverOrderByDateDesc(user);

        //Then
        assertThat(actions).containsExactly(expectedActionForInitialBalance);
    }

    @Test
    public void should_return_expected_action_representing_initial_balance_when_user_has_initial_balance() {
        //Given
        User user = User.builder().id((long) 1).vivInitialBalance(100).vivInitialBalanceDate(LocalDateTime.of(2021, 1, 1, 0, 0)).build();
        ActionService actionService = new ActionService(null, null);

        //When
        Optional<Action> actionForInitialBalance = actionService.getActionForInitialBalance(user);

        //then
        assertThat(actionForInitialBalance).isPresent()
                                           .contains(Action.builder()
                                                           .id(Action.ACTION_FOR_INITIAL_BALANCE_ID)
                                                           .achiever(user)
                                                           .type(ActionType.INITIAL_BALANCE)
                                                           .status(ActionStatus.PAYABLE)
                                                           .vivAmount(100)
                                                           .date(LocalDateTime.of(2021, 1, 1, 0, 0))
                                                           .context("Cumul des actions avant le 01/01/2021")
                                                           .build());

    }

}
