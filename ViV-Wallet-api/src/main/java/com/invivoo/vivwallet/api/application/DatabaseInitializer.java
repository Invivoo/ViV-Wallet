package com.invivoo.vivwallet.api.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.application.security.NoAuthJWTAuthenticationFilter;
import com.invivoo.vivwallet.api.domain.action.Action;
import com.invivoo.vivwallet.api.domain.action.ActionService;
import com.invivoo.vivwallet.api.domain.action.ActionStatus;
import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.payment.Payment;
import com.invivoo.vivwallet.api.domain.payment.PaymentService;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnector;
import com.invivoo.vivwallet.api.infrastructure.lynx.LynxConnectorConfiguration;
import com.invivoo.vivwallet.api.infrastructure.lynx.model.Activities;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final Environment env;
    private final ObjectMapper objectMapper;
    private final LynxConnector lynxConnector;
    private final ActionService actionService;
    private final UserService userService;
    private final PaymentService paymentService;

    public DatabaseInitializer(Environment env, @Qualifier(LynxConnectorConfiguration.LYNX_CONNECTOR_OBJECT_MAPPER) ObjectMapper objectMapper, LynxConnector lynxConnector, ActionService actionService, UserService userService, PaymentService paymentService) {
        this.env = env;
        this.objectMapper = objectMapper;
        this.lynxConnector = lynxConnector;
        this.actionService = actionService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (isInMemoryDatabase()) {
            initInMemoryDatabaseData();
        }
        if (Arrays.asList(env.getActiveProfiles()).contains(NoAuthJWTAuthenticationFilter.NO_AUTH_PROFILE)) {
            initNoAuthUser();
        }

    }

    private void initInMemoryDatabaseData() throws IOException {
        User theophileMontgomery = User.builder()
                                       .fullName("Théophile MONTGOMERY")
                                       .id(1L)
                                       .expertises(Set.of(UserExpertise.builder()
                                                                       .expertise(Expertise.PROGRAMMATION_JAVA)
                                                                       .startDate(LocalDate.of(2017, 6, 1))
                                                                       .build()))
                                       .roles(Set.of(
                                               new Role(RoleType.EXPERTISE_MANAGER),
                                               new Role(RoleType.SENIOR_MANAGER),
                                               new Role(RoleType.COMPANY_ADMINISTRATOR)
                                       ))
                                       .build();
        userService.saveAll(Arrays.asList(theophileMontgomery,
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
        List<Action> payableActions = actions.subList(0, actions.size() / 2)
                                             .stream()
                                             .peek(action -> action.setStatus(ActionStatus.PAYABLE))
                                             .collect(Collectors.toList());
        actionService.saveAll(payableActions);
    }

    private void initNoAuthUser() {
        if (userService.findByX4bIdOrByFullName(NoAuthJWTAuthenticationFilter.NO_AUTH_USER.getFullName()).isEmpty()) {
            userService.save(NoAuthJWTAuthenticationFilter.NO_AUTH_USER);
        }
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

    private boolean isInMemoryDatabase() {
        return "jdbc:h2:mem:testdb".equals(env.getProperty("spring.datasource.url"));
    }

}
