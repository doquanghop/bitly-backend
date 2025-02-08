package com.urlshortener.core.domain.shortener.service;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.ShortenUrlRequest;
import com.urlshortener.core.domain.shortener.dataTransferObject.response.ShortenUrlResponse;

public interface IUrlShortenerService {
    ShortenUrlResponse shortenUrl(ShortenUrlRequest request);
    ShortenUrlResponse decodeUrl(String shortCode);
}
