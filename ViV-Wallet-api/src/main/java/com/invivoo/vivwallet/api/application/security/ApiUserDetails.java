package com.invivoo.vivwallet.api.application.security;

import com.invivoo.vivwallet.api.domain.role.RoleType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class ApiUserDetails implements UserDetails {

    public static final String API_USER = "ApiUser";
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;

    public ApiUserDetails() {
        this.username = API_USER;
        this.authorities = List.of(new SimpleGrantedAuthority(RoleType.API_USER.name()));
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

}
