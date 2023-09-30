package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.TransactionService;
import com.demo.validation.service.AuthorizationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DoubledTransactionValidationStepTest {

    @Test
    void testVerify() {

        TransactionService transactionService = Mockito.mock(TransactionService.class);
        List<Transaction> transactions = Collections.singletonList(new Transaction("merchant", 100, LocalDateTime.now()));
        Mockito.when(transactionService.getTransactions(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(transactions);
        DoubledTransactionValidationStep doubledTransactionValidationStep = new DoubledTransactionValidationStep(transactionService);

        AuthorizationResult authorizationResult = doubledTransactionValidationStep.verify(new Transaction("other merchant", 100, LocalDateTime.now()), new ArrayList<>());

        assertTrue(authorizationResult.isValid());
    }

    @Test
    void testVerifyDoubledTransaction() {

        TransactionService transactionService = Mockito.mock(TransactionService.class);
        List<Transaction> transactions = Collections.singletonList(new Transaction("merchant", 100, LocalDateTime.now()));
        Mockito.when(transactionService.getTransactions(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(transactions);
        DoubledTransactionValidationStep doubledTransactionValidationStep = new DoubledTransactionValidationStep(transactionService);

        AuthorizationResult authorizationResult = doubledTransactionValidationStep.verify(new Transaction("merchant", 100, LocalDateTime.now()), new ArrayList<>());

        assertTrue(authorizationResult.notValid());
        assertEquals(ViolationType.DOUBLED_TRANSACTION.getDisplayString(), authorizationResult.getViolations().get(0));
    }
}
