package com.invivoo.vivwallet.api.infrastructure.lynx;


import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.ActivityToActionMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.mapper.LynxUserToUserMapper;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.ActivityType;
import org.assertj.core.api.Condition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(LynxConnectorConfiguration.class)
@PropertySource("classpath:application.yml")
public class LynxConnectorTest {

    @Autowired
    private RestTemplate restTemplate;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private MockRestServiceServer mockServer;

    @Before
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void should_return_activities_when_get_from_lynx() throws IOException, URISyntaxException {
        // given
        LynxConnector lynxConnector = new LynxConnector(restTemplate, "http://vivApiUrl", "http://userApiUrl", null, null);

        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("lynx-response.json")).toURI());
        String expectedLynxResponse = Files.lines(lynxResponse).collect(Collectors.joining());
        mockServer.expect(MockRestRequestMatchers.anything()).andRespond(MockRestResponseCreators.withSuccess(expectedLynxResponse, MediaType.APPLICATION_JSON_UTF8));

        // when
        List<Activity> actualActivities = lynxConnector.findActivities();

        //then
        Activity expectedActivity = Activity.builder()
                                            .funnelStageId(39L)
                                            .stageName("TechFcl Eval with feedback")
                                            .id(186221L)
                                            .typeId(14L)
                                            .type(ActivityType.EVALUATION_TECHNIQUE_OK_SUR_PROFIL)
                                            .date(LocalDateTime.of(2020, 1, 13, 18, 0, 1))
                                            .valueDate(LocalDateTime.of(2020, 1, 13, 0, 0))
                                            .status("Held")
                                            .comment("Echange fonctionnel")
                                            .relatedTo("Candidat Externe")
                                            .owner("Collaborateur Invivoo")
                                            .participantEmail("recruteur.invivoo@invivoo.com")
                                            .others("Chargé-De-Recrutement Invivoo")
                                            .opportunity(null)
                                            .invEntry(LocalDateTime.of(2020, 5, 11, 0, 0))
                                            .startActFrom(null)
                                            .build();
        Optional<Activity> actualActivity = actualActivities.stream().filter(a -> a.getId().equals(expectedActivity.getId())).findFirst();

        Assert.assertTrue(actualActivity.isPresent());
        Assert.assertEquals(expectedActivity, actualActivity.get());
    }

    @Test
    public void should_get_2_actions_with_10_and_40_viv_for_a_winning_coaching_done_by_a_single_collaborator() throws URISyntaxException, IOException {
        // Given
        when(userRepository.findByFullNameTrimIgnoreCase("Collaborateur1")).thenReturn(
                Optional.of(new User(1L, "Collaborateur1", "Collaborateur1", emptySet(), emptySet())));
        LynxConnector lynxConnector = new LynxConnector(restTemplate, "http://vivApiUrl", "http://userApiUrl", new ActivityToActionMapper(userRepository), new LynxUserToUserMapper());

        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("lynx-response-winning-coaching-1-collaborator.json")).toURI());
        String expectedLynxResponse = Files.lines(lynxResponse).collect(Collectors.joining());
        mockServer.expect(MockRestRequestMatchers.anything()).andRespond(MockRestResponseCreators.withSuccess(expectedLynxResponse, MediaType.APPLICATION_JSON_UTF8));

        // When
        List<Action> actions = lynxConnector.findActions();

        // Then
        assertThat(actions).extracting(Action::getVivAmount).containsExactlyInAnyOrder(10, 40);
    }

    @Test
    public void should_get_2_actions_with_10_and_20_viv_by_collaborator_for_a_winning_coaching_done_by_2_collaborators() throws URISyntaxException, IOException {
        // Given
        User collaborator1 = new User(1L, "Collaborateur1", "Collaborateur1", emptySet(), emptySet());
        when(userRepository.findByFullNameTrimIgnoreCase(collaborator1.getFullName())).thenReturn(Optional.of(collaborator1));
        User collaborator2 = new User(2L, "Collaborateur2", "Collaborateur2", emptySet(), emptySet());
        when(userRepository.findByFullNameTrimIgnoreCase(collaborator2.getFullName())).thenReturn(Optional.of(collaborator2));
        LynxConnector lynxConnector = new LynxConnector(restTemplate, "http://vivApiUrl", "http://userApiUrl", new ActivityToActionMapper(userRepository), new LynxUserToUserMapper());

        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("lynx-response-winning-coaching-2-collaborators.json")).toURI());
        String expectedLynxResponse = Files.lines(lynxResponse).collect(Collectors.joining());
        mockServer.expect(MockRestRequestMatchers.anything()).andRespond(MockRestResponseCreators.withSuccess(expectedLynxResponse, MediaType.APPLICATION_JSON_UTF8));

        // When
        List<Action> actions = lynxConnector.findActions();

        // Then
        List<Action> collaborator1Actions = actions.stream().filter(action -> action.getAchiever().equals(collaborator1)).collect(toList());
        assertThat(collaborator1Actions).extracting(Action::getVivAmount).containsExactlyInAnyOrder(10, 20);
        List<Action> collaborator2Actions = actions.stream().filter(action -> action.getAchiever().equals(collaborator2)).collect(toList());
        assertThat(collaborator2Actions).extracting(Action::getVivAmount).containsExactlyInAnyOrder(10, 20);
    }

    @Test
    public void should_get_all_lynx_users() throws URISyntaxException, IOException {
        // GIVEN
        LynxConnector lynxConnector = new LynxConnector(restTemplate, "http://vivApiUrl", "http://userApiUrl", new ActivityToActionMapper(userRepository), new LynxUserToUserMapper());

        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("lynx-user-response.json")).toURI());
        String expectedLynxResponse = Files.lines(lynxResponse).collect(Collectors.joining());
        mockServer.expect(MockRestRequestMatchers.anything()).andRespond(MockRestResponseCreators.withSuccess(expectedLynxResponse, MediaType.APPLICATION_JSON_UTF8));

        // WHEN
        List<User> users = lynxConnector.findUsers();

        // THEN
        Predicate<? super List<? extends  User>> isNotEmpty = u -> !u.isEmpty();
        assertThat(users).is(new Condition<>(isNotEmpty, "is not empty"));
    }
}
