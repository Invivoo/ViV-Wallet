package com.invivoo.vivwallet.api.application.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.user.UserService;
import com.invivoo.vivwallet.api.interfaces.authorizations.AuthorizationsController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String API = "/api/**";

    private final String apiKey;
    private final Environment environment;
    private final JWTTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    public SecurityConfig(@Value("${api.key}") String apiKey,
                          Environment environment,
                          JWTTokenProvider jwtTokenProvider,
                          ObjectMapper objectMapper,
                          @Lazy UserService userService) {
        this.apiKey = apiKey;
        this.environment = environment;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors()
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests().anyRequest().permitAll()
            .antMatchers(AuthorizationsController.API_V_1_AUTH).permitAll()
            .antMatchers(HttpMethod.HEAD, API).authenticated()
            .antMatchers(HttpMethod.GET, API).authenticated()
            .antMatchers(HttpMethod.POST, API).authenticated()
            .antMatchers(HttpMethod.PUT, API).authenticated()
            .antMatchers(HttpMethod.DELETE, API).authenticated();

        // Add our custom JWT security filter
        http.addFilterBefore(new ApiKeyAuthenticationFilter(apiKey), UsernamePasswordAuthenticationFilter.class);
        if (isNoAuthProfile()) {
            http.addFilterBefore(new NoAuthJWTAuthenticationFilter(userService), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.addFilterBefore(new JWTAuthenticationFilter(jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://vivwallet.uat.invivoo.com", "https://vivwallet.invivoo.com"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private boolean isNoAuthProfile() {
        return Arrays.asList(environment.getActiveProfiles()).contains(NoAuthJWTAuthenticationFilter.NO_AUTH_PROFILE);
    }

}
