package com.urlshortener.core.domain.shortener.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ShortenUrlRequest {
    private String originalUrl;
}
