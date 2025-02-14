package com.urlshortener.core.domain.shortening.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CreationShortenUrlRequest {
    private String originalUrl;
}
