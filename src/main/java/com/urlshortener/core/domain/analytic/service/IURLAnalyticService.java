package com.urlshortener.core.domain.analytic.service;

import com.urlshortener.core.domain.analytic.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.AdminURLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.URLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.URLAnalyticsSummaryResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface IURLAnalyticService {
    boolean shouldLogView(String urlId, HttpServletRequest httpServletRequest);
    void saveUrlAnalytic(SaveUrlAnalyticRequest urlAnalytic);
    URLAnalyticsResponse getAnalyticsSummaryByUrlId(GetAnalyticsSummaryRequest request);
    URLAnalyticsSummaryResponse getAnalyticsDetail();
    AdminURLAnalyticsResponse getOverallAnalytics(GetAnalyticsSummaryRequest request);
}
