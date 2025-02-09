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

@Entity
@Table(name = "users")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class User extends BaseEntity {
    @Id
    private String id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "oauth2_provider_name")
    private String oauth2ProviderName;
    @Column(name = "oauth2_provider_id")
    private String oauth2ProviderId;
    private String email;
    private String password;
}
