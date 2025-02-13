package com.urlshortener.core.domain.shortening.dataTransferObject.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class GetOriginalUrlRequest {
    private HttpServletRequest httpServletRequest;
    private String shortUrlCode;
}
