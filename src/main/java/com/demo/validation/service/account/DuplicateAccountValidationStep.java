package com.demo.validation.service.account;

import com.demo.model.Account;
import com.demo.model.ViolationType;
import com.demo.repository.AccountRepository;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class DuplicateAccountValidationStep extends AuthorizationStep<Account> {

    private final AccountRepository accountRepository;

    DuplicateAccountValidationStep(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AuthorizationResult verify(Account toValidate, List<String> violations) {
        // verify if an account already exist
        if (!accountRepository.findAll().isEmpty()) {
            violations.add(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString());
        }
        return checkNext(toValidate, violations);
    }
}
