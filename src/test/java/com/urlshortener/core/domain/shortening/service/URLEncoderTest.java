package com.urlshortener.core.domain.shortening.service;

import com.urlshortener.core.domain.shortening.component.EncoderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class URLEncoderTest {
    @Autowired
    private EncoderService urlEncoder;

    @Value("${url.short.max-size}")
    private int maxShortUrlSize;

    @Value("${url.short.min-size}")
    private int minShortUrlSize;

    private String longUrl;

    @BeforeEach
    void init() {
        longUrl = "https://example.com/long-url";
    }

    @Test
    void testEncode_ReturnsCorrectLength() {
        String shortUrl = urlEncoder.encode(longUrl);

        assertThat(shortUrl).hasSizeBetween(minShortUrlSize, maxShortUrlSize);
    }

    @Test
    void testEncode_ReturnsDifferentResultsForMultipleCalls() {
        String shortUrl1 = urlEncoder.encode(longUrl);
        String shortUrl2 = urlEncoder.encode(longUrl);

        assertThat(shortUrl1).isNotEqualTo(shortUrl2);
    }
}