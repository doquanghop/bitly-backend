package com.urlshortener.core.domain.auth.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RefreshTokenRequest {
    private String accessToken;
    private String refreshToken;
}
