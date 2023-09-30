package com.demo.service;

import com.demo.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Transaction saveTransaction(Transaction transaction);

    List<Transaction> getTransactions(LocalDateTime from, LocalDateTime to);
}
