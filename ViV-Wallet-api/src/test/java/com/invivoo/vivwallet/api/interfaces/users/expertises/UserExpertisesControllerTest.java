package com.invivoo.vivwallet.api.interfaces.users.expertises;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.ViVWalletApiApplication;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(UserExpertisesController.class)
public class UserExpertisesControllerTest {

    private static final ObjectMapper mapper = new ViVWalletApiApplication().objectMapper();
    public static final User JANE_DOE = User.builder()
                                            .id(1L)
                                            .fullName("Jane Doe")
                                            .expertises(Stream.of(UserExpertise.builder()
                                                                               .id(1L)
                                                                               .expertise(Expertise.AGILITE_ET_CRAFT)
                                                                               .startDate(LocalDate.of(2017, 6, 1))
                                                                               .build())
                                                              .collect(Collectors.toCollection(HashSet::new)))
                                            .build();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    @Test
    public void whenGetUserExpertises_thenReturnUserExpertises() throws Exception {
        //given
        when(userService.findById(JANE_DOE.getId())).thenReturn(Optional.of(JANE_DOE));
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + JANE_DOE.getId() + "/expertises"))
                                                  .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(JANE_DOE.getExpertises().stream().map(UserExpertiseDto::createFromUserExpertise).collect(Collectors.toList()))));

    }

    @Test
    public void whenPostUserExpertise_thenShouldSaveAndReturnUserExpertise() throws Exception {
        //given
        when(userService.findById(JANE_DOE.getId())).thenReturn(Optional.of(JANE_DOE));
        UserExpertise newExpertise = UserExpertise.builder()
                                                  .expertise(Expertise.PROGRAMMATION_JAVA)
                                                  .startDate(LocalDate.of(2018, 6, 1))
                                                  .build();
        HashSet<UserExpertise> updatedExpertises = new HashSet<>(JANE_DOE.getExpertises());
        updatedExpertises.add(newExpertise);
        User updatedJaneDoe = JANE_DOE.toBuilder()
                                      .expertises(updatedExpertises)
                                      .build();
        when(userService.save(updatedJaneDoe)).thenReturn(updatedJaneDoe);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/" + JANE_DOE.getId() + "/expertises").contentType(MediaType.APPLICATION_JSON_UTF8)
                                                                            .content(mapper.writeValueAsString(UserExpertiseDto.createFromUserExpertise(newExpertise))))
                                             .andDo(MockMvcResultHandlers.print());
        //then
        verify(userService, Mockito.times(1)).save(updatedJaneDoe);
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                     .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/api/v1/users/%s/expertises/%s", JANE_DOE.getId(), newExpertise.getId())))
                     .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(UserExpertiseDto.createFromUserExpertise(newExpertise))));
    }

}
