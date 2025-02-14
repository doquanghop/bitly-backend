package com.urlshortener.core.domain.account.service.impl;

import com.urlshortener.core.domain.account.component.JwtTokenProvider;
import com.urlshortener.core.domain.account.constant.RoleConstants;
import com.urlshortener.core.domain.account.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.account.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.account.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.account.exception.AuthException;
import com.urlshortener.core.domain.account.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.account.model.Account;
import com.urlshortener.core.domain.account.repository.AccountRepository;
import com.urlshortener.core.domain.account.service.IAccountService;
import com.urlshortener.core.infrastucture.exception.AppException;
import com.urlshortener.core.infrastucture.security.UserDetail;
import com.urlshortener.core.infrastucture.service.cache.CacheService;
import com.urlshortener.core.infrastucture.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;
    private final AccountRepository accountRepository;

    @Value("${cache.key.account.user-detail}")
    private String userDetailCacheKey;

    @Override
    @Transactional
    public AccountResponse register(RegisterRequest request) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(AuthException.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Account newAccount = new Account(
                null,
                request.getFullName(),
                null,
                RoleConstants.roleUser,
                request.getEmail(),
                encodedPassword
        );
        newAccount = accountRepository.save(newAccount);

        TokenDTO newTokenDTO = createToken(newAccount);

        return AccountResponse.from(newAccount, newTokenDTO);
    }

    @Override
    public AccountResponse login(LoginRequest loginRequest) {
        Account existingAccount = accountRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), existingAccount.getPassword())) {
            throw new AppException(AuthException.INVALID_CREDENTIALS);
        }

        TokenDTO newTokenDTO = createToken(existingAccount);

        return AccountResponse.from(existingAccount, newTokenDTO);
    }

    @Override
    @Transactional
    public AccountResponse changePassword(ChangePasswordRequest request) {
        UserDetail currentUser = getCurrentUser();

        Account existingAccount = accountRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getOldPassword(), existingAccount.getPassword())) {
            throw new AppException(AuthException.PASSWORD_MISMATCH);
        }

        existingAccount.setPassword(passwordEncoder.encode(request.getNewPassword()));
        existingAccount = accountRepository.save(existingAccount);
        TokenDTO newTokenDTO = createToken(existingAccount);

        return AccountResponse.from(existingAccount, newTokenDTO);
    }

    @Override
    @Transactional
    public AccountResponse refreshToken(RefreshTokenRequest request) {
        checkTokenInBlacklist(request.getAccessToken());
        jwtTokenProvider.checkRefreshTokenValid(request.getRefreshToken());
        String userName = jwtTokenProvider.extractUserNameWithoutExpirationCheck(request.getAccessToken());
        Account existingAccount = accountRepository.findByEmail(userName)
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));
        TokenDTO newTokenDTO = createToken(existingAccount);
        return AccountResponse.from(existingAccount, newTokenDTO);
    }

    private void checkTokenInBlacklist(String accessToken) {
        if (cacheService.isBlacklisted(accessToken)) {
            throw new AppException(AuthException.TOKEN_VERIFICATION_FAILED);
        }
    }

    @Override
    @Transactional
    public void logout(String accessToken) {
        Date tokenExpiryDate = jwtTokenProvider.extractExpiration(accessToken);
        checkTokenInBlacklist(accessToken);
        long accessTokenRemainingTimeExpiration = TimeUtils.calculateRemainingTime(new Date(), tokenExpiryDate);
        cacheService.addToBlacklist(accessToken, accessTokenRemainingTimeExpiration);
    }

    @Override
    public UserDetail authenticate(String accessToken) {
        String userName = jwtTokenProvider.extractUserName(accessToken);
        String userDetailKey = userDetailCacheKey.formatted(userName);
        UserDetail userDetailInCache = cacheService.get(userDetailKey, UserDetail.class);

        if (userDetailInCache != null) return userDetailInCache;

        Account existingAccount = accountRepository.findByEmail(userName)
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));

        UserDetail userDetailSaving = new UserDetail(
                existingAccount.getId(),
                existingAccount.getRole(),
                existingAccount.getEmail(),
                null
        );
        cacheService.put(userDetailKey, userDetailSaving);
        return userDetailSaving;
    }

    @Override
    public UserDetail getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(AuthException.UNAUTHORIZED_ACCESS);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetail) {
            return (UserDetail) principal;
        }

        throw new AppException(AuthException.USER_NOT_FOUND);
    }


    private TokenDTO createToken(Account account) {
        Date issuedAt = new Date();
        var tokenMetadataDTO = new TokenMetadataDTO(
                account.getId(),
                account.getEmail(),
                issuedAt
        );
        var accessToken = jwtTokenProvider.generateAccessToken(tokenMetadataDTO);
        var refreshToken = jwtTokenProvider.generateRefreshToken(issuedAt);
        return new TokenDTO(
                account.getId(),
                accessToken.value(),
                accessToken.expiry(),
                refreshToken.value(),
                refreshToken.expiry()
        );
    }
}
