package com.invivoo.vivwallet.api.application.security;

import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoAuthJWTAuthenticationFilter extends GenericFilterBean {

    public static final String NO_AUTH_PROFILE = "noAuth";
    public static final User NO_AUTH_USER = User.builder()
                                                .fullName("No Auth user")
                                                .x4bId("No Auth user")
                                                .roles(Stream.of(RoleType.values())
                                                             .map(roleType -> Role.builder().type(roleType).build())
                                                             .collect(Collectors.toSet()))
                                                .build();

    private final UserService userService;

    public NoAuthJWTAuthenticationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        User persistedNoAuthUser =
                userService.findByX4bIdOrByFullName(NO_AUTH_USER.getFullName())
                           .orElseThrow(() -> new RuntimeException("NO_AUTH_USER should have been initialized by DatabaseInitializer"));
        List<RoleGrantedAuthority> allRoles = persistedNoAuthUser.getRoles()
                                                                 .stream()
                                                                 .map(Role::getType)
                                                                 .map(RoleGrantedAuthority::new)
                                                                 .collect(Collectors.toList());
        JWTUserDetails jwtUserDetails = new JWTUserDetails(persistedNoAuthUser.getId().toString(), allRoles);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(jwtUserDetails,
                                                                                                     null,
                                                                                                     jwtUserDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(req, res);
    }


}
