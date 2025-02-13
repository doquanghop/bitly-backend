package com.urlshortener.core.domain.account.service.impl;

import com.urlshortener.core.domain.account.component.JwtTokenProvider;
import com.urlshortener.core.domain.account.constant.RoleConstants;
import com.urlshortener.core.domain.account.dataTransferObject.RefreshTokenDTO;
import com.urlshortener.core.domain.account.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.account.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.account.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.account.exception.AuthException;
import com.urlshortener.core.domain.account.model.Token;
import com.urlshortener.core.domain.account.model.User;
import com.urlshortener.core.domain.account.repository.TokenRepository;
import com.urlshortener.core.domain.account.repository.UserRepository;
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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    @Value("${cache.key.account.user-detail}")
    private String userDetailCacheKey;

    @Override
    @Transactional
    public AccountResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(AuthException.EMAIL_ALREADY_EXISTS);
        }

        User newUser = new User(
                null,
                request.getFullName(),
                null,
                RoleConstants.roleUser,
                request.getEmail(),
                request.getPassword()
        );
        newUser = userRepository.save(newUser);

        Token newToken = createToken(newUser);

        return new AccountResponse(
                newUser.getId(),
                newUser.getRole(),
                newToken.getAccessToken(),
                newToken.getAccessTokenExpiry(),
                newToken.getRefreshToken(),
                newToken.getRefreshTokenExpiry()
        );
    }

    @Override
    public AccountResponse login(LoginRequest loginRequest) {
        User existingUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));

        if (!passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword())) {
            throw new AppException(AuthException.INVALID_CREDENTIALS);
        }

        Token newToken = createToken(existingUser);

        return new AccountResponse(
                existingUser.getId(),
                existingUser.getRole(),
                newToken.getAccessToken(),
                newToken.getAccessTokenExpiry(),
                newToken.getRefreshToken(),
                newToken.getRefreshTokenExpiry()
        );
    }

    @Override
    @Transactional
    public AccountResponse changePassword(ChangePasswordRequest request) {
        UserDetail currentUser = getCurrentUser();

        User existingUser = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getOldPassword(), existingUser.getPassword())) {
            throw new AppException(AuthException.PASSWORD_MISMATCH);
        }

        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        existingUser = userRepository.save(existingUser);
        Token newToken = createToken(existingUser);

        return new AccountResponse(
                existingUser.getId(),
                existingUser.getRole(),
                newToken.getAccessToken(),
                newToken.getAccessTokenExpiry(),
                newToken.getRefreshToken(),
                newToken.getRefreshTokenExpiry()
        );
    }

    @Override
    @Transactional
    public AccountResponse refreshToken(RefreshTokenRequest request) {
        checkTokenInBlacklist(request.getAccessToken());

        Token existingToken = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new AppException(AuthException.REFRESH_TOKEN_INVALID));

        if (existingToken.getRefreshTokenExpiry().before(new Date())) {
            throw new AppException(AuthException.REFRESH_TOKEN_EXPIRED);
        }

        User user = userRepository.findById(existingToken.getUserId())
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));
        tokenRepository.delete(existingToken);

        Token newToken = createToken(user);

        return new AccountResponse(
                user.getId(),
                user.getRole(),
                newToken.getAccessToken(),
                newToken.getAccessTokenExpiry(),
                newToken.getRefreshToken(),
                newToken.getRefreshTokenExpiry()
        );
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

        Optional<Token> token = tokenRepository.findByAccessToken(accessToken);
        token.ifPresent(tokenRepository::delete);
    }

    @Override
    public UserDetail authenticate(String accessToken) {
        String userName = jwtTokenProvider.extractUserName(accessToken);
        String userDetailKey = userDetailCacheKey.formatted(userName);
        UserDetail userDetailInCache = cacheService.get(userDetailKey, UserDetail.class);

        if (userDetailInCache != null) return userDetailInCache;

        User existingUser = userRepository.findByEmail(userName)
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));

        UserDetail userDetailSaving = new UserDetail(
                existingUser.getId(),
                existingUser.getRole(),
                existingUser.getEmail(),
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


    private Token createToken(User user) {
        Date issuedAt = new Date();
        var tokenMetadataDTO = new TokenMetadataDTO(
                user.getId(),
                user.getEmail(),
                issuedAt
        );
        var accessToken = jwtTokenProvider.generateAccessToken(tokenMetadataDTO);
        System.out.println(accessToken.getValue().length());
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
        return new RefreshTokenDTO(refreshToken, validity);
    }


}
