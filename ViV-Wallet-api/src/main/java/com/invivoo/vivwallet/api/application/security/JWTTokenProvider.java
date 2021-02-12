package com.invivoo.vivwallet.api.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.invivoo.vivwallet.api.application.config.X4BAuthConfiguration;
import com.invivoo.vivwallet.api.infrastructure.x4b.X4BConnector;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Service
public class JWTTokenProvider {

    public static final String BEARER_ = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";

    private final X4BAuthConfiguration configuration;
    private final X4BConnector x4BConnector;
    private Algorithm jwtVerificationAlgorithm;

    public JWTTokenProvider(X4BAuthConfiguration configuration, X4BConnector x4BConnector) {
        this.configuration = configuration;
        this.x4BConnector = x4BConnector;
    }

    public String resolveToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith(BEARER_)) {
            return null;
        }
        return bearerToken.substring(BEARER_.length());
    }

    public DecodedJWT verify(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Algorithm jwtVerifierAlgorithm = getJwtVerificationAlgorithm();
        JWTVerifier verifier = JWT.require(jwtVerifierAlgorithm)
                                  .withIssuer(configuration.getIssuer())
                                  .build();
        return verifier.verify(token);
    }

    private Algorithm getJwtVerificationAlgorithm() throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (jwtVerificationAlgorithm != null) {
            return jwtVerificationAlgorithm;
        }
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                                                             .generatePublic(getPublicKeySpec());
        RSAKeyProvider rsaKeyProvider = providerForKey(rsaPublicKey);
        jwtVerificationAlgorithm = Algorithm.RSA256(rsaKeyProvider);
        return jwtVerificationAlgorithm;
    }

    private X509EncodedKeySpec getPublicKeySpec() {
        String publicKey = x4BConnector.getConfigurationKey().getKey().replace("-----BEGIN PUBLIC KEY-----\n", "").replace("-----END PUBLIC KEY-----\n", "");
        byte[] encodedPublicKey = Base64.decodeBase64(publicKey);
        return new X509EncodedKeySpec(encodedPublicKey);
    }

    static RSAKeyProvider providerForKey(final RSAPublicKey publicKey) {
        if (publicKey == null) {
            throw new IllegalArgumentException("publicKey cannot be null.");
        } else {
            return new RSAKeyProvider() {
                public RSAPublicKey getPublicKeyById(String keyId) {
                    return publicKey;
                }

                public RSAPrivateKey getPrivateKey() {
                    return null;
                }

                public String getPrivateKeyId() {
                    return null;
                }
            };
        }
    }

}
