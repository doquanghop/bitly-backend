package com.urlshortener.core.domain.account.repository;

import com.urlshortener.core.domain.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
}
