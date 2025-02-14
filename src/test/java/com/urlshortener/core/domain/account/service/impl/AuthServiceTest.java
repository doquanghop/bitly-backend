package com.urlshortener.core.domain.account.service.impl;

import com.urlshortener.core.domain.account.component.JwtTokenProvider;
import com.urlshortener.core.domain.account.constant.RoleConstants;
import com.urlshortener.core.domain.account.dataTransferObject.AccessTokenDTO;
import com.urlshortener.core.domain.account.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.account.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.account.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.account.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.account.model.Account;
import com.urlshortener.core.domain.account.repository.AccountRepository;
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
    private AccountRepository accountRepository;
    @InjectMocks
    private AccountServiceImpl authService;

    private AccessTokenDTO accessTokenDTO;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(authService, "refreshTokenExpiration", 3600L);
        ReflectionTestUtils.setField(authService, "userDetailCacheKey", "user:%s");
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password");

        when(accountRepository.existsByEmail(request.getEmail())).thenReturn(false);

        Account account = new Account("user-id", request.getFullName(), null, RoleConstants.roleUser, request.getEmail(), request.getPassword());
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        this.accessTokenDTO = new AccessTokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(this.accessTokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        TokenDTO tokenDTO = new TokenDTO( account.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(TokenDTO.class))).thenReturn(tokenDTO);

        AccountResponse response = authService.register(request);

        assertEquals(account.getId(), response.userId());
        assertEquals(account.getRole(), response.role());
        assertEquals("access-token", response.accessToken());
        assertEquals(accessExpiry, response.accessTokenExpiry());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals(refreshExpiry, response.refreshTokenExpiry());
    }

    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("test@example.com", "password");

        Account account = new Account("user-id", "Test User", null, RoleConstants.roleUser, request.getEmail(), "encodedPassword");
        when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(request.getPassword(), account.getPassword())).thenReturn(true);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        this.accessTokenDTO = new AccessTokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(this.accessTokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        TokenDTO tokenDTO = new TokenDTO( account.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(TokenDTO.class))).thenReturn(tokenDTO);

        AccountResponse response = authService.login(request);

        assertEquals(account.getId(), response.userId());
        assertEquals(account.getRole(), response.role());
        assertEquals("access-token", response.accessToken());
        assertEquals(accessExpiry, response.accessTokenExpiry());
        assertEquals("refresh-token", response.refreshToken());
        assertEquals(refreshExpiry, response.refreshTokenExpiry());
    }

    @Test
    @Disabled
    void testChangePassword_Success() {
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword");

        UserDetail dummyUserDetail = new UserDetail("user-id",RoleConstants.roleUser, "test@example.com", null);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(dummyUserDetail);
        when(auth.isAuthenticated()).thenReturn(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        Account account = new Account("user-id", "Test User", null, RoleConstants.roleUser, "test@example.com", "encodedOldPassword");
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(account));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);

        Date accessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        this.accessTokenDTO = new AccessTokenDTO("access-token", accessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(this.accessTokenDTO);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        TokenDTO tokenDTO = new TokenDTO( account.getId(), "access-token", accessExpiry, "refresh-token", refreshExpiry);
        when(tokenRepository.save(any(TokenDTO.class))).thenReturn(tokenDTO);

        AccountResponse response = authService.changePassword(request);

        verify(accountRepository).save(account);
        assertEquals(account.getId(), response.userId());
        assertEquals("access-token", response.accessToken());
    }

    @Test
    void testRefreshToken_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("access-token", "refresh-token");
        when(cacheService.isBlacklisted("access-token")).thenReturn(false);

        Date refreshExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        TokenDTO existingTokenDTO = new TokenDTO( "user-id", "access-token", new Date(System.currentTimeMillis() - 1000), "refresh-token", refreshExpiry);
        when(tokenRepository.findByRefreshToken("refresh-token")).thenReturn(Optional.of(existingTokenDTO));

        Account account = new Account("user-id", "Test User", null, RoleConstants.roleUser, "test@example.com", "encodedPassword");
        when(accountRepository.findById("user-id")).thenReturn(Optional.of(account));

        Date newAccessExpiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        accessTokenDTO = new AccessTokenDTO("new-access-token", newAccessExpiry);
        when(jwtTokenProvider.generateAccessToken(any(TokenMetadataDTO.class))).thenReturn(accessTokenDTO);

        Date newRefreshExpiry = new Date(System.currentTimeMillis() + 7200 * 1000);
        TokenDTO newTokenDTO = new TokenDTO( account.getId(), "new-access-token", newAccessExpiry, "new-refresh-token", newRefreshExpiry);
        when(tokenRepository.save(any(TokenDTO.class))).thenReturn(newTokenDTO);

        AccountResponse response = authService.refreshToken(request);

        verify(tokenRepository).delete(existingTokenDTO);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    @Disabled
    void testLogout_Success() {
        String accessToken = "access-token";
        Date expiry = new Date(System.currentTimeMillis() + 3600 * 1000);
        when(jwtTokenProvider.extractExpiration(accessToken)).thenReturn(expiry);
        when(cacheService.isBlacklisted(accessToken)).thenReturn(false);

        TokenDTO tokenDTO = new TokenDTO( "token-id", accessToken, expiry, "refresh-token", new Date(System.currentTimeMillis() + 7200 * 1000));
        when(tokenRepository.findByAccessToken(accessToken)).thenReturn(Optional.of(tokenDTO));

        authService.logout(accessToken);

        verify(tokenRepository).delete(tokenDTO);
        long ttl = TimeUtils.calculateRemainingTime(new Date(), expiry);
        verify(cacheService).addToBlacklist(accessToken, ttl);
    }

    @Test
    void testAuthenticate_CacheHit() {
        String username = "test@example.com";
        when(jwtTokenProvider.extractUserName("access-token")).thenReturn(username);

        String userKey = String.format("user:%s", username);
        UserDetail cachedDetail = new UserDetail("user-id",RoleConstants.roleUser, username, null);
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

        Account account = new Account("user-id", "Test User", null, RoleConstants.roleUser, username, "encodedPassword");
        when(accountRepository.findByEmail(username)).thenReturn(Optional.of(account));

        doNothing().when(cacheService).put(eq(userKey), any(UserDetail.class));

        UserDetail result = authService.authenticate("access-token");
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void testGetCurrentUser_Success() {
        UserDetail userDetail = new UserDetail("user-id",RoleConstants.roleUser, "test@example.com", null);
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
