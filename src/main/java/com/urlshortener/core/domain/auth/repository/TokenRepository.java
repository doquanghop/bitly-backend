package com.urlshortener.core.domain.auth.repository;

import com.urlshortener.core.domain.auth.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
