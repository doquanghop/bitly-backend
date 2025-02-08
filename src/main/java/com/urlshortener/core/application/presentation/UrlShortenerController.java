package com.urlshortener.core.application.presentation;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.ShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortener.service.IUrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/shortener")
@RequiredArgsConstructor
public class UrlShortenerController {
    private final IUrlShortenerService urlShortenerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> handleCreateUrlShorten(@RequestBody ShortenUrlRequest request) {
        var res = urlShortenerService.shortenUrl(request);
        return ApiResponse.<ShortenUrlResponse>build()
                .withData(res)
                .toEntity();
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> handleGetUrlShorten(@PathVariable String shortUrl) {
        var res = urlShortenerService.decodeUrl(shortUrl);
        return ApiResponse.<ShortenUrlResponse>build()
                .withData(res)
                .toEntity();
    }
}
