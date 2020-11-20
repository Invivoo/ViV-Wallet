package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JWTAuthenticationFilter extends GenericFilterBean {

    private final JWTTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public JWTAuthenticationFilter(JWTTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String token = jwtTokenProvider.resolveToken(request);

        if (token != null) {
            try {
                DecodedJWT decodedJWT = jwtTokenProvider.verify(token);
                JWTUserDetails jwtUserDetails = JWTUserDetails.fromDecodedJWT(decodedJWT, objectMapper);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Could not set user authentication in security context", e);
            }
        }

        filterChain.doFilter(req, res);
    }
}
