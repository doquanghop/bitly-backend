package com.urlshortener.core.domain.analytic.dataTransferObject;

public record OverTimeAnalyticDTO(
        String accessedAt,
        long totalClicksAndScans
) {
}
