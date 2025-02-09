package com.urlshortener.core.domain.auth.component;

import com.urlshortener.core.domain.auth.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.auth.dataTransferObject.TokenMetadataDTO;
import com.urlshortener.core.domain.auth.exception.AuthException;
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
class JwtTokenProviderTest {
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
        TokenDTO tokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);

        assertNotNull(tokenDTO);
        assertNotNull(tokenDTO.getValue());
        assertNotNull(tokenDTO.getExpiry());
    }

    @Test
    void testValidateToken_Success() {
        TokenDTO tokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = tokenDTO.getValue();

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

        TokenDTO expiredTokenDTO = jwtTokenProvider.generateAccessToken(expiredTokenMetadata);
        String expiredToken = expiredTokenDTO.getValue();

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken(expiredToken));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_EXPIRED.getMessage()));
    }

    @Test
    void testValidateToken_InvalidTokenSignature() {
        TokenDTO tokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = tokenDTO.getValue();

        String invalidToken = token + "123"; // Invalid token

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken(invalidToken));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_SIGNATURE_INVALID.getMessage()));
    }

    @Test
    void testValidateToken_VerificationFailed() {
        TokenDTO tokenDTO = jwtTokenProvider.generateAccessToken(tokenMetadata);
        String token = tokenDTO.getValue();

        AppException exception = assertThrows(AppException.class, () -> jwtTokenProvider.verificationToken("tamperedToken"));
        assertTrue(exception.getMessage().contains(AuthException.TOKEN_VERIFICATION_FAILED.getMessage()));
    }
}
