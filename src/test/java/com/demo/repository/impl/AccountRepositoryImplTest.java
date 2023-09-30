package com.demo.repository.impl;

import com.demo.model.Account;
import com.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountRepositoryImplTest {

    @Test
    void testSaveAccount() {

        AccountRepository accountRepository = new AccountRepositoryImpl();
        Account account = new Account(true, 10);

        assertEquals(0, accountRepository.findAll().size());

        Account accountFromRepository = accountRepository.save(account);

        assertEquals(1, accountRepository.findAll().size());

        assertNotNull(accountFromRepository);
        assertNotNull(accountFromRepository.getId());

        assertEquals(10, accountFromRepository.getAvailableLimit());

    }

    @Test
    void testFindById() {
        AccountRepository accountRepository = new AccountRepositoryImpl();
        Account account = new Account(true, 20);

        assertEquals(0, accountRepository.findAll().size());

        Account accountFromRepository = accountRepository.save(account);

        assertNotNull(accountFromRepository);
        assertNotNull(accountFromRepository.getId());

        Account accountFromRepositoryById = accountRepository.findById(account.getId());

        assertEquals(accountFromRepository, accountFromRepositoryById);

    }
}
