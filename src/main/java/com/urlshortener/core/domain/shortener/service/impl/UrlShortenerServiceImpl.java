package com.urlshortener.core.domain.shortener.service.impl;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.ShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortener.exception.ShortenException;
import com.urlshortener.core.domain.shortener.model.Url;
import com.urlshortener.core.domain.shortener.repository.UrlRepository;
import com.urlshortener.core.domain.shortener.service.IUrlEncoder;
import com.urlshortener.core.domain.shortener.service.IUrlShortenerService;
import com.urlshortener.core.infrastucture.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements IUrlShortenerService {
    private final IUrlEncoder urlEncoder;
    private final UrlRepository urlRepository;

    @Value("${client.url}")
    private String clientUrl;

    @Override
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {
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
    public ShortenUrlResponse decodeUrl(String shortCode) {
        Url existingUrl = urlRepository.findByShortUrlCode(shortCode)
                .orElseThrow(() -> new AppException(ShortenException.SHORT_URL_NOT_FOUND));
        return new ShortenUrlResponse(
                existingUrl.getOriginalUrl(),
                getShortUrl(existingUrl.getShortUrlCode())
        );
    }
}
