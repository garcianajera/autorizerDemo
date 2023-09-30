package com.demo.validation.service.transaction;

import com.google.inject.Inject;
import com.demo.model.Transaction;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;

import java.util.ArrayList;

public class LimitedTransactionAuthorizationService implements TransactionAuthorizationService {
    // limit validation and creditcard not blocked

    private final AccountService accountService;

    @Inject
    public LimitedTransactionAuthorizationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public AuthorizationResult authorize(Transaction transaction) {

        CardNotActiveValidationStep cardNotActiveValidationStep = new CardNotActiveValidationStep(accountService);
        cardNotActiveValidationStep.linkWith(new AvailableLimitValidationStep(accountService));

        return cardNotActiveValidationStep.verify(transaction, new ArrayList<>());
    }
}
