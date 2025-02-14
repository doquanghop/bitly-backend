package com.urlshortener.core.domain.subscription.model;

import com.urlshortener.core.domain.subscription.constant.UserSubscriptionStatus;
import com.urlshortener.core.infrastucture.utils.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_subscriptions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountSubscription extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    @Column(name = "account_level_id")
    private String accountLevelId;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private UserSubscriptionStatus status;
}
