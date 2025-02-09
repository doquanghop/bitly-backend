package com.urlshortener.core.domain.auth.dataTransferObject.response;

import lombok.Data;

@Data
public class AccountResponse {
    private String userId;
    private String role;
    private String accessToken;
    private String refreshToken;
}
