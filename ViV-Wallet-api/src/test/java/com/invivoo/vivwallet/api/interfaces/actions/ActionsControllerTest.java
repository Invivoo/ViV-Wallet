package com.invivoo.vivwallet.api.interfaces.actions;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.invivoo.vivwallet.api.ViVWalletApiApplication;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionRepository;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ViVWalletApiApplication.class},
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActionsControllerTest {

    private static final Action ACTION1 = Action.builder().id(1L).date(LocalDateTime.now()).build();
    private static final Action ACTION2 = Action.builder().id(2L).date(LocalDateTime.now().minusDays(10)).build();
    private static final Action ACTION3 = Action.builder().id(3L).date(LocalDateTime.now().minusDays(100)).build();
    private static final List<Action> TEST_ACTIONS = Arrays.asList(ACTION1, ACTION2, ACTION3);

    private static final ObjectMapper MAPPER;
    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new Jdk8Module());
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        MAPPER.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private ActionRepository actionRepository;
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "Théophile MONTGOMERY", roles = {"EXPERTISE_MANAGER", "SENIOR_MANAGER", "COMPANY_ADMINISTRATOR"})
    public void given_manager_when_calling_getActionsOrderedByDateDesc_then_return_sorted_actions() throws Exception {
        //given
        Mockito.when(actionRepository.findAllByOrderByDateDesc())
               .thenReturn(TEST_ACTIONS);
        String jsonTestActions = MAPPER.writeValueAsString(TEST_ACTIONS);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                                  .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(jsonTestActions))
                     .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(ACTION1.getId()));
    }

    @Test
    @WithMockUser(username = "Marouane SALIM", roles = {"CONSULTANT"})
    public void given_user_with_only_role_consultant_when_calling_getActionsOrderedByDateDesc_then_should_throw_exception() throws Exception {
        //given
        Mockito.when(actionRepository.findAllByOrderByDateDesc())
               .thenReturn(TEST_ACTIONS);
        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                                                              .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractThrowableAssert abstractThrowableAssert = Assertions.assertThatThrownBy(throwingCallable);
        //then
        abstractThrowableAssert.hasCause(new AccessDeniedException("Access is denied"));
    }

    @Test
    @WithAnonymousUser
    public void given_anonymous_user_when_calling_getActionsOrderedByDateDesc_then_should_throw_exception() throws Exception {
        //given
        Mockito.when(actionRepository.findAllByOrderByDateDesc())
               .thenReturn(TEST_ACTIONS);
        //when
        ThrowableAssert.ThrowingCallable throwingCallable = () -> this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                                                              .andExpect(MockMvcResultMatchers.status().isOk());

        AbstractThrowableAssert abstractThrowableAssert = Assertions.assertThatThrownBy(throwingCallable);
        //then
        abstractThrowableAssert.hasCause(new AccessDeniedException("Access is denied"));
    }

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

}
