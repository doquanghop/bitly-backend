package com.urlshortener.core.domain.shortener.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_analytics")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class UrlAnalytic {
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
    private String deviceType;
    private String referrer;
    @Column(name = "accessed_at")
    private LocalDateTime accessedAt;
}
