package com.demo.validation.service.account;

import com.demo.model.Account;
import com.demo.model.ViolationType;
import com.demo.repository.AccountRepository;
import com.demo.validation.service.AuthorizationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DuplicateAccountValidationStepTest {

    @Test
    void testVerifyAuthorizationResultIsValid() {

        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        Mockito.when(accountRepository.findAll()).thenReturn(Collections.emptyList());

        DuplicateAccountValidationStep duplicateAccountValidationStep = new DuplicateAccountValidationStep(accountRepository);

        Account account = new Account(true, 100);
        AuthorizationResult authorizationResult = duplicateAccountValidationStep.verify(account, new ArrayList<>());

        assertTrue(authorizationResult.isValid());
        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
    }

    @Test
    void testVerifyAuthorizationResultNotValid() {

        AccountRepository accountRepository = Mockito.mock(AccountRepository.class);
        Mockito.when(accountRepository.findAll()).thenReturn(Collections.singletonList(new Account(true, 100)));

        DuplicateAccountValidationStep duplicateAccountValidationStep = new DuplicateAccountValidationStep(accountRepository);

        Account account = new Account(true, 100);
        AuthorizationResult authorizationResult = duplicateAccountValidationStep.verify(account, new ArrayList<>());

        assertFalse(authorizationResult.isValid());
        Mockito.verify(accountRepository, Mockito.times(1)).findAll();
        assertEquals(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString(), authorizationResult.getViolations().get(0));

    }
}
