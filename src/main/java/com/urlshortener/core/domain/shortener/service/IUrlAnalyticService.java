package com.urlshortener.core.domain.shortener.service;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AdminAnalyticsSummaryResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AnalyticsSummaryResponse;

public interface IUrlAnalyticService {
    void saveUrlAnalytic(SaveUrlAnalyticRequest urlAnalytic);
    AnalyticsSummaryResponse getAnalyticsSummaryByUrlId(GetAnalyticsSummaryRequest request);
    AdminAnalyticsSummaryResponse getOverallAnalytics(GetAnalyticsSummaryRequest request);
}
