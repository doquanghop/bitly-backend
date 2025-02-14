package com.urlshortener.core.application.api;

import com.urlshortener.core.application.dataTransferObject.ApiResponse;
import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountSubscriptionResponse;
import com.urlshortener.core.domain.subscription.service.IAccountSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/subscriptions")
@RequiredArgsConstructor
public class AccountSubscriptionController {
    private final IAccountSubscriptionService subscriptionService;

    @PostMapping("/{userId}/subscribe/{accountLevelId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<AccountSubscriptionResponse>> subscribe(
            @PathVariable String userId,
            @PathVariable String accountLevelId) {
        var response = subscriptionService.subscribe(userId, accountLevelId);
        return ApiResponse.<AccountSubscriptionResponse>build().withData(response).toEntity();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<AccountSubscriptionResponse>> getSubscriptionByUserId(@PathVariable String userId) {
        var response = subscriptionService.getSubscriptionByUserId(userId);
        return ApiResponse.<AccountSubscriptionResponse>build().withData(response).toEntity();
    }

    @GetMapping("/{userId}/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Boolean>> checkSubscriptionStatus(@PathVariable String userId) {
        var status = subscriptionService.checkSubscriptionStatus(userId);
        return ApiResponse.<Boolean>build().withData(status).toEntity();
    }

    @DeleteMapping("/{userId}/cancel")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Void>> cancelSubscription(@PathVariable String userId) {
        subscriptionService.cancelSubscription(userId);
        return ApiResponse.<Void>build().toEntity();
    }

    @PostMapping("/{userId}/renew")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<ApiResponse<Void>> renewSubscription(@PathVariable String userId) {
        subscriptionService.renewSubscription(userId);
        return ApiResponse.<Void>build().toEntity();
    }
}