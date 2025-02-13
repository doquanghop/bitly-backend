package com.urlshortener.core.domain.shortener.service.impl;

import com.urlshortener.core.domain.shortener.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.ReferrerAnalyticDTO;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AdminAnalyticsSummaryResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AnalyticsSummaryResponse;
import com.urlshortener.core.domain.shortener.model.UrlAnalytic;
import com.urlshortener.core.domain.shortener.repository.UrlAnalyticRepository;
import com.urlshortener.core.domain.shortener.service.IUrlAnalyticService;
import com.urlshortener.core.infrastucture.service.cache.CacheService;
import com.urlshortener.core.infrastucture.utils.RequestUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UrlAnalyticServiceImpl implements IUrlAnalyticService {
    private final CacheService cacheService;
    private final UrlAnalyticRepository urlAnalyticRepository;

    @Value("${cache.key.url.analytic.visited}")
    private String VISITED_CACHE_KEY;
    private static final long VISITED_CACHE_EXPIRATION_TIME = 60L;

    @Value("${url.analytics.device.top}")
    private int deviceAnalyticsTop;

    @Value("${url.analytics.referrer.top}")
    private int referrerAnalyticsTop;

    @Override
    public void saveUrlAnalytic(SaveUrlAnalyticRequest request) {
        String ipAddress = RequestUtils.getIpAddress(request.getHttpServletRequest());

        String cacheKeyRequest = String.format(VISITED_CACHE_KEY, request.getUrlId(), ipAddress);
        if (cacheService.exists(cacheKeyRequest)) {
            return;
        }

        String userAgent = RequestUtils.getUserAgent(request.getHttpServletRequest());

        UrlAnalytic newUrlAnalytic = new UrlAnalytic(
        );
        urlAnalyticRepository.save(newUrlAnalytic);
        cacheService.put(cacheKeyRequest, "visited", VISITED_CACHE_EXPIRATION_TIME);
    }

    @Override
    public AnalyticsSummaryResponse getAnalyticsSummaryByUrlId(GetAnalyticsSummaryRequest request) {
        long totalClicksAndScans = urlAnalyticRepository.countByUrlId(request.id());
        if (totalClicksAndScans == 0) {
            return new AnalyticsSummaryResponse();
        }

        Pageable deviceAnalyticsPageable = PageRequest.of(0, deviceAnalyticsTop);
        List<DeviceAnalyticDTO> deviceAnalytics = urlAnalyticRepository.findTopDeviceTypesByUrlId(
                request.id(),
                deviceAnalyticsPageable
        );

        Pageable referrerAnalyticsPageable = PageRequest.of(0, referrerAnalyticsTop);
        List<ReferrerAnalyticDTO> referrerAnalytics = urlAnalyticRepository.findTopReferrersByUrlId(
                request.id(),
                referrerAnalyticsPageable
        );

        Pageable overTimeAnalyticsPageable = PageRequest.of(0, 20);
        List<OverTimeAnalyticDTO> overTimeAnalytics = urlAnalyticRepository.findOverTimeByUrlIdAndDateRange(
                request.id(),
                request.startDate(), request.endDate(),
                overTimeAnalyticsPageable
        );

        return new AnalyticsSummaryResponse(
                totalClicksAndScans,
                deviceAnalytics,
                referrerAnalytics,
                overTimeAnalytics
        );
    }

    @Override
    public AdminAnalyticsSummaryResponse getOverallAnalytics(GetAnalyticsSummaryRequest request) {
        return null;
    }
}
