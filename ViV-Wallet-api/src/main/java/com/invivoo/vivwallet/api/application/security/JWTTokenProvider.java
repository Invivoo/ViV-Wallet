package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.config.X4BAuthConfiguration;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class JWTTokenProvider {

    public static final String BEARER_ = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    private final X4BAuthConfiguration configuration;

    public JWTTokenProvider(X4BAuthConfiguration configuration) {
        this.configuration = configuration;
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith(BEARER_)) {
            return null;
        }
        return bearerToken.substring(BEARER_.length());
    }

    public DecodedJWT verify(String token) {
        Algorithm algorithm = Algorithm.HMAC256(configuration.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
                                  .withIssuer(configuration.getIssuer())
                                  .build();
        return verifier.verify(token);
    }
}
