package com.urlshortener.core.domain.subscription.service;

import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountSubscriptionResponse;

public interface IAccountSubscriptionService {
    AccountSubscriptionResponse subscribe(String userId, String accountLevelId);
    AccountSubscriptionResponse getSubscriptionByUserId(String userId);
    boolean checkSubscriptionStatus(String userId);
    void cancelSubscription(String userId);
    void renewSubscription(String userId);
}
