package com.urlshortener.core.domain.shortener.dataTransferObject.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class SaveUrlAnalyticRequest {
    private HttpServletRequest httpServletRequest;
    private String urlId;
    private LocalDateTime accessedAt;
}
