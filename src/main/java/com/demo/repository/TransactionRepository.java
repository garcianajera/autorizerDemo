package com.demo.repository;

import com.demo.model.Transaction;

import java.util.UUID;

public interface TransactionRepository extends GenericRepository<Transaction, UUID> {
}
