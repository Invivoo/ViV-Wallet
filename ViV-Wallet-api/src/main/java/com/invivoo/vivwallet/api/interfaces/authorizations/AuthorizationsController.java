package com.invivoo.vivwallet.api.interfaces.authorizations;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.User;
import com.invivoo.vivwallet.api.domain.user.UserRepository;
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
    private final UserRepository userRepository;

    public AuthorizationsController(JWTTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<AuthorizationsResponse> authorize(HttpServletRequest httpServletRequest) {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        DecodedJWT verify = jwtTokenProvider.verify(token);
        String userName = verify.getClaim("user").asString();
        User user = userRepository.findByFullName(userName)
                .orElseGet(() -> createUser(userName));
        return ResponseEntity.ok(AuthorizationsResponse.builder()
                                                       .userId(user.getId())
                                                       .roles(getRolesAsString(user.getRoles()))
                                                       .build());
    }

    private User createUser(String userName) {
        User user = User.builder()
                        .fullName(userName)
                        .build();
        return userRepository.save(user);
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
