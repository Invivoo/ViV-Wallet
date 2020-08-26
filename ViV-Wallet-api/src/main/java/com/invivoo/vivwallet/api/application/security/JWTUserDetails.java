package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JWTUserDetails implements UserDetails {

    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public JWTUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static JWTUserDetails fromDecodedJWT(DecodedJWT decodedJWT) {
        String username = decodedJWT.getClaim("user").asString();
        List<SimpleGrantedAuthority> authorities = Optional.ofNullable(decodedJWT.getClaim("roles"))
                                                           .map(roles -> roles.asList(String.class))
                                                           .orElse(List.of())
                                                           .stream()
                                                           .map(SimpleGrantedAuthority::new)
                                                           .collect(Collectors.toList());
        return new JWTUserDetails(username, authorities);
    }
}
