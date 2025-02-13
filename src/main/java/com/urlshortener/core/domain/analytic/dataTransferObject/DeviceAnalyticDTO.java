package com.urlshortener.core.domain.analytic.dataTransferObject;

public record DeviceAnalyticDTO(
        String deviceType,
        long totalClicksAndScans
) {
}
