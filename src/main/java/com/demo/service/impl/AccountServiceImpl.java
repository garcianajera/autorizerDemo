package com.demo.service.impl;

import com.google.inject.Inject;
import com.demo.model.Account;
import com.demo.repository.AccountRepository;
import com.demo.service.AccountService;
import com.demo.service.ApplicationSessionService;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ApplicationSessionService applicationSessionService;

    @Inject
    public AccountServiceImpl(AccountRepository accountRepository, ApplicationSessionService applicationSessionService) {
        this.accountRepository = accountRepository;
        this.applicationSessionService = applicationSessionService;
    }


    @Override
    public Account saveAccount(Account account) {
        return this.accountRepository.save(account);
    }

    @Override
    public Account getAccountFromSession() {
        return this.accountRepository.findById(applicationSessionService.getAccountFromSession());
    }

    @Override
    public void setAccountInSession(Account account) {
        //save account and store id in session
        saveAccount(account);
        applicationSessionService.setAccountInSession(account.getId());
    }
}
