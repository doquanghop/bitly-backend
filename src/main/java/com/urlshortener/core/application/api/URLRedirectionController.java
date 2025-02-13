package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.redirection.service.IRedirectService;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.GetOriginalUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/redirection")
@RequiredArgsConstructor
public class URLRedirectionController {
    private final IRedirectService redirectService;

    @GetMapping("/{shortUrlCode}")
    public ResponseEntity<ApiResponse<ShortenUrlResponse>> getLongUrl(
            @PathVariable String shortUrlCode,
            HttpServletRequest httpServletRequest
    ) {
        var request = new GetOriginalUrlRequest(
                httpServletRequest,
                shortUrlCode
        );
        var res = redirectService.getOriginalUrl(request);
        return ApiResponse.<ShortenUrlResponse>build()
                .withData(res)
                .toEntity();
    }
}
