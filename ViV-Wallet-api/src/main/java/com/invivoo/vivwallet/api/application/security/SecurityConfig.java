package com.invivoo.vivwallet.api.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.interfaces.authorizations.AuthorizationsController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String API = "api";
    public static final String NO_AUTH_PROFILE = "noAuth";

    private final String apiKey;
    private final Environment environment;
    private final JWTTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public SecurityConfig(@Value("${api.key}") String apiKey,
                          Environment environment,
                          JWTTokenProvider jwtTokenProvider,
                          ObjectMapper objectMapper) {
        this.apiKey = apiKey;
        this.environment = environment;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .anyRequest().permitAll()
            .antMatchers(API).authenticated()
            .antMatchers(AuthorizationsController.API_V_1_AUTH).permitAll();

        // Add our custom JWT security filter
        http.addFilterBefore(new ApiKeyAuthenticationFilter(apiKey), UsernamePasswordAuthenticationFilter.class);
        if (Arrays.asList(environment.getActiveProfiles()).contains(NO_AUTH_PROFILE)) {
            http.addFilterBefore(new NoAuthJWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);
        }
    }
}
