package com.urlshortener.core.domain.subscription.repository;

import com.urlshortener.core.domain.subscription.model.AccountSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountSubscriptionRepository extends JpaRepository<AccountSubscription, String> {
}
