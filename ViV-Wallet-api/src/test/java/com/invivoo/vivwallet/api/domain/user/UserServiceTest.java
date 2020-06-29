package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private ActionRepository actionRepository;

    @Test
    public void should_return_a_balance_of_20_VIV_when_one_user_did_2_actions_of_10_VIV_not_paid() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).viv(new BigDecimal(10)).achiever(user).build();
        Action action2 = Action.builder().id((long) 2).viv(new BigDecimal(10)).achiever(user).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2);
        when(actionRepository.findAllByPaymentIsNull()).thenReturn(notPaidActions);
        UserService userService = new UserService(actionRepository);

        //When
        long balance = userService.computeBalance(user.getId());

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_20_VIV_for_user1_when_user1_did_2_actions_of_10_VIV_not_paid_and_user2_did_1_action_of_5_VIV_not_paid() {
        //Given
        User user1 = User.builder().id((long) 1).build();
        User user2 = User.builder().id((long) 2).build();
        Action action1 = Action.builder().id((long) 1).viv(new BigDecimal(10)).achiever(user1).build();
        Action action2 = Action.builder().id((long) 2).viv(new BigDecimal(10)).achiever(user1).build();
        Action action3 = Action.builder().id((long) 3).viv(new BigDecimal(5)).achiever(user2).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2, action3);
        when(actionRepository.findAllByPaymentIsNull()).thenReturn(notPaidActions);
        UserService userService = new UserService(actionRepository);

        //When
        long balance = userService.computeBalance(user1.getId());

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_0_VIV_when_no_not_paid_action() {
        //Given
        User user = User.builder().id((long) 1).build();
        when(actionRepository.findAllByPaymentIsNull()).thenReturn(Collections.emptyList());
        UserService userService = new UserService(actionRepository);

        //When
        long balance = userService.computeBalance(user.getId());

        //Then
        assertThat(balance).isEqualTo(0);
    }

}
