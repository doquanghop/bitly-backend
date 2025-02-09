package com.urlshortener.core.domain.auth.dataTransferObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data @AllArgsConstructor
public class TokenMetadataDTO {
    private String userId;
    private String userName;
    private Date issuedAt;
}
