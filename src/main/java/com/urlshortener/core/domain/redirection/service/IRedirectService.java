package com.urlshortener.core.domain.redirection.service;

import com.urlshortener.core.domain.shortening.dataTransferObject.request.GetOriginalUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;

public interface IRedirectService {
    ShortenUrlResponse getOriginalUrl(GetOriginalUrlRequest request);
}
