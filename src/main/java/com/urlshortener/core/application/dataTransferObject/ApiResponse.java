package com.urlshortener.core.application.dataTransferObject;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private HttpHeaders headers;
    private int code;
    private Boolean isSuccess;
    private String message;
    private T data;
    private Map<String, Object> errors;
    private Date timestamp;

    public ApiResponse() {
    }

    @PostConstruct
    private void init() {
        httpStatus = HttpStatus.OK;
        code = 200;
        isSuccess = true;
        errors = new HashMap<>();
    }

    public static <T> ApiResponse<T> build() {
        return new ApiResponse<>();
    }

    public ApiResponse<T> withCode(int code) {
        this.code = code;
        return this;
    }

    public ApiResponse<T> withData(T data) {
        this.data = data;
        return this;
    }

    public ApiResponse<T> withHttpHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public ApiResponse<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiResponse<T> withErrors(Map<String, Object> errors) {
        this.errors = errors;
        return this;
    }

    public ResponseEntity<ApiResponse<T>> toEntity() {
        return ResponseEntity.status(httpStatus != null ? httpStatus : HttpStatus.OK)
                .headers(headers != null ? headers : new HttpHeaders())
                .body(this);
    }
}
