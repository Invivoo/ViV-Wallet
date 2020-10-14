package com.invivoo.vivwallet.api.interfaces.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.ViVWalletApiApplication;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@WebMvcTest(ActionsController.class)
public class ActionsControllerTest {

    public static final User TEST_ACHIEVER = User.builder()
                                                 .expertises(List.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA)
                                                                                  .build()))
                                                 .roles(List.of()).build();
    private static final Action ACTION1 = Action.builder()
                                                .id(1L)
                                                .date(LocalDateTime.now())
                                                .achiever(TEST_ACHIEVER)
                                                .type(ActionType.COACHING)
                                                .build();
    private static final Action ACTION2 = Action.builder()
                                                .id(2L)
                                                .date(LocalDateTime.now().minusDays(10))
                                                .achiever(TEST_ACHIEVER)
                                                .type(ActionType.COACHING)
                                                .build();
    private static final Action ACTION3 = Action.builder()
                                                .id(3L)
                                                .date(LocalDateTime.now().minusDays(100))
                                                .achiever(TEST_ACHIEVER)
                                                .type(ActionType.COACHING)
                                                .build();
    private static final List<Action> TEST_ACTIONS = Arrays.asList(ACTION1, ACTION2, ACTION3);

    private static final ObjectMapper mapper = new ViVWalletApiApplication().objectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActionService actionService;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @Test
    @WithMockUser(username = "Théophile MONTGOMERY", authorities = {"EXPERTISE_MANAGER", "SENIOR_MANAGER", "COMPANY_ADMINISTRATOR"})
    public void given_manager_when_calling_getActionsOrderedByDateDesc_then_return_sorted_actions() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(TEST_ACTIONS);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                                  .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(TEST_ACTIONS.stream().map(ActionDto::createFromAction).collect(Collectors.toList()))));
    }

    @Test
    @WithAnonymousUser
    public void given_anonymous_user_when_calling_getActionsOrderedByDateDesc_then_should_throw_exception() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(TEST_ACTIONS);
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                                  .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
