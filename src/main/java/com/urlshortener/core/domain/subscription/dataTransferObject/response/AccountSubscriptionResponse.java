package com.urlshortener.core.domain.subscription.dataTransferObject.response;

import java.time.LocalDateTime;

public record AccountSubscriptionResponse(
        String id,
        String userId,
        String accountLevelId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String status
) {
}
