package com.demo.validation.service.transaction;

import com.demo.model.Account;
import com.demo.model.Transaction;
import com.demo.model.ViolationType;
import com.demo.service.AccountService;
import com.demo.validation.service.AuthorizationResult;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AvailableLimitValidationStepTest {

    @Test
    void testVerifyValidAccountLimit() {

        AccountService accountService = Mockito.mock(AccountService.class);
        AvailableLimitValidationStep availableLimitValidationStep = new AvailableLimitValidationStep(accountService);
        Mockito.when(accountService.getAccountFromSession()).thenReturn(new Account(true, 100));

        AuthorizationResult authorizationResult = availableLimitValidationStep.verify(new Transaction("merchant", 50, LocalDateTime.now()), new ArrayList<>());

        assertTrue(authorizationResult.isValid());

    }

    @Test
    void testVerifyAccountLimitNotValid() {

        AccountService accountService = Mockito.mock(AccountService.class);
        AvailableLimitValidationStep availableLimitValidationStep = new AvailableLimitValidationStep(accountService);
        Mockito.when(accountService.getAccountFromSession()).thenReturn(new Account(true, 100));

        AuthorizationResult authorizationResult = availableLimitValidationStep.verify(new Transaction("merchant", 500, LocalDateTime.now()), new ArrayList<>());

        assertTrue(authorizationResult.notValid());
        assertEquals(ViolationType.INSUFFICIENT_LIMIT.getDisplayString(), authorizationResult.getViolations().get(0));

    }
}
