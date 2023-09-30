package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class AvailableLimitValidationStep extends AuthorizationStep<Transaction> {

    private final AccountService accountService;

    AvailableLimitValidationStep(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AuthorizationResult verify(Transaction toValidate, List<String> violations) {

        if (toValidate.getAmount() > accountService.getAccountFromSession().getAvailableLimit()) {
            violations.add(ViolationType.INSUFFICIENT_LIMIT.getDisplayString());
        }

        return checkNext(toValidate, violations);
    }
}
