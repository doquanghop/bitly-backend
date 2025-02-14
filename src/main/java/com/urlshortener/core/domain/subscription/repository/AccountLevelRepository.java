package com.urlshortener.core.domain.subscription.repository;

import com.urlshortener.core.domain.subscription.model.AccountLevel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountLevelRepository extends JpaRepository<AccountLevel, String> {
}
