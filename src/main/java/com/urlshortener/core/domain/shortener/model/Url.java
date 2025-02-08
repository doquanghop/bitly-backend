package com.urlshortener.core.domain.shortener.model;

import com.urlshortener.core.domain.utils.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "urls")
@NoArgsConstructor @AllArgsConstructor @Getter @Setter @Builder
public class Url extends BaseEntity {
    @Id
    private String id;
    @Column(name = "original_url")
    private String originalUrl;
    @Column(name = "short_url")
    private String shortUrl;
}
