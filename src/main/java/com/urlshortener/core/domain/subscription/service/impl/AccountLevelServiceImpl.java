package com.urlshortener.core.domain.subscription.service.impl;

import com.urlshortener.core.domain.subscription.dataTransferObject.request.CreationAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.request.UpdateAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountLevelResponse;
import com.urlshortener.core.domain.subscription.repository.AccountLevelRepository;
import com.urlshortener.core.domain.subscription.service.IAccountLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountLevelServiceImpl implements IAccountLevelService {
    private final AccountLevelRepository accountLevelRepository;

    @Override
    public AccountLevelResponse createAccountLevel(CreationAccountLevelRequest request) {
        return null;
    }

    @Override
    public AccountLevelResponse updateAccountLevel(String id, UpdateAccountLevelRequest request) {
        return null;
    }

    @Override
    public void deleteAccountLevel(String id) {

    }

    @Override
    public List<AccountLevelResponse> getAllAccountLevels() {
        return List.of();
    }

    @Override
    public AccountLevelResponse getAccountLevelById(String id) {
        return null;
    }
}
