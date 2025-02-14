package com.urlshortener.core.domain.analytic.dataTransferObject.response;

import com.urlshortener.core.domain.analytic.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.ReferrerAnalyticDTO;

import java.util.List;

public record URLAnalyticsSummaryResponse(
        long totalURL,
        List<DeviceAnalyticDTO> topDeviceTypes,
        List<ReferrerAnalyticDTO> topReferrers,
        List<OverTimeAnalyticDTO> clickOverTime
) {
    public URLAnalyticsSummaryResponse(){
        this(0L, List.of(), List.of(), List.of());
    }
}