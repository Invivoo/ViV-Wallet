package com.invivoo.vivwallet.api.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.interfaces.authorizations.AuthorizationsController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String API = "api";

    private final String apiKey;
    private final JWTTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public SecurityConfig(@Value("${api.key}") String apiKey, JWTTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.apiKey = apiKey;
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
        http.addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);

    }
}
