package com.urlshortener.core.domain.redirection.service.impl;

import com.urlshortener.core.domain.analytic.service.IURLAnalyticService;
import com.urlshortener.core.domain.redirection.service.IRedirectService;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.GetOriginalUrlRequest;
import com.urlshortener.core.domain.analytic.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortening.exception.ShortenException;
import com.urlshortener.core.domain.shortening.model.URL;
import com.urlshortener.core.domain.shortening.repository.URLRepository;
import com.urlshortener.core.infrastucture.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedirectServiceImpl implements IRedirectService {
    private final IURLAnalyticService urlAnalyticService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final URLRepository urlRepository;

    @Value("${kafka.topic.url.analytic.visited}")
    private String URL_ANALYTIC_VISITED_TOPIC;

    @Override
    public ShortenUrlResponse getOriginalUrl(GetOriginalUrlRequest request) {
        LocalDateTime accessAt = LocalDateTime.now();
        URL existingURL = urlRepository.findByShortUrlCode(request.getShortUrlCode())
                .orElseThrow(() -> new AppException(ShortenException.SHORT_URL_NOT_FOUND));
        logAccessEvent(existingURL.getId(), accessAt, request.getHttpServletRequest());
        return new ShortenUrlResponse(
                existingURL.getOriginalUrl(),
                existingURL.getShortUrlCode()
        );
    }

    private void logAccessEvent(String urlId, LocalDateTime accessAt, HttpServletRequest httpServletRequest) {
        if (!urlAnalyticService.shouldLogView(urlId, httpServletRequest)) {
            return;
        }

        var saveUrlAnalytic = new SaveUrlAnalyticRequest(
                httpServletRequest,
                urlId,
                accessAt
        );
        kafkaTemplate.send(URL_ANALYTIC_VISITED_TOPIC, saveUrlAnalytic);
    }
}
