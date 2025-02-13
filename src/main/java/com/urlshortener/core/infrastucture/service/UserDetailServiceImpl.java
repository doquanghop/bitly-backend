package com.urlshortener.core.infrastucture.service;

import com.urlshortener.core.domain.account.exception.AuthException;
import com.urlshortener.core.domain.account.model.User;
import com.urlshortener.core.domain.account.repository.UserRepository;
import com.urlshortener.core.infrastucture.exception.AppException;
import com.urlshortener.core.infrastucture.security.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new AppException(AuthException.USER_NOT_FOUND));
        return new UserDetail(
                existingUser.getId(),
                existingUser.getRole(),
                existingUser.getEmail(),
                null
        );
    }
}
