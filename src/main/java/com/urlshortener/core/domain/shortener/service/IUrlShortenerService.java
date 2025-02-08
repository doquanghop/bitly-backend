package com.urlshortener.core.domain.shortener.service;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;

public interface IUrlShortenerService {
    ShortenUrlResponse shortenUrl(CreationShortenUrlRequest request);

    ShortenUrlResponse decodeUrl(String shortCode);

    void deleteShortenUrl(DeletionShortenUrlRequest request);
}
