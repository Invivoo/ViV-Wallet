package com.invivoo.vivwallet.api.interfaces.authorizations;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AuthorizationsController.API_V_1_AUTH)
public class AuthorizationsController {

    public static final String API_V_1_AUTH = "/api/Authorizations";

    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthorizationsController(JWTTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthorizationsResponse> authorize(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        DecodedJWT verify = jwtTokenProvider.verify(token);
        String x4bId = verify.getClaim("user").asString();
        User user = userService.findByX4bIdOrCreateIfNotExists(x4bId);
        return ResponseEntity.ok(AuthorizationsResponse.builder()
                                                       .userId(user.getId())
                                                       .roles(getRolesAsString(user.getRoles()))
                                                       .build());
    }

    private List<String> getRolesAsString(List<Role> roles) {
        return Optional.ofNullable(roles)
                       .orElse(List.of())
                       .stream()
                       .map(Role::getType)
                       .map(RoleType::name)
                       .collect(Collectors.toList());
    }

}
