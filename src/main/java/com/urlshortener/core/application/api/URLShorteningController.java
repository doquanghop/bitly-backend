package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortening.service.IURLShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/shortener")
@RequiredArgsConstructor
public class URLShorteningController {
    private final IURLShortenerService urlShortenerService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> creationShortenUrl(@RequestBody CreationShortenUrlRequest request) {
        var res = urlShortenerService.shortenUrl(request);
        return ApiResponse.<ShortenUrlResponse>build()
                .withData(res)
                .toEntity();
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteShortenUrl(@RequestBody DeletionShortenUrlRequest request) {
        urlShortenerService.deleteShortenUrl(request);
        return ApiResponse.<Void>build().toEntity();
    }

}
