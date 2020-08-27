package com.invivoo.vivwallet.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
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
import java.time.LocalDateTime;
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
    private final PaymentService paymentService;

    public DatabaseInitializer(@Qualifier(LynxConnectorConfiguration.LYNX_CONNECTOR_OBJECT_MAPPER) ObjectMapper objectMapper, LynxConnector lynxConnector, ActionService actionService, UserRepository userRepository, PaymentService paymentService) {
        this.objectMapper = objectMapper;
        this.lynxConnector = lynxConnector;
        this.actionService = actionService;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
    }

    @Override
    public void run(String... args) throws IOException, URISyntaxException {
        User theophileMontgomery = User.builder()
                                       .fullName("Théophile MONTGOMERY")
                                       .id(1L)
                                       .build();
        theophileMontgomery.setRoles(Arrays.asList(
                new Role(theophileMontgomery, RoleType.EXPERTISE_MANAGER),
                new Role(theophileMontgomery, RoleType.SENIOR_MANAGER),
                new Role(theophileMontgomery, RoleType.COMPANY_ADMINISTRATOR)
        ));
        userRepository.saveAll(Arrays.asList(theophileMontgomery,
                                             User.builder()
                                                 .fullName("Collaborateur Invivoo")
                                                 .id(2L)
                                                 .build()));


        Path lynxResponse = Paths.get(Objects.requireNonNull(getClass().getResource(String.format("/%s", "lynx-dev-data.json"))).toURI());
        String lynxResponseFromFile = Files.lines(lynxResponse).collect(Collectors.joining());

        Activities activities = objectMapper.readValue(lynxResponseFromFile, Activities.class);
        List<Action> actions = lynxConnector.getActionsFromActivities(activities.getActivities());
        actionService.saveAll(actions);

        Payment payment = Payment.builder()
                                 .id(1L)
                                 .date(LocalDateTime.of(2020, 7, 29, 0, 0))
                                 .receiver(theophileMontgomery)
                                 .build();

        paymentService.save(payment);
        List<Action> paidActions = actions.subList(0, actions.size() / 2)
                                          .stream()
                                          .peek(action -> action.setPayment(payment))
                                          .collect(Collectors.toList());
        actionService.saveAll(paidActions);

    }

}
