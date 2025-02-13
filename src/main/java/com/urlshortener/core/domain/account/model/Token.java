package com.urlshortener.core.domain.account.model;

import com.urlshortener.core.infrastucture.utils.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_tokens")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Token extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "access_token")
    private String accessToken;
    @Column(name = "access_token_expiry")
    private Date accessTokenExpiry;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "refresh_token_expiry")
    private Date refreshTokenExpiry;
}
