package com.invivoo.vivwallet.api.interfaces.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.ViVWalletApiApplication;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.application.security.JWTUserDetails;
import com.invivoo.vivwallet.api.application.security.RoleGrantedAuthority;
import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@WebMvcTest(ActionsController.class)
public class ActionsControllerTest {

    public static final User TEST_ACHIEVER_JAVA = User.builder()
                                                      .expertises(Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA)
                                                                                      .build()))
                                                      .roles(Set.of()).build();
    public static final User TEST_ACHIEVER_C_SHARP = User.builder()
                                                         .expertises(Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_C_SHARP)
                                                                                         .build()))
                                                         .roles(Set.of()).build();
    private static final Action JAVA_ACTION1 = Action.builder()
                                                     .id(1L)
                                                     .date(LocalDateTime.now())
                                                     .achiever(TEST_ACHIEVER_JAVA)
                                                     .type(ActionType.COACHING)
                                                     .build();
    private static final Action JAVA_ACTION2 = Action.builder()
                                                     .id(2L)
                                                     .date(LocalDateTime.now().minusDays(10))
                                                     .achiever(TEST_ACHIEVER_JAVA)
                                                     .type(ActionType.COACHING)
                                                     .build();
    private static final Action C_SHARP_ACTION = Action.builder()
                                                       .id(3L)
                                                       .date(LocalDateTime.now().minusDays(100))
                                                       .achiever(TEST_ACHIEVER_C_SHARP)
                                                       .type(ActionType.COACHING)
                                                       .build();

    private static final ObjectMapper mapper = new ViVWalletApiApplication().objectMapper();
    public static final String SENIOR_MANAGER = "SENIOR_MANAGER";
    public static final String COMPANY_ADMINISTRATOR = "COMPANY_ADMINISTRATOR";
    public static final String EXPERTISE_MANAGER = "EXPERTISE_MANAGER";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private ActionService actionService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @TestConfiguration
    static class TestUserDetailsConfiguration {

        @Bean("testUserDetailsService")
        UserDetailsService testUserDetailsService() {
            return s -> new JWTUserDetails(s, null) {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return List.of(new RoleGrantedAuthority(RoleType.valueOf(s)));
                }
            };
        }
    }

    @Test
    @WithUserDetails(value = SENIOR_MANAGER, userDetailsServiceBeanName = "testUserDetailsService")
    public void shouldReturnSortedActionsForAchieverWithSameExpertise_whenSeniorManagerCallingGetActionsOrderedByDateDesc() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION));
        Mockito.when(securityService.connectedUserHasAnyRole(RoleType.SENIOR_MANAGER, RoleType.COMPANY_ADMINISTRATOR)).thenCallRealMethod();
        Mockito.when(securityService.getAllowedExpertiseForConnectedUser()).thenCallRealMethod();
        Mockito.when(securityService.getConnectedUser()).thenReturn(Optional.of(TEST_ACHIEVER_JAVA));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION).stream().map(ActionDto::createFromAction).collect(Collectors.toList()))));
    }

    @Test
    @WithUserDetails(value = COMPANY_ADMINISTRATOR, userDetailsServiceBeanName = "testUserDetailsService")
    public void shouldReturnSortedActionsForAchieverWithSameExpertise_whenCompanyAdministratorCallingGetActionsOrderedByDateDesc() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION));
        Mockito.when(securityService.connectedUserHasAnyRole(RoleType.SENIOR_MANAGER, RoleType.COMPANY_ADMINISTRATOR)).thenCallRealMethod();
        Mockito.when(securityService.getAllowedExpertiseForConnectedUser()).thenCallRealMethod();
        Mockito.when(securityService.getConnectedUser()).thenReturn(Optional.of(TEST_ACHIEVER_JAVA));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION).stream().map(ActionDto::createFromAction).collect(Collectors.toList()))));
    }

    @Test
    @WithUserDetails(value = EXPERTISE_MANAGER, userDetailsServiceBeanName = "testUserDetailsService")
    public void shouldReturnSortedActionsForAchieverWithSameExpertise_whenExpertiseManagerCallingGetActionsOrderedByDateDesc() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION));
        Mockito.when(securityService.connectedUserHasAnyRole(RoleType.SENIOR_MANAGER, RoleType.COMPANY_ADMINISTRATOR)).thenCallRealMethod();
        Mockito.when(securityService.getAllowedExpertiseForConnectedUser()).thenCallRealMethod();
        Mockito.when(securityService.getConnectedUser()).thenReturn(Optional.of(TEST_ACHIEVER_C_SHARP));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(List.of(C_SHARP_ACTION).stream().map(ActionDto::createFromAction).collect(Collectors.toList()))));
    }

    @Test
    @WithAnonymousUser
    public void given_anonymous_user_when_calling_getActionsOrderedByDateDesc_then_should_throw_exception() throws Exception {
        //given
        Mockito.when(actionService.findAllByOrderByDateDesc())
               .thenReturn(List.of(JAVA_ACTION1, JAVA_ACTION2, C_SHARP_ACTION));
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/actions"))
                                             .andDo(MockMvcResultHandlers.print());

        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
