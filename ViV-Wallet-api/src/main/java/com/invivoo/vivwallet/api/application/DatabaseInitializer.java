package com.invivoo.vivwallet.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final ObjectMapper objectMapper;
    private final LynxConnector lynxConnector;
    private final ActionService actionService;
    private final UserRepository userRepository;

    public DatabaseInitializer(@Qualifier(LynxConnectorConfiguration.LYNX_CONNECTOR_OBJECT_MAPPER) ObjectMapper objectMapper, LynxConnector lynxConnector, ActionService actionService, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.lynxConnector = lynxConnector;
        this.actionService = actionService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws IOException, URISyntaxException {
        userRepository.saveAll(Arrays.asList(User.builder()
                                                 .fullName("Collaborateur Invivoo")
                                                 .id(1L)
                                                 .build(),
                                             User.builder()
                                                 .fullName("Collaborateur Invivoo")
                                                 .id(1L)
                                                 .build()));


        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getResource(String.format("/%s","lynx-dev-data.json"))).toURI());
        String lynxResponseFromFile = Files.lines(lynxResponse).collect(Collectors.joining());

        Activities activities = objectMapper.readValue(lynxResponseFromFile, Activities.class);
        List<Action> actionsFromActivities = lynxConnector.getActionsFromActivities(activities.getActivities());
        actionService.saveAll(actionsFromActivities);

    }

}
