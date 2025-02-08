package com.urlshortener.core.domain.auth.service.impl;

import com.urlshortener.core.domain.auth.component.JwtProvider;
import com.urlshortener.core.domain.auth.model.Token;
import com.urlshortener.core.domain.auth.model.User;
import com.urlshortener.core.domain.auth.repository.TokenRepository;
import com.urlshortener.core.domain.auth.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    public Token create(User user) {
        var accessToken = jwtProvider.generateAccessToken(user.getId());
        Token newToken = new Token(
                null,
                user.getId(),
                accessToken.getValue(),
                accessToken.getExpiry(),
                null,
                null
        );
        tokenRepository.save(newToken);
        return newToken;
    }

}
