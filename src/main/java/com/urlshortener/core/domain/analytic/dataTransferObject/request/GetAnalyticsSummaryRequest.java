package com.urlshortener.core.domain.analytic.dataTransferObject.request;


import java.time.LocalDate;

public record GetAnalyticsSummaryRequest(
        String urlId,
        LocalDate startDate,
        LocalDate endDate
){}
