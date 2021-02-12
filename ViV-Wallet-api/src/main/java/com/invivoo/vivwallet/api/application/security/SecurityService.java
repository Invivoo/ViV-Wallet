package com.invivoo.vivwallet.api.application.security;

import com.invivoo.vivwallet.api.domain.expertise.Expertise;
import com.invivoo.vivwallet.api.domain.expertise.UserExpertise;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.invivoo.vivwallet.api.domain.role.RoleType.COMPANY_ADMINISTRATOR;
import static com.invivoo.vivwallet.api.domain.role.RoleType.SENIOR_MANAGER;

@Service
public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getConnectedUser() {
        return userService.findById(Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    public boolean connectedUserHasAnyRole(RoleType... roleTypes) {
        Set<RoleType> types = Set.of(roleTypes);
        return SecurityContextHolder.getContext()
                                    .getAuthentication()
                                    .getAuthorities()
                                    .stream()
                                    .filter(RoleGrantedAuthority.class::isInstance)
                                    .map(RoleGrantedAuthority.class::cast)
                                    .map(RoleGrantedAuthority::getRoleType)
                                    .anyMatch(types::contains);
    }

    public List<Expertise> getAllowedExpertiseForConnectedUser() {
        if (connectedUserHasAnyRole(SENIOR_MANAGER, COMPANY_ADMINISTRATOR)) {
            return List.of(Expertise.values());
        }
        return getConnectedUser().map(User::getExpertises)
                                 .map(expertises -> expertises.stream()
                                                              .map(UserExpertise::getExpertise)
                                                              .collect(Collectors.toList()))
                                 .orElse(List.of());
    }
}
