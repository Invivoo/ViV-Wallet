package com.invivoo.vivwallet.api.interfaces.authorizations;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.security.JWTTokenProvider;
import com.invivoo.vivwallet.api.domain.role.Role;
import com.invivoo.vivwallet.api.domain.role.RoleType;
import com.invivoo.vivwallet.api.domain.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(AuthorizationsController.API_V_1_AUTH)
public class AuthorizationsController {

    public static final String API_V_1_AUTH = "/api/Authorizations";
    public static final String HTTP_SCHEMAS_XMLSOAP_ORG_WS_2005_05_IDENTITY_CLAIMS_EMAILADDRESS = "http://schemas.xmlsoap"
                                                                                                  + ".org/ws/2005/05/identity/claims"
                                                                                                  + "/emailaddress";

    private final JWTTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthorizationsController(JWTTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<AuthorizationsResponse> authorize(HttpServletRequest httpServletRequest) throws InvalidKeySpecException,
                                                                                                          NoSuchAlgorithmException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        DecodedJWT verify = jwtTokenProvider.verify(token);
        Optional<String> x4bId = Optional.ofNullable(verify.getClaim("user")).map(Claim::asString);
        Optional<String> emailFromClaims = Optional.ofNullable(verify.getClaim(
                HTTP_SCHEMAS_XMLSOAP_ORG_WS_2005_05_IDENTITY_CLAIMS_EMAILADDRESS)).map(Claim::asString);
        return emailFromClaims.flatMap(userService::findByEmail)
                              .or(() -> x4bId.flatMap(userService::findByX4bIdOrByFullName))
                              .map(user -> AuthorizationsResponse.builder()
                                                                 .userId(user.getId())
                                                                 .roles(getRolesAsString(user.getRoles()))
                                                                 .build())
                              .map(ResponseEntity::ok)
                              .orElseGet(ResponseEntity.notFound()::build);
    }

    private List<String> getRolesAsString(Set<Role> roles) {
        return Optional.ofNullable(roles)
                       .orElse(Set.of())
                       .stream()
                       .map(Role::getType)
                       .map(RoleType::name)
                       .collect(Collectors.toList());
    }

}
