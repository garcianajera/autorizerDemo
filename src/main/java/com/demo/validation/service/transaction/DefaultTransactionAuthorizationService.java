package com.demo.validation.service.transaction;

import com.google.inject.Inject;
import com.demo.model.Transaction;
import com.demo.service.AccountService;
import com.demo.service.TransactionService;
import com.demo.validation.service.AuthorizationResult;

import java.util.ArrayList;

public class DefaultTransactionAuthorizationService implements TransactionAuthorizationService {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Inject
    public DefaultTransactionAuthorizationService(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Override
    public AuthorizationResult authorize(Transaction transaction) {
        AccountNotInitializedValidationStep accountNotInitializedValidationStep = new AccountNotInitializedValidationStep(accountService);
        accountNotInitializedValidationStep
                .linkWith(new CardNotActiveValidationStep(accountService))
                .linkWith(new AvailableLimitValidationStep(accountService))
                .linkWith(new FrequencyIntervalValidationStep(transactionService))
                .linkWith(new DoubledTransactionValidationStep(transactionService));
        return accountNotInitializedValidationStep.verify(transaction, new ArrayList<>());
    }
}
