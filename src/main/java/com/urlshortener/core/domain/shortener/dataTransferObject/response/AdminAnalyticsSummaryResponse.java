package com.urlshortener.core.domain.shortener.dataTransferObject.response;

import com.urlshortener.core.domain.shortener.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.ReferrerAnalyticDTO;

import java.util.List;

public record AdminAnalyticsSummaryResponse(
        long totalShortenedUrls,
        long totalClicks,
        long totalUniqueUsers,
        List<DeviceAnalyticDTO> topDevices,
        List<ReferrerAnalyticDTO> topReferrers,
        List<OverTimeAnalyticDTO> clickTrends
) {
}