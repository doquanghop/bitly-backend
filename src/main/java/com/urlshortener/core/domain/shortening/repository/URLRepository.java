package com.urlshortener.core.domain.shortening.repository;

import com.urlshortener.core.domain.shortening.model.URL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface URLRepository extends JpaRepository<URL, String> {
    Optional<URL> findByShortUrlCode(String shortUrlCode);
}
