package com.invivoo.vivwallet.api.domain.user;

import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import com.invivoo.vivwallet.api.domain.action.ActionStatus;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertiseStatus;
import com.invivoo.vivwallet.api.domain.payment.PaymentRepository;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final User TEST_USER_1 = new User(1L,
                                                     "User 1",
                                                     "User 1",
                                                     "user1@mail.com",
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).status(UserExpertiseStatus.CONSULTANT).build()),
                                                     Set.of());
    private static final User TEST_USER_2 = new User(2L,
                                                     "User 2",
                                                     "User 2",
                                                     "user2@mail.com",
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).build()),
                                                     Set.of());
    private static final User TEST_USER_3 = new User(2L,
                                                     "User 2",
                                                     "User 2",
                                                     null,
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).build()),
                                                     Set.of());
    private static final User TEST_USER_4 = new User(2L,
                                                     "User 2",
                                                     "User 2",
                                                     "   ",
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).build()),
                                                     Set.of());
    private static final List<User> TEST_USERS = Arrays.asList(TEST_USER_1, TEST_USER_2);

    @Mock
    private UserRepository userRepository;
    @Mock
    private ActionRepository actionRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private LynxConnector lynxConnector;
    @InjectMocks
    private UserService userService;

    @Test
    public void should_return_a_balance_of_20_VIV_when_one_user_did_2_actions_of_10_VIV_not_paid() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).vivAmount(10).valueDate(LocalDateTime.of(2020, 1, 1, 0, 0)).achiever(user).build();
        Action action2 = Action.builder().id((long) 2).vivAmount(10).valueDate(LocalDateTime.of(2020, 1, 1, 0, 0)).achiever(user).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2);
        when(actionRepository.findAllByAchieverAndValueDateAfterAndStatus(Mockito.any(User.class),
                                                                          Mockito.any(LocalDateTime.class),
                                                                          Mockito.any(
                                                                                  ActionStatus.class))).thenReturn(notPaidActions);
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository, lynxConnector);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_20_VIV_for_user1_when_user1_did_2_actions_of_10_VIV_not_paid_and_user2_did_1_action_of_5_VIV_not_paid() {
        //Given
        User user = User.builder().id((long) 1).build();
        Action action1 = Action.builder().id((long) 1).vivAmount(10).valueDate(LocalDateTime.of(2020, 1, 1, 0, 0)).achiever(user).build();
        Action action2 = Action.builder().id((long) 2).vivAmount(10).valueDate(LocalDateTime.of(2020, 1, 1, 0, 0)).achiever(user).build();
        List<Action> notPaidActions = Arrays.asList(action1, action2);
        when(actionRepository.findAllByAchieverAndValueDateAfterAndStatus(Mockito.any(User.class),
                                                                          Mockito.any(LocalDateTime.class),
                                                                          Mockito.any(
                                                                                  ActionStatus.class))).thenReturn(notPaidActions);
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository, lynxConnector);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(20);
    }

    @Test
    public void should_return_a_balance_of_0_VIV_when_no_not_paid_action() {
        //Given
        User user = User.builder().id((long) 1).build();
        when(actionRepository.findAllByAchieverAndValueDateAfterAndStatus(Mockito.any(User.class),
                                                                          Mockito.any(LocalDateTime.class),
                                                                          Mockito.any(
                                                                                  ActionStatus.class))).thenReturn(Collections.emptyList());
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository, lynxConnector);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isZero();
    }

    @Test
    public void should_return_a_balance_of_10_VIV_when_initial_balance_is_10__and_no_action_done() {
        //Given
        User user = User.builder().id((long) 1).vivInitialBalance(10).build();
        when(actionRepository.findAllByAchieverAndValueDateAfterAndStatus(Mockito.any(User.class),
                                                                          Mockito.any(LocalDateTime.class),
                                                                          Mockito.any(
                                                                                  ActionStatus.class))).thenReturn(Collections.emptyList());
        UserService userService = new UserService(userRepository, actionRepository, paymentRepository, lynxConnector);

        //When
        long balance = userService.computeBalance(user);

        //Then
        assertThat(balance).isEqualTo(10);
    }

    @Test
    public void shouldCallFindByFullNameIgnoreCase_whenCallFindByUserName() {
        //Given
        String fullName = "fullName";

        //When
        userService.findByFullName(fullName);

        //Then
        verify(userRepository, Mockito.times(1)).findByFullNameTrimIgnoreCase(fullName);
    }

    @Test
    public void should_return_list_of_users_added_updateFromLynx() {
        // GIVEN
        when(lynxConnector.findUsers()).thenReturn(TEST_USERS);
        User existingUserWithoutEmail = new User(1L,
                                                 "User 1",
                                                 "User 1",
                                                 null,
                                                 Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).status(
                                                         UserExpertiseStatus.CONSULTANT).build()),
                                                 Set.of());
        when(userRepository.findAll()).thenReturn(Collections.singletonList(existingUserWithoutEmail));
        when(userRepository.saveAll(TEST_USERS)).thenReturn(TEST_USERS);

        // WHEN
        List<User> result = userService.updateFromLynx();

        // THEN
        assertThat(result).isEqualTo(TEST_USERS);
        verify(lynxConnector).findUsers();
        verify(userRepository).saveAll(TEST_USERS);

    }
}
