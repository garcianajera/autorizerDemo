package com.demo.service.impl;

import com.demo.model.Transaction;
import com.demo.repository.TransactionRepository;
import com.demo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionServiceImplTest {

    @Test
    void testSaveTransaction() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction("merchant", 100, now);
        UUID id = UUID.randomUUID();
        transaction.setId(id);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(transaction);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);

        Transaction transactionFro9mService = transactionService.saveTransaction(transaction);

        assertEquals(id, transactionFro9mService.getId());
        assertEquals(100, transactionFro9mService.getAmount());
        assertEquals(now, transactionFro9mService.getTime());
    }

    @Test
    void testGetTransactions() {
        TransactionRepository transactionRepository = Mockito.mock(TransactionRepository.class);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMinus1Minute = now.minusMinutes(1);
        LocalDateTime nowMinus30Sec = now.minusSeconds(30);
        LocalDateTime nowMinus2Minutes = now.minusMinutes(2);
        LocalDateTime nowMinus20Minutes = now.minusMinutes(20);
        LocalDateTime nowPlus2Minutes = now.plusMinutes(2);

        Transaction transaction = new Transaction("merchant", 100, now);
        Transaction transaction1 = new Transaction("merchant", 100, nowMinus1Minute);
        Transaction transaction2 = new Transaction("merchant", 100, nowMinus30Sec);
        Transaction transaction3 = new Transaction("merchant", 100, nowMinus2Minutes);
        Transaction transaction4 = new Transaction("merchant", 100, nowMinus20Minutes);
        Transaction transaction5 = new Transaction("merchant", 100, nowPlus2Minutes);

        Collection<Transaction> transactions = Arrays.asList(transaction, transaction1, transaction2, transaction3, transaction4, transaction5);
        Mockito.when(transactionRepository.findAll()).thenReturn(transactions);
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository);
        List<Transaction> transactionsFromService = transactionService.getTransactions(now.minusMinutes(2), now);
        assertEquals(4, transactionsFromService.size());

    }


}
