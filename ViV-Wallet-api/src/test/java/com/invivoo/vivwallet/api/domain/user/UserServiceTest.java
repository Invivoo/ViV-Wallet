package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.payment.PaymentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActionRepository actionRepository;
    @Mock
    private PaymentRepository paymentRepository;

    @Test
    public void should_return_a_balance_of_20_VIV_when_one_user_did_2_actions_of_10_VIV_not_paid() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).vivAmount(10).achiever(user).build();
        Action action2 = Action.builder().id((long) 2).vivAmount(10).achiever(user).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2);
        when(actionRepository.findAllByAchieverAndDateAfter(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(notPaidActions);
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_20_VIV_for_user1_when_user1_did_2_actions_of_10_VIV_not_paid_and_user2_did_1_action_of_5_VIV_not_paid() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).vivAmount(10).achiever(user).build();
        Action action2 = Action.builder().id((long) 2).vivAmount(10).achiever(user).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2);
        when(actionRepository.findAllByAchieverAndDateAfter(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(notPaidActions);
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_0_VIV_when_no_not_paid_action() {
        //Given
        User user = User.builder().id((long) 1).build();
        when(actionRepository.findAllByAchieverAndDateAfter(Mockito.any(User.class), Mockito.any(LocalDateTime.class))).thenReturn(Collections.emptyList());
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(0);
    }

    @Test
    public void shouldCallFindByFullNameIgnoreCase_whenCallFindByUserName() {
        //Given
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository);
        String fullName = "fullName";

        //When
        userService.findByFullName(fullName);

        //Then
        verify(userRepository, Mockito.times(1)).findByFullNameIgnoreCase(fullName);
    }

}
