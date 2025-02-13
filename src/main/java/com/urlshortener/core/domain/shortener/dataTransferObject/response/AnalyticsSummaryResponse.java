package com.urlshortener.core.domain.shortener.dataTransferObject.response;

import com.urlshortener.core.domain.shortener.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.ReferrerAnalyticDTO;

import java.util.List;

public record AnalyticsSummaryResponse(
        long totalClickAndScan,
        List<DeviceAnalyticDTO> topDeviceTypes,
        List<ReferrerAnalyticDTO> topReferrers,
        List<OverTimeAnalyticDTO> clickOverTime
) {
    public AnalyticsSummaryResponse(){
        this(0L, List.of(), List.of(), List.of());
    }
}