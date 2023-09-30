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

class FrequencyIntervalValidationStepTest {

    @Test
    void testVerify() {

        TransactionService transactionService = Mockito.mock(TransactionService.class);
        List<Transaction> transactions = Collections.singletonList(new Transaction("merchant", 100, LocalDateTime.now()));
        Mockito.when(transactionService.getTransactions(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(transactions);
        FrequencyIntervalValidationStep frequencyIntervalValidationStep = new FrequencyIntervalValidationStep(transactionService);

        AuthorizationResult authorizationResult = frequencyIntervalValidationStep.verify(new Transaction("other merchant", 100, LocalDateTime.now()), new ArrayList<>());

        assertTrue(authorizationResult.isValid());
    }

    @Test
    void testVerifyHighFrequencyTransaction() {

        TransactionService transactionService = Mockito.mock(TransactionService.class);
        LocalDateTime now = LocalDateTime.now();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("merchant1", 100, now.minusMinutes(2)));
        transactions.add(new Transaction("merchant2", 100, now.minusMinutes(1)));
        transactions.add(new Transaction("merchant3", 100, now.minusMinutes(1)));
        Mockito.when(transactionService.getTransactions(Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class))).thenReturn(transactions);

        FrequencyIntervalValidationStep frequencyIntervalValidationStep = new FrequencyIntervalValidationStep(transactionService);

        AuthorizationResult authorizationResult = frequencyIntervalValidationStep.verify(new Transaction("merchant", 100, now), new ArrayList<>());

        assertTrue(authorizationResult.notValid());
        assertEquals(ViolationType.HIGH_FREQUENCY_SMALL_INTERVAL.getDisplayString(), authorizationResult.getViolations().get(0));
    }
}
