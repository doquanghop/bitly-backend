package com.urlshortener.core.domain.shortener.dataTransferObject.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class GetOriginalUrlRequest {
    private HttpServletRequest httpServletRequest;
    private String shortUrlCode;
}
