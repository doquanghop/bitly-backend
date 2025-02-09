package com.urlshortener.core.domain.auth.repository;

import com.urlshortener.core.domain.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
