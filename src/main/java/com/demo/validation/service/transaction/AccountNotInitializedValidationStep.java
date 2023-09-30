package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class AccountNotInitializedValidationStep extends AuthorizationStep<Transaction> {
    private final AccountService accountService;

    AccountNotInitializedValidationStep(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public AuthorizationResult verify(Transaction toValidate, List<String> violations) {
        // verify if an account already exist
        if (accountService.getAccountFromSession() == null) {
            violations.add(ViolationType.ACCOUNT_NOT_INITIALIZED.getDisplayString());
            // we have to return instead of continue with next validations because
            // transaction can not be processed if there is no account in session
            return AuthorizationResult.invalid(violations);
        }
        return checkNext(toValidate, violations);
    }
}
