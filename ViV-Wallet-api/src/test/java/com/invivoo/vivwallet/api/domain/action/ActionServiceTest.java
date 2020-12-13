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
import java.util.List;

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
        when(actionRepository.findAllByAchieverOrderByDateDesc(user)).thenReturn(expectedActions);
        ActionService actionService = new ActionService(actionRepository, lynxConnector);

        //When
        List<Action> actions = actionService.findAllByAchieverOrderByDateDesc(user);

        //Then
        assertThat(actions).containsExactly(action2, action1);
    }

}
