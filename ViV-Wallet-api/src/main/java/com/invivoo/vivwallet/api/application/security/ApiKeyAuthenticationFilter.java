package com.invivoo.vivwallet.api.application.security;

import org.apache.commons.lang3.StringUtils;
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

public class ApiKeyAuthenticationFilter extends GenericFilterBean {

    public static final String API_KEY_ = "ApiKey ";
    public static final String AUTHORIZATION = "Authorization";

    private final String apiKey;

    public ApiKeyAuthenticationFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String apiKey = resolveApiKey(request);

        if (StringUtils.isNotBlank(apiKey) && this.apiKey.equals(apiKey)) {
            try {
                ApiUserDetails jwtUserDetails = new ApiUserDetails();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUserDetails, null, jwtUserDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Could not set user authentication in security context", e);
            }
        }

        filterChain.doFilter(req, res);
    }

    public String resolveApiKey(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith(API_KEY_)) {
            return null;
        }
        return bearerToken.substring(API_KEY_.length());
    }
}
