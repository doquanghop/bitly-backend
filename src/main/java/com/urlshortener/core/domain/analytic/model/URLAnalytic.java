package com.urlshortener.core.domain.analytic.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_analytics")
@AllArgsConstructor @NoArgsConstructor @Builder
@Getter @Setter
public class URLAnalytic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "url_id")
    private String urlId;
    @Column(name = "ip_address")
    private String ipAddress;
    private String country;
    private String city;
    @Column(name = "user_agent")
    private String userAgent;
    @Column(name = "device_type")
    private String deviceType;
    private String referrer;
    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;
}
