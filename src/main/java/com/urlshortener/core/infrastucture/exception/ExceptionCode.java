package com.urlshortener.core.infrastucture.exception;

public interface ExceptionCode {
    Integer getCode();
    String getType();
    String getMessage();
}
