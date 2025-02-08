package com.urlshortener.core.domain.auth.service.impl;

import com.urlshortener.core.domain.auth.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.auth.model.Token;
import com.urlshortener.core.domain.auth.model.User;
import com.urlshortener.core.domain.auth.repository.UserRepository;
import com.urlshortener.core.domain.auth.service.IAuthService;
import com.urlshortener.core.domain.auth.service.ITokenService;
import com.urlshortener.core.infrastucture.security.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {
    private final ITokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public AccountResponse register(RegisterRequest request) {
        User newUser = new User();
        userRepository.save(newUser);
        Token newToken = tokenService.create(newUser);
        return null;
    }

    @Override
    public AccountResponse login(LoginRequest loginRequest) {
        User existingUser;
        return null;
    }

    @Override
    public void logout(String accessToken) {

    }

    @Override
    public UserDetail authenticate(String accessToken) {
        return null;
    }

}
