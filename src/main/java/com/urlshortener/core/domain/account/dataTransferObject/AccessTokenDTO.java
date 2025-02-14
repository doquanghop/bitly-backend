package com.urlshortener.core.domain.account.dataTransferObject;

import java.util.Date;

public record AccessTokenDTO(
        String value,
        Date expiry
) {
}
