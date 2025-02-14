package com.urlshortener.core.domain.shortening.dataTransferObject.request;

import lombok.Data;

@Data
public class DeletionShortenUrlRequest {
    private String shortUrlCode;
}
