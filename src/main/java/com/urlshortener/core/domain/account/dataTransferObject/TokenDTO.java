package com.urlshortener.core.domain.account.dataTransferObject;

import java.util.Date;

public record TokenDTO (
         String userId,
         String accessToken,
         Date accessTokenExpiry,
         String refreshToken,
         Date refreshTokenExpiry
){}