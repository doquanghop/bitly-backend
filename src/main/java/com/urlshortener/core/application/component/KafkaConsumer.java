package com.urlshortener.core.application.component;

import com.urlshortener.core.domain.shortener.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.shortener.service.IUrlAnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final IUrlAnalyticService urlAnalyticService;

    @KafkaListener(topics = "${kafka.topic.url.analytic.visited}")
    public void saveUrlAnalytics(SaveUrlAnalyticRequest request) {
        urlAnalyticService.saveUrlAnalytic(request);
    }
}
