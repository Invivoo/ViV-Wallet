package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    public static Optional<JWTUserDetails> fromDecodedJWT(DecodedJWT decodedJWT, ObjectMapper objectMapper) {
        Optional<Map<String, Object>> vivWalletClaims = Optional.ofNullable(decodedJWT.getClaim("viv-wallet"))
                                                                .map(Claim::asString)
                                                                .map(vivWalletClaimsAsString -> getVivWalletClaims(objectMapper, vivWalletClaimsAsString));
        List<RoleGrantedAuthority> authorities = vivWalletClaims.map(claims -> claims.get("roles"))
                                                                .map(roles -> (List<String>) roles)
                                                                .orElse(List.of())
                                                                .stream()
                                                                .map(RoleType::valueOf)
                                                                .map(RoleGrantedAuthority::new)
                                                                .collect(Collectors.toList());
        return vivWalletClaims.map(claims -> claims.get("userId"))
                              .map(String::valueOf)
                              .map(userId -> new JWTUserDetails(userId, authorities) );
    }

    private static Map getVivWalletClaims(ObjectMapper objectMapper, String vivWalletClaimsAsString) {
        try {
            return objectMapper.readValue(vivWalletClaimsAsString, Map.class);
        } catch (IOException e) {
            return null;
        }
    }
}
