package com.urlshortener.core.application.security;

import com.urlshortener.core.domain.account.service.IAccountService;
import com.urlshortener.core.infrastucture.exception.AppException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final IAccountService authService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            try {
                UserDetails userDetail = authService.authenticate(accessToken);
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetail,
                                null,
                                userDetail.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (AppException e) {
                throw new ServletException(e);
            }
        }
        filterChain.doFilter(request, response);
    }
}
