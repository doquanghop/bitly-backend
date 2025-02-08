package com.urlshortener.core.domain.shortener.repository;

import com.urlshortener.core.domain.shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, String> {
    Optional<Url> findByShortUrlCode(String shortUrlCode);
}
