package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.AdminURLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.dataTransferObject.response.URLAnalyticsResponse;
import com.urlshortener.core.domain.analytic.service.IURLAnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${api.prefix}/analytic")
@RequiredArgsConstructor
public class AnalyticController {
    private final IURLAnalyticService urlAnalyticService;

    @GetMapping
    public ResponseEntity<ApiResponse<URLAnalyticsResponse>> getAnalyticsSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ApiResponse.<URLAnalyticsResponse>build()
                .toEntity();
    }

    @GetMapping("/{urlId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<URLAnalyticsResponse>> getAnalyticsSummaryByUrlId(
            @PathVariable String urlId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        var request = new GetAnalyticsSummaryRequest(urlId, startDate, endDate);
        var response = urlAnalyticService.getAnalyticsSummaryByUrlId(request);
        return ApiResponse.<URLAnalyticsResponse>build()
                .withData(response)
                .toEntity();
    }

    @GetMapping("/{urlId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AdminURLAnalyticsResponse>> getOverallAnalyticsSummaryUrlId(
            @PathVariable String urlId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ){
        var request = new GetAnalyticsSummaryRequest(urlId, startDate, endDate);
        var response = urlAnalyticService.getOverallAnalytics(request);
        return ApiResponse.<AdminURLAnalyticsResponse>build()
                .withData(response)
                .toEntity();
    }
}
