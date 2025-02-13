package com.urlshortener.core.domain.shortening.service;

import com.urlshortener.core.domain.shortening.component.EncoderService;
import com.urlshortener.core.domain.shortening.dataTransferObject.request.CreationShortenUrlRequest;
import com.urlshortener.core.domain.shortening.dataTransferObject.response.ShortenUrlResponse;
import com.urlshortener.core.domain.shortening.model.URL;
import com.urlshortener.core.domain.shortening.repository.URLRepository;
import com.urlshortener.core.domain.shortening.service.impl.URLShortenerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@SpringBootTest
class URLShortenerTest {
    @Mock
    private EncoderService urlEncoder;
    @Mock
    private URLRepository urlRepository;

    @InjectMocks
    private URLShortenerServiceImpl urlShortenerService;

    private String originalUrl;
    private String shortUrlCode;
    @Value("${client.url}")
    private String clientUrl;

    @BeforeEach
    void init() {
        originalUrl = "https://example.com";
        shortUrlCode = "abcd1234";
        ReflectionTestUtils.setField(urlShortenerService, "clientUrl", clientUrl);
    }

    @Test
    void testCreateUrlShortener() {
        CreationShortenUrlRequest request = new CreationShortenUrlRequest(originalUrl);
        when(urlEncoder.encode(originalUrl)).thenReturn(shortUrlCode);
        URL url = URL.builder()
                .originalUrl(originalUrl)
                .shortUrlCode(shortUrlCode)
                .build();
        when(urlRepository.save(any(URL.class))).thenReturn(url);
        ShortenUrlResponse response = urlShortenerService.shortenUrl(request);

        assertThat(response).isNotNull();
        assertThat(response.getOriginalUrl()).isEqualTo(originalUrl);
        assertThat(response.getShortUrl()).isEqualTo(clientUrl + "/" + shortUrlCode);

        verify(urlEncoder, times(1)).encode(originalUrl);
        verify(urlRepository, times(1)).save(any(URL.class));
    }
}
