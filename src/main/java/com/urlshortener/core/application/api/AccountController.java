package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.account.dataTransferObject.request.ChangePasswordRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.LoginRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RefreshTokenRequest;
import com.urlshortener.core.domain.account.dataTransferObject.request.RegisterRequest;
import com.urlshortener.core.domain.account.dataTransferObject.response.AccountResponse;
import com.urlshortener.core.domain.account.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/account")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService authService;

    @GetMapping
    public String getAccounts() {
        return "hi hello";
    }

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
