package com.urlshortener.core.domain.shortener.dataTransferObject.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ShortenUrlResponse {
    private String originalUrl;
    private String shortUrl;
}