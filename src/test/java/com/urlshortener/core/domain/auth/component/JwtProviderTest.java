package com.urlshortener.core.domain.auth.component;

import com.urlshortener.core.domain.auth.dataTransferObject.TokenDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class JwtProviderTest {
    @Mock
    private TokenDTO tokenDTO;
    @InjectMocks
    private JwtProvider jwtProvider;
    @Test
    void testGenerateAccessToken() {

    }
}
