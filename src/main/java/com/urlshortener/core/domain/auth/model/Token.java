package com.urlshortener.core.domain.auth.model;

import com.urlshortener.core.infrastucture.utils.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_tokens")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Token extends BaseEntity {
    @Id
    private String id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "access_token_expiry")
    private LocalDateTime accessTokenExpiry;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "refresh_token_expiry")
    private LocalDateTime refreshTokenExpiry;
}
