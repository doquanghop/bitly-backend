package com.urlshortener.core.domain.subscription.service;

import com.urlshortener.core.domain.subscription.dataTransferObject.request.CreationAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.request.UpdateAccountLevelRequest;
import com.urlshortener.core.domain.subscription.dataTransferObject.response.AccountLevelResponse;

import java.util.List;

public interface IAccountLevelService {
    AccountLevelResponse createAccountLevel(CreationAccountLevelRequest request);
    AccountLevelResponse updateAccountLevel(String id, UpdateAccountLevelRequest request);
    void deleteAccountLevel(String id);
    List<AccountLevelResponse> getAllAccountLevels();
    AccountLevelResponse getAccountLevelById(String id);
}
