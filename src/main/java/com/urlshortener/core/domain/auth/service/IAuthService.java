package com.urlshortener.core.domain.auth.service;

import com.urlshortener.core.domain.auth.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.infrastucture.security.UserDetail;
import com.urlshortener.core.domain.auth.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.response.AccountResponse;

public interface IAuthService {
    AccountResponse register(RegisterRequest request);

    AccountResponse login(LoginRequest request);

    AccountResponse refreshToken(RefreshTokenRequest request);

    AccountResponse changePassword(ChangePasswordRequest request);

    void logout(String accessToken);

    UserDetail authenticate(String accessToken);
    UserDetail getCurrentUser();
}
