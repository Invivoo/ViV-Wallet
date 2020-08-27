package com.invivoo.vivwallet.api.application.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(value = "x4b.auth")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class X4BAuthConfiguration {

    private String issuer;
    private String secret;
}
