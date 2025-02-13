package com.urlshortener.core.domain.analytic.dataTransferObject;

import lombok.Data;

@Data
public class OverTimeAnalyticDTO {
    private String accessedAt;
    private long totalClicksAndScans;
}
