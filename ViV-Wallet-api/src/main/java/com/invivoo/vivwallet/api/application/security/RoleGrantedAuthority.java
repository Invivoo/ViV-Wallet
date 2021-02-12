package com.invivoo.vivwallet.api.application.security;

import com.invivoo.vivwallet.api.domain.role.RoleType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@RequiredArgsConstructor
public class RoleGrantedAuthority implements GrantedAuthority {

    private final RoleType roleType;

    @Override
    public String getAuthority() {
        return roleType.name();
    }
}
