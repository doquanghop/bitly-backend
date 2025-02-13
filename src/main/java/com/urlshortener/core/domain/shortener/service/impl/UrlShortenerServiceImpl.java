package com.urlshortener.core.domain.shortener.service.impl;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.GetOriginalUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortener.exception.ShortenException;
import com.urlshortener.core.domain.shortener.model.Url;
import com.urlshortener.core.domain.shortener.repository.UrlRepository;
import com.urlshortener.core.domain.shortener.service.IUrlShortenerService;
import com.urlshortener.core.domain.shortener.service.UrlEncoderService;
import com.urlshortener.core.infrastucture.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements IUrlShortenerService {
    private final UrlEncoderService urlEncoder;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UrlRepository urlRepository;

    @Value("${client.url}")
    private String clientUrl;
    @Value("${kafka.topic.url.analytic.visited}")
    private String URL_ANALYTIC_VISITED_TOPIC;

    @Override
    public ShortenUrlResponse shortenUrl(CreationShortenUrlRequest request) {
        String shortedUrlCode = urlEncoder.encode(request.getOriginalUrl());
        Url newUrl = Url.builder()
                .originalUrl(request.getOriginalUrl())
                .shortUrlCode(shortedUrlCode)
                .build();
        urlRepository.save(newUrl);
        return new ShortenUrlResponse(
                request.getOriginalUrl(),
                getShortUrl(shortedUrlCode)
        );
    }

    private String getShortUrl(String shortedUrlCode) {
        StringBuilder url = new StringBuilder(clientUrl);
        url.append("/").append(shortedUrlCode);
        return url.toString();
    }

    @Override
    public ShortenUrlResponse decodeUrl(GetOriginalUrlRequest request) {
        Url existingUrl = urlRepository.findByShortUrlCode(request.getShortUrlCode())
                .orElseThrow(() -> new AppException(ShortenException.SHORT_URL_NOT_FOUND));

        var saveUrlAnalytic = new SaveUrlAnalyticRequest(
                request.getHttpServletRequest(),
                existingUrl.getId(),
                LocalDateTime.now()
        );
        kafkaTemplate.send(URL_ANALYTIC_VISITED_TOPIC, saveUrlAnalytic);
        return new ShortenUrlResponse(
                existingUrl.getOriginalUrl(),
                getShortUrl(existingUrl.getShortUrlCode())
        );
    }

    @Override
    public void deleteShortenUrl(DeletionShortenUrlRequest request) {
        Url existingUrl = urlRepository.findByShortUrlCode(request.getShortUrlCode())
                .orElseThrow(() -> new AppException(ShortenException.SHORT_URL_NOT_FOUND));
        urlRepository.delete(existingUrl);
    }
}
