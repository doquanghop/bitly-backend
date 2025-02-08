package com.urlshortener.core.domain.auth.service;

import com.urlshortener.core.domain.auth.model.Token;
import com.urlshortener.core.domain.auth.model.User;

public interface ITokenService {
    Token create(User user);
}
