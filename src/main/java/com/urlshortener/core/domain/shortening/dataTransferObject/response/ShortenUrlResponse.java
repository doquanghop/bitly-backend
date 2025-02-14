package com.urlshortener.core.domain.shortening.dataTransferObject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ShortenUrlResponse {
    private String originalUrl;
    private String shortUrl;
}