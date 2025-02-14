package com.urlshortener.core.domain.account.dataTransferObject.response;

import com.urlshortener.core.domain.account.dataTransferObject.TokenDTO;
import com.urlshortener.core.domain.account.model.Account;

import java.util.Date;

public record AccountResponse (
         String userId,
         String role,
         String accessToken,
         Date accessTokenExpiry,
         String refreshToken,
         Date refreshTokenExpiry
){
    public static AccountResponse from(Account account, TokenDTO tokenDTO) {
        return new AccountResponse(
                account.getId(),
                account.getRole(),
                tokenDTO.accessToken(),
                tokenDTO.accessTokenExpiry(),
                tokenDTO.refreshToken(),
                tokenDTO.refreshTokenExpiry()
        );
    }
}
