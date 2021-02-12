package com.invivoo.vivwallet.api.infrastructure.x4b;

import com.invivoo.vivwallet.api.application.config.X4BAuthConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.invivoo.vivwallet.api.infrastructure.x4b.X4BConnectorConfiguration.X4B_CONNECTOR_REST_TEMPLATE;

@Service
public class X4BConnector {

    private final RestTemplate restTemplate;
    private final X4BAuthConfiguration x4BAuthConfiguration;

    public X4BConnector(@Qualifier(X4B_CONNECTOR_REST_TEMPLATE) RestTemplate restTemplate, X4BAuthConfiguration x4BAuthConfiguration) {
        this.restTemplate = restTemplate;
        this.x4BAuthConfiguration = x4BAuthConfiguration;
    }

    public X4BConfigurationKey getConfigurationKey() {
        ResponseEntity<X4BConfigurationKey> response = restTemplate.getForEntity(x4BAuthConfiguration.getPublicKeyUrl(), X4BConfigurationKey.class);
        if (HttpStatus.OK != response.getStatusCode()) {
            throw new SecurityException(String.format("No configuration key received from x4b at %s", x4BAuthConfiguration.getPublicKeyUrl()));
        }
        return response.getBody();
    }
}
