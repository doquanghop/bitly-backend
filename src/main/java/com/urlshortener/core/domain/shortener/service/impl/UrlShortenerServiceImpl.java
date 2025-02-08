package com.urlshortener.core.domain.shortener.service.impl;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.ShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortener.model.Url;
import com.urlshortener.core.domain.shortener.repository.UrlRepository;
import com.urlshortener.core.domain.shortener.service.IUrlEncoder;
import com.urlshortener.core.domain.shortener.service.IUrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerServiceImpl implements IUrlShortenerService {
    private final IUrlEncoder urlEncoder;
    private final UrlRepository urlRepository;

    @Override
    public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {
        String shortedUrl = urlEncoder.encode(request.getLongUrl());
        Url newUrl = Url.builder()
                .originalUrl(request.getLongUrl())
                .shortUrl(shortedUrl)
                .build();
        urlRepository.save(newUrl);
        return new ShortenUrlResponse(
                request.getLongUrl(),
                shortedUrl
        );
    }

    @Override
    public ShortenUrlResponse decodeUrl(String shortCode) {
        Url existingUrl = urlRepository.findByShortUrl(shortCode);
        return new ShortenUrlResponse(
                existingUrl.getOriginalUrl(),
                shortCode
        );
    }
}
