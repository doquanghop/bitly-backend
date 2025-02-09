package com.urlshortener.core.domain.auth.component;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.auth.exception.AuthException;
import com.urlshortener.core.infrastucture.exception.AppException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.secretKeyAccess}")
    private String secretKey;

    private static final String CLAIM_USER_ID = "user_id";

    public TokenDTO generateAccessToken(TokenMetadataDTO tokenMetadata) {
        Date issuedAt = tokenMetadata.getIssuedAt();
        Date validity = new Date(issuedAt.getTime() + accessTokenExpiration * 1000);

        JWTClaimsSet claimsSet =  new JWTClaimsSet.Builder()
                .subject(tokenMetadata.getUserName())
                .issuer("auth-service")
                .issueTime(tokenMetadata.getIssuedAt())
                .expirationTime(validity)
                .jwtID(UUID.randomUUID().toString())
                .claim(CLAIM_USER_ID, tokenMetadata.getUserId())
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
        return new TokenDTO(
                accessToken,
                validity
        );
    }

    public boolean verificationToken(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);

            MACVerifier verifier = new MACVerifier(secretKey);
            if (!signedJWT.verify(verifier)) {
                throw new AppException(AuthException.TOKEN_SIGNATURE_INVALID);
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new AppException(AuthException.TOKEN_EXPIRED);
            }

            return true;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        }
    }
}
