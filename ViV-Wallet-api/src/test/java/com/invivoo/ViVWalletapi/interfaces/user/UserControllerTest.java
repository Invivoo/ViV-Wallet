package com.invivoo.ViVWalletapi.interfaces.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.ViVWalletapi.domain.user.User;
import com.invivoo.ViVWalletapi.domain.user.UserRepository;
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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final User TEST_USER_1 = new User(1L, "User 1", "user1", "password");
    private static final User TEST_USER_2 = new User(2L, "User 2", "user2", "password");
    private static final List<User> TEST_USERS = Arrays.asList(TEST_USER_1, TEST_USER_2);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void whenGetUsers_shouldReturnUsers() throws Exception {
        //given
        Mockito.when(userRepository.findAll())
               .thenReturn(TEST_USERS);
        String jsonTestUsers = mapper.writeValueAsString(TEST_USERS);
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users"))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(jsonTestUsers))
                     .andExpect(MockMvcResultMatchers.jsonPath("$[0].login").value(TEST_USERS.get(0).getLogin()))
                     .andExpect(MockMvcResultMatchers.jsonPath("$[1].login").value(TEST_USERS.get(1).getLogin()));
    }

    @Test
    public void whenPostUser_shouldSaveAndReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        Mockito.when(userRepository.save(testUser))
               .thenReturn(testUser);
        String jsonTestUser = mapper.writeValueAsString(testUser);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users").contentType(APPLICATION_JSON_UTF8)
                                                                                 .content(jsonTestUser))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                     .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/api/v1/users/%s", testUser.getId())))
                     .andExpect(MockMvcResultMatchers.content().string(jsonTestUser));
    }

    @Test
    public void whenGetUser_shouldReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        Mockito.when(userRepository.findById(testUser.getId()))
               .thenReturn(Optional.of(testUser));
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/users/%s", testUser.getId())))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        String expectedJsonUser = mapper.writeValueAsString(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(expectedJsonUser));
    }

    @Test
    public void whenPutUserUpdate_shouldUpdateAndReturnUser() throws Exception {
        //given
        User testUser = TEST_USER_1;
        UserUpdateDto userUpdate = new UserUpdateDto("newFullName");
        Mockito.when(userRepository.findById(testUser.getId()))
               .thenReturn(Optional.of(testUser));
        User expectedUser = testUser.toBuilder()
                                    .fullName(userUpdate.getFullName())
                                    .build();
        Mockito.when(userRepository.save(expectedUser))
               .thenReturn(expectedUser);

        String jsonUserUpdate = mapper.writeValueAsString(userUpdate);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/users/%s", testUser.getId()))
                                                                                 .contentType(APPLICATION_JSON_UTF8)
                                                                                 .content(jsonUserUpdate))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        String expectedJsonUpdatedUser = mapper.writeValueAsString(expectedUser);
        Mockito.verify(userRepository, Mockito.times(1)).save(expectedUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(expectedJsonUpdatedUser));
    }

    @Test
    public void whenDeleteUser_shouldDeleteAndReturnOk() throws Exception {
        //given
        User testUser = TEST_USER_1;
        Mockito.when(userRepository.findById(testUser.getId()))
               .thenReturn(Optional.of(testUser));

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/users/%s", TEST_USER_1.getId())))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        Mockito.verify(userRepository, Mockito.times(1)).delete(testUser);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
