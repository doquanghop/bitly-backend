package com.urlshortener.core.domain.analytic.dataTransferObject.response;

import com.urlshortener.core.domain.analytic.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.ReferrerAnalyticDTO;

import java.util.List;

public record AdminURLAnalyticsResponse(
        long totalShortenedUrls,
        long totalClicks,
        long totalUniqueUsers,
        List<DeviceAnalyticDTO> topDevices,
        List<ReferrerAnalyticDTO> topReferrers,
        List<OverTimeAnalyticDTO> clickTrends
) {
}