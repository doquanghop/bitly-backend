package com.urlshortener.core.domain.shortener.exception;

import com.urlshortener.core.infrastucture.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum ShortenException implements ExceptionCode {
    SHORT_URL_NOT_FOUND("SHORT_URL_NOT_FOUND", 404, null);

    private final String type;
    private final Integer code;
    private final String message;
}
