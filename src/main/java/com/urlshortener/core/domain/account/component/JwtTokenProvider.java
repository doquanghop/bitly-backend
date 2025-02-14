package com.urlshortener.core.domain.account.component;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.urlshortener.core.domain.account.dataTransferObject.RefreshTokenDTO;
import com.urlshortener.core.domain.account.dataTransferObject.AccessTokenDTO;
import com.urlshortener.core.domain.account.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.account.exception.AuthException;
import com.urlshortener.core.infrastucture.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.secretKeyAccess}")
    private String secretKey;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private static final String CLAIM_USER_ID = "user_id";

    public AccessTokenDTO generateAccessToken(TokenMetadataDTO tokenMetadata) {
        Date issuedAt = tokenMetadata.issuedAt();
        Date validity = new Date(issuedAt.getTime() + accessTokenExpiration * 1000);

        var claimsSet = new JWTClaimsSet.Builder()
                .subject(tokenMetadata.userName())
                .issuer("auth-service")
                .issueTime(tokenMetadata.issuedAt())
                .expirationTime(validity)
                .jwtID(UUID.randomUUID().toString())
                .claim(CLAIM_USER_ID, tokenMetadata.userId())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        String accessToken = jwsObject.serialize();
        return new AccessTokenDTO(
                accessToken,
                validity
        );
    }

    public RefreshTokenDTO generateRefreshToken(Date issuedAt) {
        Date validity = new Date(issuedAt.getTime() + refreshTokenExpiration * 1000);

        var claimsSet = new JWTClaimsSet.Builder()
                .issuer("auth-service")
                .issueTime(issuedAt)
                .expirationTime(validity)
                .jwtID(UUID.randomUUID().toString())
                .build();

        JWSObject jwsObject = new JWSObject(
                new JWSHeader(JWSAlgorithm.HS256),
                new Payload(claimsSet.toJSONObject())
        );

        try {
            jwsObject.sign(new MACSigner(secretKey));
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        String refreshToken = jwsObject.serialize();
        return new RefreshTokenDTO(
                refreshToken,
                validity
        );
    }
    public String extractUserNameWithoutExpirationCheck(String accessToken) {
        try {
            SignedJWT signedJWT = parseToken(accessToken);
            verifySignature(signedJWT);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        } catch (JOSEException e) {
            throw new AppException(AuthException.TOKEN_SIGNATURE_INVALID);
        }
    }
    public void checkRefreshTokenValid(String refreshToken) {
        try {
            SignedJWT signedJWT = parseToken(refreshToken);
            verifySignature(signedJWT);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            checkExpiration(jwtClaimsSet);
        } catch (ParseException e) {
            throw new AppException(AuthException.REFRESH_TOKEN_INVALID);
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(AuthException.REFRESH_TOKEN_VERIFICATION_FAILED);
        }
    }
    public boolean verificationToken(String accessToken) {
        try {
            SignedJWT signedJWT = parseToken(accessToken);

            verifySignature(signedJWT);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            checkExpiration(jwtClaimsSet);

            return true;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        }
    }

    public Date extractExpiration(String accessToken) {
        try {
            SignedJWT signedJWT = parseToken(accessToken);
            verifySignature(signedJWT);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            checkExpiration(jwtClaimsSet);
            return jwtClaimsSet.getExpirationTime();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        }
    }

    public String extractUserName(String accessToken) {
        try {
            SignedJWT signedJWT = parseToken(accessToken);
            verifySignature(signedJWT);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            checkExpiration(jwtClaimsSet);
            return jwtClaimsSet.getSubject();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        }
    }

    private SignedJWT parseToken(String accessToken) throws ParseException {
        return SignedJWT.parse(accessToken);
    }

    private void verifySignature(SignedJWT signedJWT) throws JOSEException {
        MACVerifier verifier = new MACVerifier(secretKey);
        if (!signedJWT.verify(verifier)) {
            throw new AppException(AuthException.TOKEN_SIGNATURE_INVALID);
        }
    }

    private void checkExpiration(JWTClaimsSet jwtClaimsSet) {
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        if (expirationTime != null && expirationTime.before(new Date())) {
            throw new AppException(AuthException.TOKEN_EXPIRED);
        }
    }
}
