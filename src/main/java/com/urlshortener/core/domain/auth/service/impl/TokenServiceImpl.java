package com.urlshortener.core.domain.auth.service.impl;

import com.urlshortener.core.domain.auth.component.JwtTokenProvider;
import com.urlshortener.core.domain.auth.dataTransferObject.RefreshTokenDTO;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.auth.model.Token;
import com.urlshortener.core.domain.auth.model.User;
import com.urlshortener.core.domain.auth.repository.TokenRepository;
import com.urlshortener.core.domain.auth.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public Token create(User user) {
        Date issuedAt = new Date();
        var tokenMetadataDTO = new TokenMetadataDTO(
                user.getId(),
                user.getEmail(),
                issuedAt
        );
        var accessToken = jwtTokenProvider.generateAccessToken(tokenMetadataDTO);
        var refreshToken = generateRefreshToken(issuedAt);
        Token newToken = new Token(
                null,
                user.getId(),
                accessToken.getValue(),
                accessToken.getExpiry(),
                refreshToken.getValue(),
                refreshToken.getExpiry()
        );
        return tokenRepository.save(newToken);
    }

    private RefreshTokenDTO generateRefreshToken(Date issuedAt) {
        Date validity = new Date(issuedAt.getTime() + refreshTokenExpiration * 1000);
        String refreshToken = UUID.randomUUID().toString();
        return new RefreshTokenDTO(
                refreshToken,
                validity
        );
    }
}
