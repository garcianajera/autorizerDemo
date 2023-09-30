package com.demo.validation.service.transaction;

import com.demo.model.Account;
import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class CardNotActiveValidationStep extends AuthorizationStep<Transaction> {

    private final AccountService accountService;

    CardNotActiveValidationStep(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AuthorizationResult verify(Transaction toValidate, List<String> violations) {
        Account account = accountService.getAccountFromSession();

        if (!account.hasActiveCard()) {
            violations.add(ViolationType.CARD_NOT_ACTIVE.getDisplayString());
            // we have to return instead of continue with next validations because
            // transaction can not be processed if account is not active
            return AuthorizationResult.invalid(violations);
        }
        return checkNext(toValidate, violations);
    }
}
