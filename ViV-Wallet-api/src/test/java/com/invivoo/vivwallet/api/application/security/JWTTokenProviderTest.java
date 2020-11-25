package com.invivoo.vivwallet.api.application.security;


import com.auth0.jwt.exceptions.TokenExpiredException;
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

    @Test(expected = TokenExpiredException.class)
    public void whenVerifyJwt_thenGetDecodedJWT() throws InvalidKeySpecException, NoSuchAlgorithmException {
        //given
        X4BConnector x4BConnectorMock = Mockito.mock(X4BConnector.class);
        Mockito.when(x4BConnectorMock.getConfigurationKey()).thenReturn(new X4BConfigurationKey("-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw7mwAZcLWygi3FCbAy5J\nuNAMdGlBxCCROIJCBinYHXiSea7I9FRY3Bmof8Kw9fU04zPcwo0vZiVjAPluGVTC\n/h"
                                                                                                + "+ovIdZcYJjoZqi5wrNrEdBCJllTDhwxdshyfXVQPqmSpnffDLl8fYpqUZ7fthU\nmYmAu7QLQRQJYYnX0Fr+B5EwjV5+bOqKX+6SiXp5iF764RoiyfBnAq27BmJ0E/2n\n/Rg4Tfrv+DJPmc45okeC7SS3Q6TQlL3tcqavt2B7uXqlsC8Mkz4WYeD2qLmuKFen"
                                                                                                + "\ndqEL+xZ3qlY423LwtPk2vdrrO5bUSTkayMmDYG+TRkthZ2QjoK6t9qGO6qIRpXca\nbQIDAQAB\n-----END PUBLIC KEY-----\n", "RS256"));
        JWTTokenProvider jwtTokenProvider = new JWTTokenProvider(new X4BAuthConfiguration("X4B", "U29tZVhDb21wb25lbnRLb29yZGluYXRvclNlY3JldA=="), x4BConnectorMock);

        //when
        DecodedJWT verify = jwtTokenProvider.verify(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzY2VuYXJpbyI6IntcInJvbGVzXCI6W1wiUmVhZFwiLFwiV3JpdGVcIl0sXCJ3b3Jrc3BhY2VzXCI6W1wiQmVub2l0IEpPTkFUSEFTXCJdfSIsInR5cGUiOiJSZWd1bGFyVXNlciIsImlzcyI6Ilg0QiIsInVzZXIiOiJCZW5vaXQgSk9OQVRIQVMiLCJzcmMiOiJleHRlcm5hbCIsImV4cCI6MTYwNjMyNjA5MS4wfQ"
                + ".iP44Cfabnv-j9cdjQ2J7JjpjFTvqjc2RX90tiFDyk0p2Jmr9eojYswMP2v2_CwP3qVP3rKzO-3hxI3ibJH2p_Omu1u-n-FnkhSq0lJvQEbSi38Y7CuPYaNK-S85Iq4J9p6bOf368O_dP9rn5NQotRj4kWdZhlUJA-U4KNd15Oyv2fiHXPiLeTIrslUsTzbRJdvw8Du_vRBfebiqbm"
                + "-Ua_8lfOyh9bFOxgdYlAoZ8ZcyZqMVMT5WMiE9yav3_lyZCvBRZ1mVgqTD8uszAjByJzECgqNzHvAkn9bWCJHnl_zDi0zo1T3ydrhwR6rqU66FXYg2uJ8beqtXfFUKV-aYlJA");

        //then
        Assert.assertEquals(verify.getClaim("user").asString(), "Benoit JONATHAS");
    }
}
