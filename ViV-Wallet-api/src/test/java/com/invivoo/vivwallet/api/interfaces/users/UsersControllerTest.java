package com.invivoo.vivwallet.api.interfaces.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.application.security.SecurityService;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.action.ActionStatus;
import com.invivoo.vivwallet.api.domain.action.ActionType;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.actions.ActionDto;
import com.invivoo.vivwallet.api.interfaces.payments.PaymentDto;
import lombok.Data;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Data
@RunWith(SpringRunner.class)
@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                                         MediaType.APPLICATION_JSON.getSubtype(),
                                                                         StandardCharsets.UTF_8);
    private static final User TEST_USER_1 = new User(1L,
                                                     "User 1",
                                                     "User 1",
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).build()),
                                                     Set.of());
    private static final User TEST_USER_2 = new User(2L,
                                                     "User 2",
                                                     "User 2",
                                                     Set.of(UserExpertise.builder().expertise(Expertise.PROGRAMMATION_JAVA).build()),
                                                     Set.of());
    private static final List<User> TEST_USERS = Arrays.asList(TEST_USER_1, TEST_USER_2);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private ActionService actionService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;


    public UsersControllerTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void whenGetUsers_shouldReturnUsers() throws Exception {
        //given
        when(userService.findAll()).thenReturn(TEST_USERS);
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content()
                                                     .string(mapper.writeValueAsString(TEST_USERS.stream()
                                                                                                 .map(UserDto::createFromUser)
                                                                                                 .collect(Collectors.toList()))))
                     .andExpect(MockMvcResultMatchers.jsonPath("$[0].fullName").value(TEST_USERS.get(0).getFullName()))
                     .andExpect(MockMvcResultMatchers.jsonPath("$[1].fullName").value(TEST_USERS.get(1).getFullName()));
    }

    @Test
    public void whenPostUser_shouldSaveAndReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        when(userService.save(Mockito.any(User.class))).thenReturn(testUser);
        when(userService.save(Mockito.any(User.class))).thenReturn(testUser);
        when(userService.save(Mockito.any(User.class))).thenReturn(testUser);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                                                                                 .contentType(APPLICATION_JSON_UTF8)
                                                                                 .content(mapper.writeValueAsString(testUser)))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        verify(userService, Mockito.times(1)).save(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                     .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/api/v1/users/%s", testUser.getId())))
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(UserDto.createFromUser(testUser))));
    }

    @Test
    public void whenGetUser_shouldReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s", testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        String expectedJsonUser = mapper.writeValueAsString(UserDto.createFromUser(testUser));
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedJsonUser));
    }

    @Test
    public void whenPutUserUpdate_shouldUpdateAndReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        UserUpdateDto userUpdate = new UserUpdateDto("newFullName");
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        User expectedUser = testUser.toBuilder().fullName(userUpdate.getFullName()).build();
        when(userService.save(expectedUser)).thenReturn(expectedUser);

        String jsonUserUpdate = mapper.writeValueAsString(userUpdate);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/users/%s", testUser.getId()))
                                                                                 .contentType(APPLICATION_JSON_UTF8)
                                                                                 .content(jsonUserUpdate))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        verify(userService, Mockito.times(1)).save(expectedUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content()
                                                     .string(mapper.writeValueAsString(UserDto.createFromUser(TEST_USER_1.toBuilder()
                                                                                                                         .fullName(
                                                                                                                                 userUpdate.getFullName())
                                                                                                                         .build()))));
    }

    @Test
    public void whenDeleteUser_shouldDeleteAndReturnOk() throws Exception {
        //given
        User testUser = TEST_USER_1;
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/users/%s",
                                                                                                       TEST_USER_1.getId())))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        verify(userService, Mockito.times(1)).delete(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void whenGetBalanceForUser_shouldReturnBalance() throws Exception {
        //Given
        User testUser = TEST_USER_1;
        int expectedBalance = 20;
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userService.computeBalance(testUser)).thenReturn(expectedBalance);

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s/balance",
                                                                                                    testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        verify(userService).computeBalance(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.jsonPath("$.value", Matchers.is(20)));
    }

    @Test
    public void whenGetListOfActionsForUserInAnExpertise_shouldReturnListOfActionsWithExpertiseInformation() throws Exception {
        //Given
        User testUser = TEST_USER_1;
        Action action1 = anNonPayableAction(testUser);
        Action action2 = aPayableAction(testUser);
        List<Action> expectedActions = Arrays.asList(action2, action1);
        when(actionService.findAllByAchieverOrderByDateDesc(testUser)).thenReturn(expectedActions);

        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s/actions",
                                                                                                    testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        List<ActionDto> expectedActionDtos = Arrays.asList(ActionDto.createFromAction(action2), ActionDto.createFromAction(action1));
        String expectedJson = mapper.writeValueAsString(expectedActionDtos);

        verify(actionService).findAllByAchieverOrderByDateDesc(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    public void whenGetListOfActionsForUserInAnExpertiseWithInitialBalance_shouldReturnListOfActionsWithExpertiseInformationAndActionRepresentingInitialBalanceInExpectedOrder() throws
                                                                                                                                                                                 Exception {
        //Given
        User testUser = TEST_USER_1;
        Action action1 = anNonPayableAction(testUser);
        Action action2 = aPayableAction(testUser);
        List<Action> expectedActions = Arrays.asList(action2, action1);
        when(actionService.findAllByAchieverOrderByDateDesc(testUser)).thenReturn(expectedActions);

        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s/actions",
                                                                                                    testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        List<ActionDto> expectedActionDtos = Arrays.asList(ActionDto.createFromAction(action2), ActionDto.createFromAction(action1));
        String expectedJson = mapper.writeValueAsString(expectedActionDtos);

        verify(actionService).findAllByAchieverOrderByDateDesc(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    public void whenGetListOfActionsForUserNotInAnExpertise_shouldReturnListOfActions() throws Exception {
        //Given
        Action action1 = anNonPayableAction(TEST_USER_1);
        Action action2 = aPayableAction(TEST_USER_1);
        List<Action> expectedActions = Arrays.asList(action2, action1);
        when(userService.findById(TEST_USER_1.getId())).thenReturn(Optional.of(TEST_USER_1));
        when(actionService.findAllByAchieverOrderByDateDesc(TEST_USER_1)).thenReturn(expectedActions);

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s/actions",
                                                                                                    TEST_USER_1.getId())))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        List<ActionDto> expectedActionDtos = Arrays.asList(ActionDto.createFromAction(action2), ActionDto.createFromAction(action1));
        String expectedJson = mapper.writeValueAsString(expectedActionDtos);

        verify(actionService).findAllByAchieverOrderByDateDesc(TEST_USER_1);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    public void whenGetListOfPaymentsForUser_shouldReturnListOfPayments() throws Exception {
        //Given
        User testUser = TEST_USER_1;
        PaymentDto payment1 = PaymentDto.builder()
                                        .id(1L)
                                        .userId(testUser.getId())
                                        .date(LocalDate.of(2020, Month.JANUARY, 1))
                                        .viv(50)
                                        .amount(BigDecimal.valueOf(250))
                                        .build();
        PaymentDto payment2 = PaymentDto.builder()
                                        .id(2L)
                                        .userId(testUser.getId())
                                        .date(LocalDate.of(2020, Month.JUNE, 1))
                                        .viv(50)
                                        .amount(BigDecimal.valueOf(250))
                                        .build();
        List<PaymentDto> expectedPayments = Arrays.asList(payment2, payment1);
        when(paymentService.findAllByReceiver(testUser.getId())).thenReturn(expectedPayments);

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s/payments",
                                                                                                    testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        verify(paymentService).findAllByReceiver(testUser.getId());
        String expectedJson = mapper.writeValueAsString(expectedPayments);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.content().string(expectedJson));
    }

    @Test
    public void whenSendActionsUpdateRequest_shouldReturnUpdatedActions() throws Exception {
        //Given
        User testUser = TEST_USER_1;
        when(userService.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        Action givenPublicationActionToValidate = Action.builder()
                                                        .id(1L)
                                                        .date(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0))
                                                        .type(ActionType.ARTICLE_PUBLICATION)
                                                        .status(ActionStatus.TO_VALIDATE)
                                                        .vivAmount(15)
                                                        .context("This is a comment")
                                                        .achiever(testUser)
                                                        .build();
        Action givenCoachingActionToValidate = Action.builder()
                                                     .id(2L)
                                                     .date(LocalDateTime.of(2021, Month.FEBRUARY, 1, 12, 0))
                                                     .type(ActionType.COACHING)
                                                     .status(ActionStatus.TO_VALIDATE)
                                                     .vivAmount(10)
                                                     .context("This is a comment")
                                                     .achiever(testUser)
                                                     .build();
        Mockito.when(actionService.findAllByAchieverAndIdIn(testUser, List.of(1L, 2L)))
               .thenReturn(List.of(givenPublicationActionToValidate, givenCoachingActionToValidate));

        //When
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post(String.format("/api/v1/users/%s/actions",
                                                                                                     testUser.getId()))
                                                                                 .content(
                                                                                         "{\"updates\": [{\"id\": \"1\", \"status\": "
                                                                                         + "\"PAYABLE\"}, "
                                                                                         + "{\"id\": \"2\", \"status\": \"NON_PAYABLE\"}]}")
                                                                                 .contentType(APPLICATION_JSON_UTF8))
                                                  .andDo(MockMvcResultHandlers.print());

        //Then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string("[{\"id\":1,\"userId\":1,\"type\":\"Article moyen 500 – 2500 "
                                                                       + "mots\",\"comment\":\"This is a comment\","
                                                                       + "\"creationDate\":\"2020-01-01T12:00:00\",\"valueDate\":null,"
                                                                       + "\"payment\":15,\"status\":\"PAYABLE\",\"achiever\":{\"id\":1,"
                                                                       + "\"fullName\":\"User 1\","
                                                                       + "\"expertise\":{\"id\":\"PROGRAMMATION_JAVA\","
                                                                       + "\"expertiseName\":\"Programmation Java\"},\"status\":null,"
                                                                       + "\"startDate\":null,\"endDate\":null,\"roles\":[]}},{\"id\":2,"
                                                                       + "\"userId\":1,\"type\":\"Coaching\",\"comment\":\"This is a "
                                                                       + "comment\",\"creationDate\":\"2021-02-01T12:00:00\","
                                                                       + "\"valueDate\":null,\"payment\":10,\"status\":\"NON_PAYABLE\","
                                                                       + "\"achiever\":{\"id\":1,\"fullName\":\"User 1\","
                                                                       + "\"expertise\":{\"id\":\"PROGRAMMATION_JAVA\","
                                                                       + "\"expertiseName\":\"Programmation Java\"},\"status\":null,"
                                                                       + "\"startDate\":null,\"endDate\":null,\"roles\":[]}}]"));
    }

    private Action anNonPayableAction(User user) {
        return Action.builder()
                     .id(1L)
                     .date(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0))
                     .type(ActionType.ARTICLE_PUBLICATION)
                     .status(ActionStatus.NON_PAYABLE)
                     .vivAmount(15)
                     .context("This is a comment")
                     .achiever(user)
                     .build();
    }

    private Action aPayableAction(User user) {
        return Action.builder()
                     .id(2L)
                     .date(LocalDateTime.of(2020, Month.JUNE, 1, 12, 0))
                     .type(ActionType.COACHING)
                     .status(ActionStatus.PAYABLE)
                     .vivAmount(10)
                     .context("This is a comment")
                     .achiever(user)
                     .build();
    }
}
