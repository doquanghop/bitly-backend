package com.urlshortener.core.domain.subscription.model;

import com.urlshortener.core.infrastucture.utils.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_levels")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountLevel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    @Column(name = "max_urls")
    private String maxUrls;
    @Column(name = "max_storage_days")
    private String maxStorageDays;
    @Column(name = "monthly_price")
    private BigDecimal monthlyPrice;
    @Column(name = "yearly_price")
    private BigDecimal yearlyPrice;
}
