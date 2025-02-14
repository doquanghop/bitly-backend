package com.urlshortener.core.domain.account.model;

import com.urlshortener.core.infrastucture.utils.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "avatar_url")
    private String avatarUrl;
    private String role;
    private String email;
    private String password;
}
