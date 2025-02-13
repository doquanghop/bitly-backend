package com.urlshortener.core.domain.shortener.dataTransferObject;

public record ReferrerAnalyticDTO (
        String referrer,
        long totalClicksAndScans
){
}
