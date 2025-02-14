package com.urlshortener.core.domain.account.dataTransferObject;

import java.util.Date;

public record RefreshTokenDTO(
        String value,
        Date expiry
) {
}
