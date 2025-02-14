package com.urlshortener.core.domain.account.component;

import com.urlshortener.core.domain.account.dataTransferObject.AccessTokenDTO;
import com.urlshortener.core.domain.account.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.account.exception.AuthException;
import com.urlshortener.core.infrastucture.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAccessTokenDTOProviderTest {
    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    private TokenMetadataDTO tokenMetadata;
    private long accessTokenExpiration;
    @BeforeEach
    void init() {
        accessTokenExpiration = 3600;
        tokenMetadata = new TokenMetadataDTO(
                "user123",
                "userName",
                new Date()
        );
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpiration", accessTokenExpiration);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", "iFHbGVg38AlolC6GzHa9zecLhgy3moTzfv4UNQH8VNs=");

    }

    @Test
    void testGenerateAccessToken_Success() {
        AccessTokenDTO accessTokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);

        assertNotNull(accessTokenDTO);
        assertNotNull(accessTokenDTO.getValue());
        assertNotNull(accessTokenDTO.getExpiry());
    }

    @Test
    void testValidateToken_Success() {
        AccessTokenDTO accessTokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = accessTokenDTO.getValue();

        boolean isValid = jwtTokenProvider.verificationToken(token);

        assertTrue(isValid);
    }

    @Test
    void testValidateToken_ExpiredToken() {
        TokenMetadataDTO expiredTokenMetadata = new TokenMetadataDTO(
                "user123",
                "userName",
                new Date(System.currentTimeMillis() - accessTokenExpiration * 1000 - 3600*1000)
        ); // Expired

        AccessTokenDTO expiredAccessTokenDTO = jwtTokenProvider.generateAccessToken(expiredTokenMetadata);
        String expiredToken = expiredAccessTokenDTO.getValue();

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken(expiredToken));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_EXPIRED.getMessage()));
    }

    @Test
    void testValidateToken_InvalidTokenSignature() {
        AccessTokenDTO accessTokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = accessTokenDTO.getValue();

        String invalidToken = token + "123"; // Invalid token

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken(invalidToken));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_SIGNATURE_INVALID.getMessage()));
    }

    @Test
    void testValidateToken_VerificationFailed() {
        AccessTokenDTO accessTokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = accessTokenDTO.getValue();

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken("tamperedToken"));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_VERIFICATION_FAILED.getMessage()));
    }
}
