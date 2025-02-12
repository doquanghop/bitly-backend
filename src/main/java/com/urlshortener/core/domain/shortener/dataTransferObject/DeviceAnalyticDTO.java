package com.urlshortener.core.domain.shortener.dataTransferObject;

public record CountTopDeviceAnalyticDTO(
        String deviceType,
        long totalClicksAndScans
) {
}
