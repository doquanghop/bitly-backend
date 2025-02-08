package com.urlshortener.core.domain.shortener.repository;

import com.urlshortener.core.domain.shortener.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url, String> {
    Url findByShortUrl(String shortUrl);
}
