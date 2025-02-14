package com.urlshortener.core.domain.shortening.service.impl;

import com.urlshortener.core.domain.shortening.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortening.exception.ShortenException;
import com.urlshortener.core.domain.shortening.model.URL;
import com.urlshortener.core.domain.shortening.repository.URLRepository;
import com.urlshortener.core.domain.shortening.service.IURLShortenerService;
import com.urlshortener.core.domain.shortening.component.EncoderService;
import com.urlshortener.core.infrastucture.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class URLShortenerServiceImpl implements IURLShortenerService {
    private final EncoderService urlEncoder;
    private final URLRepository urlRepository;

    @Override
    public ShortenUrlResponse shortenUrl(CreationShortenUrlRequest request) {
        String shortedUrlCode = urlEncoder.encode(request.getOriginalUrl());
        URL newURL = URL.builder()
                .originalUrl(request.getOriginalUrl())
                .shortUrlCode(shortedUrlCode)
                .build();
        urlRepository.save(newURL);
        return new ShortenUrlResponse(
                request.getOriginalUrl(),
                shortedUrlCode
        );
    }

    @Override
    public void deleteShortenUrl(DeletionShortenUrlRequest request) {
        URL existingURL = urlRepository.findByShortUrlCode(request.getShortUrlCode())
                .orElseThrow(() -> new AppException(ShortenException.SHORT_URL_NOT_FOUND));
        urlRepository.delete(existingURL);
    }
}
