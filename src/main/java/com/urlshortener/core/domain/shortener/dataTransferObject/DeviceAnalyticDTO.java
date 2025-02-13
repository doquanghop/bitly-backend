package com.urlshortener.core.domain.shortener.dataTransferObject;

public record DeviceAnalyticDTO(
        String deviceType,
        long totalClicksAndScans
) {
}
