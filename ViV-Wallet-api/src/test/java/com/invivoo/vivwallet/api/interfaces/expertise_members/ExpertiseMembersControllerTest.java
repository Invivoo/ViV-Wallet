package com.invivoo.vivwallet.api.interfaces.expertise_members;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMember;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMemberRepository;
import com.invivoo.vivwallet.api.domain.expertise.ExpertiseMemberStatus;
import com.invivoo.vivwallet.api.domain.user.User;
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

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpertiseMembersController.class)
public class ExpertiseMembersControllerTest {
    private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final User TEST_USER_1 = new User(1L, "User 1", List.of());
    private static final User TEST_USER_2 = new User(2L, "User 2", List.of());
    private static final ExpertiseMember TEST_EM_1 = new ExpertiseMember(1L, TEST_USER_1, Expertise.PROGRAMMATION_JAVA, ExpertiseMemberStatus.CONSULTANT_SENIOR, LocalDate.now().minusYears(5), null);
    private static final ExpertiseMember TEST_EM_2 = new ExpertiseMember(2L, TEST_USER_2, Expertise.PROGRAMMATION_C_SHARP, ExpertiseMemberStatus.CONSULTANT_SENIOR_IN_ONBOARDING, LocalDate.now().minusYears(1), null);
    private static final List<ExpertiseMember> TEST_EXPERTISE_MEMBERS = Arrays.asList(TEST_EM_1, TEST_EM_2);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpertiseMemberRepository expertiseMemberRepository;

    @MockBean
    private JWTTokenProvider jwtTokenProvider;

    public ExpertiseMembersControllerTest() {
    }

    @BeforeClass
    public static void beforeClass() {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void whenGetExpertiseMembers_shouldReturnExpertiseMembers() throws Exception {
        //given
        Mockito.when(expertiseMemberRepository.findAll())
                .thenReturn(TEST_EXPERTISE_MEMBERS);
        String jsonTestExpertiseMembers = mapper.writeValueAsString(TEST_EXPERTISE_MEMBERS);
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/expertiseMembers"))
                .andDo(MockMvcResultHandlers.print());
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(jsonTestExpertiseMembers))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expertise").value(TEST_EXPERTISE_MEMBERS.get(0).getExpertise().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].expertise").value(TEST_EXPERTISE_MEMBERS.get(1).getExpertise().toString()));
    }

    @Test
    public void whenPostExpertiseMember_shouldSaveAndReturnExpertiseMember() throws Exception {
        //given
        ExpertiseMember testExpertiseMember = TEST_EM_1;
        Mockito.when(expertiseMemberRepository.save(testExpertiseMember))
                .thenReturn(testExpertiseMember);
        String jsonTestExpertiseMember = mapper.writeValueAsString(testExpertiseMember);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/expertiseMembers").contentType(APPLICATION_JSON_UTF8)
                .content(jsonTestExpertiseMember))
                .andDo(MockMvcResultHandlers.print());
        //then
        Mockito.verify(expertiseMemberRepository, Mockito.times(1)).save(testExpertiseMember);
        resultActions.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/api/v1/expertiseMembers/%s", testExpertiseMember.getId())))
                .andExpect(MockMvcResultMatchers.content().string(jsonTestExpertiseMember));
    }

    @Test
    public void whenGetExpertiseMember_shouldReturnExpertiseMember() throws Exception {
        //given
        ExpertiseMember testExpertiseMember = TEST_EM_1;
        Mockito.when(expertiseMemberRepository.findById(testExpertiseMember.getId()))
                .thenReturn(Optional.of(testExpertiseMember));
        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/v1/expertiseMembers/%s", testExpertiseMember.getId())))
                .andDo(MockMvcResultHandlers.print());
        //then
        String expectedJsonExpertiseMember = mapper.writeValueAsString(testExpertiseMember);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJsonExpertiseMember));
    }

    @Test
    public void whenPutExpertiseMemberUpdate_shouldUpdateAndReturnExpertiseMember() throws Exception {
        //given
        ExpertiseMember testExpertiseMember = TEST_EM_1;
        ExpertiseMemberUpdateDTO expertiseMemberUpdate = new ExpertiseMemberUpdateDTO(ExpertiseMemberStatus.CONSULTANT_SENIOR, LocalDate.now().minusYears(5), LocalDate.now());
        Mockito.when(expertiseMemberRepository.findById(testExpertiseMember.getId()))
                .thenReturn(Optional.of(testExpertiseMember));
        ExpertiseMember expectedExpertiseMember = testExpertiseMember.toBuilder()
                .status(expertiseMemberUpdate.getStatus())
                .startDate(expertiseMemberUpdate.getStartDate())
                .endDate(expertiseMemberUpdate.getEndDate())
                .build();
        Mockito.when(expertiseMemberRepository.save(expectedExpertiseMember))
                .thenReturn(expectedExpertiseMember);

        String jsonExpertiseMemberUpdate = mapper.writeValueAsString(expertiseMemberUpdate);

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.put(String.format("/api/v1/expertiseMembers/%s", testExpertiseMember.getId()))
                .contentType(APPLICATION_JSON_UTF8)
                .content(jsonExpertiseMemberUpdate))
                .andDo(MockMvcResultHandlers.print());
        //then
        String expectedJsonUpdatedExpertiseMember = mapper.writeValueAsString(expectedExpertiseMember);
        Mockito.verify(expertiseMemberRepository, Mockito.times(1)).save(expectedExpertiseMember);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedJsonUpdatedExpertiseMember));
    }

    @Test
    public void whenDeleteExpertiseMember_shouldDeleteAndReturnOk() throws Exception {
        //given
        ExpertiseMember testExpertiseMember = TEST_EM_1;
        Mockito.when(expertiseMemberRepository.findById(testExpertiseMember.getId()))
                .thenReturn(Optional.of(testExpertiseMember));

        //when
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/api/v1/expertiseMembers/%s", TEST_EM_1.getId())))
                .andDo(MockMvcResultHandlers.print());
        //then
        Mockito.verify(expertiseMemberRepository, Mockito.times(1)).delete(testExpertiseMember);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
