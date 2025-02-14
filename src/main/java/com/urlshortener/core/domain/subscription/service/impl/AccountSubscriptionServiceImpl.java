package com.urlshortener.core.domain.subscription.service.impl;

import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountSubscriptionResponse;
import com.urlshortener.core.domain.subscription.repository.AccountSubscriptionRepository;
import com.urlshortener.core.domain.subscription.service.IAccountSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountSubscriptionServiceImpl implements IAccountSubscriptionService {
    private final AccountSubscriptionRepository accountSubscriptionRepository;

    @Override
    public AccountSubscriptionResponse subscribe(String userId, String accountLevelId) {
        return null;
    }

    @Override
    public AccountSubscriptionResponse getSubscriptionByUserId(String userId) {
        return null;
    }

    @Override
    public boolean checkSubscriptionStatus(String userId) {
        return false;
    }

    @Override
    public void cancelSubscription(String userId) {

    }

    @Override
    public void renewSubscription(String userId) {

    }
}
