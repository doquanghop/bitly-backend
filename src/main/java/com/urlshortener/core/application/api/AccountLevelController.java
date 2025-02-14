package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.subscription.dataTransferObject.request.CreationAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.request.UpdateAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountLevelResponse;
import com.urlshortener.core.domain.subscription.service.IAccountLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/account-level")
@RequiredArgsConstructor
public class AccountLevelController {
    private final IAccountLevelService accountLevelService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AccountLevelResponse>> createAccountLevel(
            @RequestBody CreationAccountLevelRequest request) {
        var response = accountLevelService.createAccountLevel(request);
        return ApiResponse.<AccountLevelResponse>build().withData(response).toEntity();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<AccountLevelResponse>> updateAccountLevel(
            @PathVariable String id,
            @RequestBody UpdateAccountLevelRequest request) {
        var response = accountLevelService.updateAccountLevel(id, request);
        return ApiResponse.<AccountLevelResponse>build().withData(response).toEntity();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteAccountLevel(@PathVariable String id) {
        accountLevelService.deleteAccountLevel(id);
        return ApiResponse.<Void>build().toEntity();
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<AccountLevelResponse>>> getAllAccountLevels() {
        var response = accountLevelService.getAllAccountLevels();
        return ApiResponse.<List<AccountLevelResponse>>build().withData(response).toEntity();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountLevelResponse>> getAccountLevelById(@PathVariable String id) {
        var response = accountLevelService.getAccountLevelById(id);
        return ApiResponse.<AccountLevelResponse>build().withData(response).toEntity();
    }
}
