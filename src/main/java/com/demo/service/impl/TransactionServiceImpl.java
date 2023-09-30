package com.demo.service.impl;

import com.google.inject.Inject;
import com.demo.model.Transaction;
import com.demo.repository.TransactionRepository;
import com.demo.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Inject
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactions(final LocalDateTime from, final LocalDateTime to) {
        //inclusive range from to
        return transactionRepository.findAll()
                .stream()
                .filter(t -> (t.getTime().isAfter(from) || t.getTime().equals(from)) && (t.getTime().isBefore(to) || t.getTime().equals(to)))
                .collect(Collectors.toList());
    }
}
