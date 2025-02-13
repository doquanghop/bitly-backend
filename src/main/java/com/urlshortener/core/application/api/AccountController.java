package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.auth.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.auth.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.auth.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountResponse>> register(@RequestBody RegisterRequest request) {
        var response = authService.register(request);
        return ApiResponse.<AccountResponse>build()
                .withData(response)
                .toEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccountResponse>> login(@RequestBody LoginRequest request) {
        var response = authService.login(request);
        return ApiResponse.<AccountResponse>build()
                .withData(response)
                .toEntity();
    }

    @PatchMapping("/changePassword")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<AccountResponse>> changePassword(@RequestBody ChangePasswordRequest request) {
        var response = authService.changePassword(request);
        return ApiResponse.<AccountResponse>build()
                .withData(response)
                .toEntity();
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse<AccountResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = authService.refreshToken(request);
        return ApiResponse.<AccountResponse>build()
                .withData(response)
                .toEntity();
    }
}
