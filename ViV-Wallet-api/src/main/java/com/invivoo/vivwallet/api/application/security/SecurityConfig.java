package com.invivoo.vivwallet.api.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.authorizations.AuthorizationsController;
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

    private final JWTTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public SecurityConfig(JWTTokenProvider jwtTokenProvider, ObjectMapper objectMapper, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.userService = userService;
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
        http.addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, objectMapper, userService), UsernamePasswordAuthenticationFilter.class);

    }
}
