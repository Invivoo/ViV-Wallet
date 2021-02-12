package com.invivoo.vivwallet.api.application.security;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.invivoo.vivwallet.api.application.config.X4BAuthConfiguration;
import com.invivoo.vivwallet.api.infrastructure.x4b.X4BConfigurationKey;
import com.invivoo.vivwallet.api.infrastructure.x4b.X4BConnector;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class JWTTokenProviderTest {

    @Test
    public void whenVerifyJwt_thenGetDecodedJWT() throws InvalidKeySpecException, NoSuchAlgorithmException {
        //given
        X4BConnector x4BConnectorMock = Mockito.mock(X4BConnector.class);
        Mockito.when(x4BConnectorMock.getConfigurationKey()).thenReturn(new X4BConfigurationKey("-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw7mwAZcLWygi3FCbAy5J\nuNAMdGlBxCCROIJCBinYHXiSea7I9FRY3Bmof8Kw9fU04zPcwo0vZiVjAPluGVTC\n/h"
                                                                                                + "+ovIdZcYJjoZqi5wrNrEdBCJllTDhwxdshyfXVQPqmSpnffDLl8fYpqUZ7fthU\nmYmAu7QLQRQJYYnX0Fr+B5EwjV5+bOqKX+6SiXp5iF764RoiyfBnAq27BmJ0E/2n\n/Rg4Tfrv+DJPmc45okeC7SS3Q6TQlL3tcqavt2B7uXqlsC8Mkz4WYeD2qLmuKFen"
                                                                                                + "\ndqEL+xZ3qlY423LwtPk2vdrrO5bUSTkayMmDYG+TRkthZ2QjoK6t9qGO6qIRpXca\nbQIDAQAB\n-----END PUBLIC KEY-----\n", "RS256"));
        JWTTokenProvider jwtTokenProvider = new JWTTokenProvider(new X4BAuthConfiguration("X4B", "U29tZVhDb21wb25lbnRLb29yZGluYXRvclNlY3JldA=="), x4BConnectorMock);

        //when
        DecodedJWT verify = jwtTokenProvider.verify(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9"
                +
                ".eyJzY2VuYXJpbyI6IntcInJvbGVzXCI6W1wiUmVhZFwiLFwiV3JpdGVcIl0sXCJ3b3Jrc3BhY2VzXCI6W1wiQmVub2l0IEpPTkFUSEFTXCJdfSIsInZpdi13YWxsZXQiOiJ7XCJyb2xlc1wiOltcIkVYUEVSVElTRV9NQU5BR0VSXCIsXCJDT01QQU5ZX0FETUlOSVNUUkFUT1JcIixcIlNFTklPUl9NQU5BR0VSXCIsXCJDT05TVUxUQU5UXCJdLFwidXNlcklkXCI6MX0iLCJ0eXBlIjoiUmVndWxhclVzZXIiLCJpc3MiOiJYNEIiLCJ1c2VyIjoiVGjDqW9waGlsZSBNT05UR09NRVJZIiwic3JjIjoiZXh0ZXJuYWwifQ.eJLphlzJSP5Ym55ksf81wLq1DFbd51mIj-uJGavTKh--eev_-GzviXpE42n6enVBGuV_uYKlZAykPMGhs3eu6Z997Ujc5xi0tQHaIDCcld10dvmXraTbm4ESrPHoHS8qjIToSai2tae3yfu7mN-Qqn1sOOjf2BfLjJQuZQMgpngEt51cAst57nZE-JiWdNDDXgiVOI-3RH7CIIFONK-wWT4S00ZaWJnrpKhRtV2lWnnxyEYSIwBd869HkxiaQNK9qdSEsFRclWDE4SFFSyA4yOFltSJglWKYIPoJ2thrp7jEKPqDOf1KxNtGf_rTWSXylX81y9JSmuEp63hQTAIAig");

        //then
        Assert.assertEquals(verify.getClaim("user").asString(), "Théophile MONTGOMERY");
    }
}
