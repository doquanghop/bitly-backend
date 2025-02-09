package com.urlshortener.core.domain.auth.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class RefreshTokenDTO {
    private String value;
    private Date expiry;
}
