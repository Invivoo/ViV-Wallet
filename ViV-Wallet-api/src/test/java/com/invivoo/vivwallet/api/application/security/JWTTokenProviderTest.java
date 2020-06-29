package com.invivoo.vivwallet.api.application.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.config.X4BAuthConfiguration;
import org.junit.Assert;
import org.junit.Test;

public class JWTTokenProviderTest {

    private JWTTokenProvider jwtTokenProvider = new JWTTokenProvider(new X4BAuthConfiguration("X4B", "U29tZVhDb21wb25lbnRLb29yZGluYXRvclNlY3JldA=="));

    @Test
    public void whenVerifyJwt_thenGetDecodedJWT() {
        DecodedJWT verify = jwtTokenProvider.verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzY2VuYXJpbyI6IntcInJvbGVzXCI6W1wiQWRtaW5cIixcIlJlYWRcIixcIldyaXRlXCJdLFwid29ya3NwYWNlc1wiOltcIkF1eGlsYXJ5V29ya3NwYWNlXCIsXCJCZW5jaG1hcmtcIixcIkRlZmF1bHRXb3Jrc3BhY2VcIixcIlRFTVBcIl19IiwidHlwZSI6IlJlZ3VsYXJVc2VyIiwiaXNzIjoiWDRCIiwidXNlciI6ImFkbWluIiwic3JjIjoiZGF0YWJhc2UifQ.-9SBinNhBHaDhd2qv2ZI6i2IWqTl6dHq0J1zPPsfkOE");
        Assert.assertEquals(verify.getClaim("user").asString(), "admin");
    }
}
