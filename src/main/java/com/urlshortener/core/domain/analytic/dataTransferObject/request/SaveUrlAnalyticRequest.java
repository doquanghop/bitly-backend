package com.urlshortener.core.domain.analytic.dataTransferObject.request;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @AllArgsConstructor @NoArgsConstructor
public class SaveUrlAnalyticRequest {
    private HttpServletRequest httpServletRequest;
    private String urlId;
    private LocalDateTime accessedAt;
}
