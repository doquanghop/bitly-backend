package com.urlshortener.core.domain.shortener.dataTransferObject.request;


import java.time.LocalDate;

public record GetAnalyticsSummaryRequest(
        String id,
        LocalDate startDate,
        LocalDate endDate
){}
