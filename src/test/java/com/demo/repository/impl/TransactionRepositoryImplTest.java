package com.demo.repository.impl;

import com.demo.model.Transaction;
import com.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionRepositoryImplTest {

    @Test
    void testSaveTransaction() {
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        LocalDateTime now = LocalDateTime.now();
        String merchant = "merchant";
        int amount = 100;

        Transaction transaction = transactionRepository.save(new Transaction(merchant, amount, now));

        assertNotNull(transaction);
        assertEquals(merchant, transaction.getMerchant());
        assertEquals(amount, transaction.getAmount());
        assertEquals(now, transaction.getTime());

    }

    @Test
    void testFindById() {
        TransactionRepository transactionRepository = new TransactionRepositoryImpl();

        LocalDateTime now = LocalDateTime.now();
        String merchant = "merchant";
        int amount = 100;

        Transaction transaction = transactionRepository.save(new Transaction(merchant, amount, now));

        assertNotNull(transaction);
        assertNotNull(transaction.getId());

        UUID id = transaction.getId();

        Transaction findTransaction = transactionRepository.findById(id);

        assertEquals(id, findTransaction.getId());
        assertEquals(merchant, findTransaction.getMerchant());
        assertEquals(amount, findTransaction.getAmount());
        assertEquals(now, findTransaction.getTime());

    }

}
