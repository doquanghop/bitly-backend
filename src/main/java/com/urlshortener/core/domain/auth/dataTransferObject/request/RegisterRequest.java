package com.urlshortener.core.domain.auth.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
}
