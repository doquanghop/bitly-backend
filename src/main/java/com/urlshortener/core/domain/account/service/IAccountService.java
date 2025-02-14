package com.urlshortener.core.domain.account.service;

import com.urlshortener.core.domain.account.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.infrastucture.security.UserDetail;
import com.urlshortener.core.domain.account.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.account.dataTransferObject.response.AccountResponse;

public interface IAccountService {
    AccountResponse register(RegisterRequest request);

    AccountResponse login(LoginRequest request);

    AccountResponse refreshToken(RefreshTokenRequest request);

    AccountResponse changePassword(ChangePasswordRequest request);

    void logout(String accessToken);

    UserDetail authenticate(String accessToken);
    UserDetail getCurrentUser();
}
