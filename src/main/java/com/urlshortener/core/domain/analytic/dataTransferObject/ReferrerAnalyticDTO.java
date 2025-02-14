package com.urlshortener.core.domain.analytic.dataTransferObject;

public record ReferrerAnalyticDTO (
        String referrer,
        long totalClicksAndScans
){
}
