package com.urlshortener.core.domain.subscription.dataTransferObject.response;

import java.math.BigDecimal;

public record AccountLevelResponse(
        String id,
        String maxURLs,
        String maxStorageDays,
        BigDecimal monthlyPrice,
        BigDecimal yearlyPrice
) {
}
