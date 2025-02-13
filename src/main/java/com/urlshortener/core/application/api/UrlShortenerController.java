package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.GetOriginalUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortener.service.IUrlShortenerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/shortener")
@RequiredArgsConstructor
public class UrlShortenerController {
    private final IUrlShortenerService urlShortenerService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> creationShortenUrl(@RequestBody CreationShortenUrlRequest request) {
        var res = urlShortenerService.shortenUrl(request);
        return ApiResponse.<ShortenUrlResponse>build()
                .withData(res)
                .toEntity();
    }

    @GetMapping("/{shortUrlCode}")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> getLongUrl(
            @PathVariable String shortUrlCode,
            HttpServletRequest httpServletRequest
    ) {
        var request = new GetOriginalUrlRequest(
                httpServletRequest,
                shortUrlCode
        );
        var res = urlShortenerService.decodeUrl(request);
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
