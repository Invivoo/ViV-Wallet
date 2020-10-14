package com.invivoo.vivwallet.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
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
    public void run(String... args) throws Exception {
        User theophileMontgomery = User.builder()
                                       .fullName("Théophile MONTGOMERY")
                                       .id(1L)
                                       .expertises(List.of(UserExpertise.builder()
                                                                        .expertise(Expertise.PROGRAMMATION_JAVA)
                                                                        .startDate(LocalDate.of(2017, 6, 1))
                                                                        .build()))
                                       .build();
        theophileMontgomery.setRoles(List.of(
                new Role(theophileMontgomery, RoleType.EXPERTISE_MANAGER),
                new Role(theophileMontgomery, RoleType.SENIOR_MANAGER),
                new Role(theophileMontgomery, RoleType.COMPANY_ADMINISTRATOR)
        ));
        userRepository.saveAll(Arrays.asList(theophileMontgomery,
                                             User.builder()
                                                 .fullName("Collaborateur Invivoo")
                                                 .id(2L)
                                                 .build()));

        String lynxResponse = readFromInputStream(getClass().getResourceAsStream(String.format("/%s", "lynx-dev-data.json")));
        Activities activities = objectMapper.readValue(lynxResponse, Activities.class);
        List<Action> actions = lynxConnector.getActionsFromActivities(activities.getActivities());
        actionService.saveAll(actions);

        Payment payment = Payment.builder()
                                 .id(1L)
                                 .date(LocalDate.of(2020, 7, 29))
                                 .receiver(theophileMontgomery)
                                 .build();

        paymentService.save(payment);
        List<Action> paidActions = actions.subList(0, actions.size() / 2)
                                          .stream()
                                          .peek(action -> action.setPayment(payment))
                                          .collect(Collectors.toList());
        actionService.saveAll(paidActions);

    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

}
