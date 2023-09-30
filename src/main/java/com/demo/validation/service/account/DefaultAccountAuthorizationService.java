package com.demo.validation.service.account;

import com.google.inject.Inject;
import com.demo.model.Account;
import com.demo.repository.AccountRepository;
import com.demo.validation.service.AuthorizationResult;

import java.util.ArrayList;

public class DefaultAccountAuthorizationService implements AccountAuthorizationService {

    private final AccountRepository accountRepository;

    @Inject
    public DefaultAccountAuthorizationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public AuthorizationResult authorize(Account account) {
        return new DuplicateAccountValidationStep(accountRepository).verify(account, new ArrayList<>());
    }
}
