package com.demo.validation.service.transaction;

import com.demo.model.Account;
import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountNotInitializedValidationStepTest {

    @Test
    void testVerifyAccountInitialized() {

        AccountService accountService = Mockito.mock(AccountService.class);

        AccountNotInitializedValidationStep accountNotInitializedValidationStep = new AccountNotInitializedValidationStep(accountService);
        Mockito.when(accountService.getAccountFromSession()).thenReturn(new Account());
        AuthorizationResult authorizationResult = accountNotInitializedValidationStep.verify(new Transaction(), new ArrayList<>());

        assertTrue(authorizationResult.isValid());
    }

    @Test
    void testVerifyAccountNotInitialized() {

        AccountService applicationSessionService = Mockito.mock(AccountService.class);

        AccountNotInitializedValidationStep accountNotInitializedValidationStep = new AccountNotInitializedValidationStep(applicationSessionService);
        AuthorizationResult authorizationResult = accountNotInitializedValidationStep.verify(new Transaction(), new ArrayList<>());

        assertTrue(authorizationResult.notValid());
        assertEquals(ViolationType.ACCOUNT_NOT_INITIALIZED.getDisplayString(), authorizationResult.getViolations().get(0));

    }
}
