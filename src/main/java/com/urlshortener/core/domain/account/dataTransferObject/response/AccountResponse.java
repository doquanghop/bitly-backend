package com.urlshortener.core.domain.account.dataTransferObject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class AccountResponse {
    private String userId;
    private String role;
    private String accessToken;
    private Date accessTokenExpiry;
    private String refreshToken;
    private Date refreshTokenExpiry;
}
