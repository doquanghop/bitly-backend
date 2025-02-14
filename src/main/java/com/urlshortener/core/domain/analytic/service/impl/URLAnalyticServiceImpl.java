package com.urlshortener.core.domain.analytic.service.impl;

import com.urlshortener.core.domain.account.service.IAccountService;
import com.urlshortener.core.domain.analytic.dataTransferObject.DeviceAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.OverTimeAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.ReferrerAnalyticDTO;
import com.urlshortener.core.domain.analytic.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.AdminURLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.URLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.URLAnalyticsSummaryResponse;
import com.urlshortener.core.domain.analytic.model.URLAnalytic;
import com.urlshortener.core.domain.analytic.repository.URLAnalyticRepository;
import com.urlshortener.core.domain.analytic.service.IURLAnalyticService;
import com.urlshortener.core.domain.subscription.service.IAccountLevelService;
import com.urlshortener.core.domain.subscription.service.IAccountSubscriptionService;
import com.urlshortener.core.infrastucture.constant.DeviceTypeEnum;
import com.urlshortener.core.infrastucture.constant.ReferrerTypeEnum;
import com.urlshortener.core.infrastucture.exception.AppException;
import com.urlshortener.core.infrastucture.service.cache.CacheService;
import com.urlshortener.core.infrastucture.utils.RequestUtils;
import com.urlshortener.core.infrastucture.utils.UserAgentParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class URLAnalyticServiceImpl implements IURLAnalyticService {
    private final IAccountService accountService;
    private final IAccountLevelService accountLevelService;
    private final IAccountSubscriptionService accountSubscriptionService;
    private final CacheService cacheService;
    private final URLAnalyticRepository urlAnalyticRepository;

    @Value("${cache.key.url.analytic.visited}")
    private String VISITED_CACHE_KEY;
    private static final long VISITED_CACHE_EXPIRATION_TIME = 60L;

    @Value("${url.analytics.device.top}")
    private int deviceAnalyticsTop;
    @Value("${url.analytics.referrer.top}")
    private int referrerAnalyticsTop;

    @Override
    public boolean shouldLogView(String urlId, HttpServletRequest httpServletRequest) {
        String ipAddress = RequestUtils.getIpAddress(httpServletRequest);
        String cacheKeyRequest = String.format(VISITED_CACHE_KEY, urlId, ipAddress);
        if (cacheService.exists(cacheKeyRequest)) return false;
        cacheService.put(cacheKeyRequest, "visited", VISITED_CACHE_EXPIRATION_TIME);
        return true;
    }

    @Override
    public void saveUrlAnalytic(SaveUrlAnalyticRequest request) {
        String ipAddress = RequestUtils.getIpAddress(request.getHttpServletRequest());
        String userAgent = RequestUtils.getUserAgent(request.getHttpServletRequest());
        DeviceTypeEnum deviceType = UserAgentParser.getDeviceType(userAgent);
        ReferrerTypeEnum referrerType = ReferrerTypeEnum.fromString(RequestUtils.getReferrer(request.getHttpServletRequest()));

        URLAnalytic newURLAnalytic = URLAnalytic.builder()
                .urlId(request.getUrlId())
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .deviceType(deviceType.name())
                .referrer(referrerType.name())
                .accessedAt(request.getAccessedAt())
                .build();
        urlAnalyticRepository.save(newURLAnalytic);
    }

    @Override
    public URLAnalyticsResponse getAnalyticsSummaryByUrlId(GetAnalyticsSummaryRequest request) {
        var currentUser = accountService.getCurrentUser();
        var accountSubscription = accountSubscriptionService.getSubscriptionByUserId(currentUser.getId());
        if (accountSubscription == null) throw new AppException(null);
        var accountLevel = accountLevelService.getAccountLevelById(accountSubscription.accountLevelId());

        long totalClicksAndScans = urlAnalyticRepository.countByUrlId(request.urlId());
        if (totalClicksAndScans == 0) return new URLAnalyticsResponse();

        Pageable deviceAnalyticsPageable = PageRequest.of(0, deviceAnalyticsTop);
        List<DeviceAnalyticDTO> deviceAnalytics = urlAnalyticRepository.findTopDeviceTypesByUrlId(
                request.urlId(),
                deviceAnalyticsPageable
        );

        Pageable referrerAnalyticsPageable = PageRequest.of(0, referrerAnalyticsTop);
        List<ReferrerAnalyticDTO> referrerAnalytics = urlAnalyticRepository.findTopReferrersByUrlId(
                request.urlId(),
                referrerAnalyticsPageable
        );

        Pageable overTimeAnalyticsPageable = PageRequest.of(0, 20);
        List<OverTimeAnalyticDTO> overTimeAnalytics = urlAnalyticRepository.findOverTimeByUrlIdAndDateRange(
                request.urlId(),
                request.startDate(), request.endDate(),
                overTimeAnalyticsPageable
        );

        return new URLAnalyticsResponse(
                totalClicksAndScans,
                deviceAnalytics,
                referrerAnalytics,
                overTimeAnalytics
        );
    }

    @Override
    public URLAnalyticsSummaryResponse getAnalyticsDetail() {
        var currentUser = accountService.getCurrentUser();
        long totalUrls = urlAnalyticRepository.countByUserId(currentUser.getId());
        if (totalUrls == 0) return new URLAnalyticsSummaryResponse();


        return null;
    }

    @Override
    public AdminURLAnalyticsResponse getOverallAnalytics(GetAnalyticsSummaryRequest request) {
        return null;
    }
}
