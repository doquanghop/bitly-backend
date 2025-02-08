package com.urlshortener.core.domain.shortener.dataTransferObject.request;

import lombok.Data;

@Data
public class DeletionShortenUrlRequest {
    private String shortUrlCode;
}
