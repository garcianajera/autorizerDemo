package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.TransactionService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class FrequencyIntervalValidationStep extends AuthorizationStep<Transaction> {

    private final TransactionService transactionService;

    FrequencyIntervalValidationStep(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public AuthorizationResult verify(Transaction toValidate, List<String> violations) {

        if (transactionService.getTransactions(toValidate.getTime().minusMinutes(2), toValidate.getTime()).size() >= 3) {
            violations.add(ViolationType.HIGH_FREQUENCY_SMALL_INTERVAL.getDisplayString());
        }

        return checkNext(toValidate, violations);
    }
}
