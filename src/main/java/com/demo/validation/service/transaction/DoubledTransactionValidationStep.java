package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.TransactionService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.AuthorizationStep;

import java.util.List;

public class DoubledTransactionValidationStep extends AuthorizationStep<Transaction> {

    private final TransactionService transactionService;

    DoubledTransactionValidationStep(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public AuthorizationResult verify(Transaction toValidate, List<String> violations) {
        List<Transaction> transactions = transactionService.getTransactions(toValidate.getTime().minusMinutes(2), toValidate.getTime());

        boolean isTransactionProcessed = transactions.stream()
                .anyMatch(t -> t.getMerchant().equals(toValidate.getMerchant()) && t.getAmount() == toValidate.getAmount());

        if (isTransactionProcessed) {
            violations.add(ViolationType.DOUBLED_TRANSACTION.getDisplayString());
        }

        return checkNext(toValidate, violations);
    }
}
