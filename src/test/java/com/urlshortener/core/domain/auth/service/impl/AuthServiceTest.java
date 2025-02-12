package com.urlshortener.core.domain.auth.service.impl;

import com.urlshortener.core.domain.auth.component.JwtTokenProvider;
import com.urlshortener.core.domain.auth.constant.RoleConstants;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.auth.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.auth.model.Token;
import com.urlshortener.core.domain.auth.model.User;
import com.urlshortener.core.domain.auth.repository.TokenRepository;
import com.urlshortener.core.domain.auth.repository.UserRepository;
import com.urlshortener.core.infrastucture.exception.AppException;
import com.urlshortener.core.infrastucture.security.UserDetail;
import com.urlshortener.core.infrastucture.service.cache.CacheService;
import com.urlshortener.core.infrastucture.utils.TimeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CacheService cacheService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthServiceImpl authService;

    private TokenDTO tokenDTO;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(authService, "refreshTokenExpiration", 3600L);
        ReflectionTestUtils.setField(authService, "userDetailCacheKey", "user:%s");
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        User user = new User("user-id", request.getFullName(), null, RoleConstants.roleUser, request.getEmail(), request.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        tokenDTO = new TokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(tokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        Token token = new Token("user-id", user.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        AccountResponse response = authService.register(request);

        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getRole(), response.getRole());
        assertEquals("access-token", response.getAccessToken());
        assertEquals(accessExpiry, response.getAccessTokenExpiry());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(refreshExpiry, response.getRefreshTokenExpiry());
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("test@example.com", "password");

        User user = new User("user-id", "Test User", null, RoleConstants.roleUser, request.getEmail(), "encodedPassword");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        tokenDTO = new TokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(tokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        Token token = new Token("token-id", user.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        AccountResponse response = authService.login(request);

        assertEquals(user.getId(), response.getUserId());
        assertEquals(user.getRole(), response.getRole());
        assertEquals("access-token", response.getAccessToken());
        assertEquals(accessExpiry, response.getAccessTokenExpiry());
        assertEquals("refresh-token", response.getRefreshToken());
        assertEquals(refreshExpiry, response.getRefreshTokenExpiry());
    }

    @Test
    @Disabled
    void testChangePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword");

        UserDetail dummyUserDetail = new UserDetail(RoleConstants.roleUser, "test@example.com", null);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyUserDetail);
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        User user = new User("user-id", "Test User", null, RoleConstants.roleUser, "test@example.com", "encodedOldPassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        tokenDTO = new TokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(tokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        Token token = new Token("token-id", user.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(Token.class))).thenReturn(token);

        AccountResponse response = authService.changePassword(request);

        verify(userRepository).save(user);
        assertEquals(user.getId(), response.getUserId());
        assertEquals("access-token", response.getAccessToken());
    }

    @Test
    void testRefreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("access-token", "refresh-token");
        when(cacheService.isBlacklisted("access-token")).thenReturn(false);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        Token existingToken = new Token("token-id", "user-id", "access-token", new Date(System.currentTimeMillis() - 1000), "refresh-token", refreshExpiry);
        when(tokenRepository.findByRefreshToken("refresh-token")).thenReturn(Optional.of(existingToken));

        User user = new User("user-id", "Test User", null, RoleConstants.roleUser, "test@example.com", "encodedPassword");
        when(userRepository.findById("user-id")).thenReturn(Optional.of(user));

        Date newAccessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        tokenDTO = new TokenDTO("new-access-token", newAccessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(tokenDTO);

        Date newRefreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        Token newToken = new Token("token-id-2", user.getId(), "new-access-token", newAccessExpiry, "new-refresh-token", newRefreshExpiry);
        when(tokenRepository.save(any(Token.class))).thenReturn(newToken);

        AccountResponse response = authService.refreshToken(request);

        verify(tokenRepository).delete(existingToken);
        assertEquals("new-access-token", response.getAccessToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
    }

    @Test
    @Disabled
    void testLogout_Success() {
        String accessToken = "access-token";
        Date expiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        when(jwtTokenProvider.extractExpiration(accessToken)).thenReturn(expiry);
        when(cacheService.isBlacklisted(accessToken)).thenReturn(false);

        Token token = new Token("token-id", "token-id", accessToken, expiry, "refresh-token", new Date(System.currentTimeMillis() + 7200 * 1000));
        when(tokenRepository.findByAccessToken(accessToken)).thenReturn(Optional.of(token));

        authService.logout(accessToken);

        verify(tokenRepository).delete(token);
        long ttl = TimeUtils.calculateRemainingTime(new Date(), expiry);
        verify(cacheService).addToBlacklist(accessToken, ttl);
    }

    @Test
    void testAuthenticate_CacheHit() {
        String username = "test@example.com";
        when(jwtTokenProvider.extractUserName("access-token")).thenReturn(username);

        String userKey = String.format("user:%s", username);
        UserDetail cachedDetail = new UserDetail(RoleConstants.roleUser, username, null);
        when(cacheService.get(userKey, UserDetail.class)).thenReturn(cachedDetail);

        UserDetail result = authService.authenticate("access-token");
        assertEquals(cachedDetail, result);
    }

    @Test
    void testAuthenticate_NotInCache() {
        String username = "test@example.com";
        when(jwtTokenProvider.extractUserName("access-token")).thenReturn(username);

        String userKey = String.format("user:%s", username);
        when(cacheService.get(userKey, UserDetail.class)).thenReturn(null);

        User user = new User("user-id", "Test User", null, RoleConstants.roleUser, username, "encodedPassword");
        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));

        doNothing().when(cacheService).put(eq(userKey), any(UserDetail.class));

        UserDetail result = authService.authenticate("access-token");
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetCurrentUser_Success() {
        UserDetail userDetail = new UserDetail(RoleConstants.roleUser, "test@example.com", null);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetail);
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetail result = authService.getCurrentUser();
        assertEquals(userDetail, result);
    }

    @Test
    void testGetCurrentUser_NotAuthenticated() {
        SecurityContextHolder.clearContext();
        AppException exception = assertThrows(AppException.class, () -> authService.getCurrentUser());
        assertNotNull(exception);
    }
}
