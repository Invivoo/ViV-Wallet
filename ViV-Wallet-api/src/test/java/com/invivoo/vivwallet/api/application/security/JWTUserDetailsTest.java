package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.function.Function;

public class JWTUserDetailsTest {

    @Test
    public void shouldHaveCorrectJWTUserDetails_whenBuildFromDecodedJWT() {
        // given
        DecodedJWT decodedJWT = Mockito.mock(DecodedJWT.class);

        Claim userClaim = mockClaim("John DOE", Claim::asString);
        Mockito.when(decodedJWT.getClaim("user")).thenReturn(userClaim);

        Claim rolesClaim = mockClaim(List.of("COMPANY_ADMINISTRATION"), claim -> claim.asList(String.class));
        Mockito.when(decodedJWT.getClaim("roles")).thenReturn(rolesClaim);

        // when
        JWTUserDetails jwtUserDetails = JWTUserDetails.fromDecodedJWT(decodedJWT);

        // then
        Assert.assertEquals(List.of(new SimpleGrantedAuthority("COMPANY_ADMINISTRATION")), jwtUserDetails.getAuthorities());

    }

    private <T> Claim mockClaim(T value, Function<Claim, T> supplier) {
        Claim roleClaim = Mockito.mock(Claim.class);
        Mockito.when(supplier.apply(roleClaim)).thenReturn(value);
        return roleClaim;
    }
}
