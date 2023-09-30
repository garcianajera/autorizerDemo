package com.demo.service.impl;

import com.demo.model.Account;
import com.demo.repository.AccountRepository;
import com.demo.service.AccountService;
import com.demo.service.ApplicationSessionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountServiceImplTest {

    @Test
    void testSaveAccount() {
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        Account account = new Account(true, 100);
        UUID id = UUID.randomUUID();
        account.setId(id);
        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(account);
        AccountService accountService = new AccountServiceImpl(accountRepository, null);

        Account accountFromService = accountService.saveAccount(account);

        assertEquals(id, accountFromService.getId());
        assertEquals(100, accountFromService.getAvailableLimit());
        assertTrue(accountFromService.hasActiveCard());

    }

    @Test
    void testGetAccountFromSession() {
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        ApplicationSessionService applicationSessionService = Mockito.mock(ApplicationSessionService.class);

        Account account = new Account(true, 100);
        UUID id = UUID.randomUUID();
        account.setId(id);
        Mockito.when(applicationSessionService.getAccountFromSession()).thenReturn(id);
        Mockito.when(accountRepository.findById(id)).thenReturn(account);
        AccountService accountService = new AccountServiceImpl(accountRepository, applicationSessionService);

        Account accountFromService = accountService.getAccountFromSession();

        assertEquals(id, accountFromService.getId());
        assertEquals(100, accountFromService.getAvailableLimit());
        assertTrue(accountFromService.hasActiveCard());

    }

    @Test
    void testSetAccountInSession() {
        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        ApplicationSessionService applicationSessionService = Mockito.mock(ApplicationSessionService.class);

        Account account = new Account(true, 100);
        UUID id = UUID.randomUUID();
        account.setId(id);
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Mockito.doNothing().when(applicationSessionService).setAccountInSession(id);
        AccountService accountService = new AccountServiceImpl(accountRepository, applicationSessionService);

        accountService.setAccountInSession(account);
        Mockito.verify(accountRepository, Mockito.times(1)).save(account);
        Mockito.verify(applicationSessionService, Mockito.times(1)).setAccountInSession(id);

    }

}
