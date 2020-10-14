package com.invivoo.vivwallet.api.application.security;

import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final UserService userService;

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> getConnectedUser() {
        return userService.findByX4bId(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
