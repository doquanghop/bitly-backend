package com.urlshortener.core.domain.auth.exception;

import com.urlshortener.core.infrastucture.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthException implements ExceptionCode {
    TOKEN_EXPIRED("AUTH001", 401, "Token has expired"),
    TOKEN_VERIFICATION_FAILED("AUTH005", 403, "Token verification failed"),
    TOKEN_SIGNATURE_INVALID("AUTH006", 403, "Token signature is invalid");

    private final String type;
    private final Integer code;
    private final String message;
}
