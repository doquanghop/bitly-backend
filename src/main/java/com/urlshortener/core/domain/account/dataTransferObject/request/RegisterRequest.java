package com.urlshortener.core.domain.account.dataTransferObject.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
}
