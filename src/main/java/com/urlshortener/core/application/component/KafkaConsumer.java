package com.urlshortener.core.application.component;

import com.urlshortener.core.domain.analytic.dataTransferObject.request.SaveUrlAnalyticRequest;
import com.urlshortener.core.domain.analytic.service.IURLAnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
    private final IURLAnalyticService urlAnalyticService;

    @KafkaListener(topics = "${kafka.topic.url.analytic.visited}", groupId = "url-analytic")
    public void saveUrlAnalytics(SaveUrlAnalyticRequest request) {
        urlAnalyticService.saveUrlAnalytic(request);
    }
}
