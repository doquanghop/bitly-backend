package com.urlshortener.core.domain.auth.component;

import com.urlshortener.core.domain.auth.dataTransferObject.TokenDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    @Value("${jwt.secretKeyAccess}")
    private String secretKeyAccess;

    public TokenDTO generateAccessToken(String userId) {
        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + accessTokenExpiration * 1000);
        Map<String, Object> claims = new HashMap<>();
        String accessToken = Jwts.builder()
                .claims(claims)
                .subject("subject")
                .expiration(expirationDate)
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
        return new TokenDTO(
                accessToken,
                expirationDate
        );
    }
    private SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyAccess);
        return Keys.hmacShaKeyFor(bytes);
    }
}
