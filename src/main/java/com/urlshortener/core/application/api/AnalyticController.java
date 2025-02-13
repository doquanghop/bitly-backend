package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.GetAnalyticsSummaryRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AdminAnalyticsSummaryResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.AnalyticsSummaryResponse;
import com.urlshortener.core.domain.shortener.service.IUrlAnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("${api.prefix}/analytic")
@RequiredArgsConstructor
public class AnalyticController {
    private final IUrlAnalyticService urlAnalyticService;

    @GetMapping
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponse>> getAnalyticsSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ApiResponse.<AnalyticsSummaryResponse>build()
                .toEntity();
    }

    @GetMapping("/{urlId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponse>> getAnalyticsSummaryByUrlId(
            @PathVariable String urlId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        var request = new GetAnalyticsSummaryRequest(urlId, startDate, endDate);
        var response = urlAnalyticService.getAnalyticsSummaryByUrlId(request);
        return ApiResponse.<AnalyticsSummaryResponse>build()
                .withData(response)
                .toEntity();
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AdminAnalyticsSummaryResponse>> getOverallAnalyticsSummary(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ){
        var request = new GetAnalyticsSummaryRequest(null, startDate, endDate);
        var response = urlAnalyticService.getOverallAnalytics(request);
        return ApiResponse.<AdminAnalyticsSummaryResponse>build()
                .withData(response)
                .toEntity();
    }
}
