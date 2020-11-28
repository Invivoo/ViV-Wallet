package com.invivoo.vivwallet.api.infrastructure.lynx;


import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activity;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.ActivityType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@Import(LynxConnectorConfiguration.class)
@PropertySource("classpath:application.yml")
public class LynxConnectorTest {

    @Autowired
    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;

    @Before
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void should_return_activities_when_get_from_lynx() throws IOException, URISyntaxException {
        // given
        LynxConnector lynxConnector = new LynxConnector(restTemplate, "http://vivApiUrl", null);

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
}
