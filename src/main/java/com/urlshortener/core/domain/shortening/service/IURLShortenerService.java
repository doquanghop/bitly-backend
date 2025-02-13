package com.urlshortener.core.domain.shortening.service;

import com.urlshortener.core.domain.shortening.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.DeletionShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;

public interface IURLShortenerService {
    ShortenUrlResponse shortenUrl(CreationShortenUrlRequest request);


    void deleteShortenUrl(DeletionShortenUrlRequest request);
}
