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

class CardNotActiveValidationStepTest {

    @Test
    void testVerifyActiveCard() {

        AccountService accountService = Mockito.mock(AccountService.class);
        CardNotActiveValidationStep cardNotActiveValidationStep = new CardNotActiveValidationStep(accountService);

        Mockito.when(accountService.getAccountFromSession()).thenReturn(new Account(true, 123));
        AuthorizationResult authorizationResult = cardNotActiveValidationStep.verify(new Transaction(), new ArrayList<>());

        assertTrue(authorizationResult.isValid());

    }

    @Test
    void testVerifyNotActiveCard() {

        AccountService accountService = Mockito.mock(AccountService.class);
        CardNotActiveValidationStep cardNotActiveValidationStep = new CardNotActiveValidationStep(accountService);

        Mockito.when(accountService.getAccountFromSession()).thenReturn(new Account(false, 123));
        AuthorizationResult authorizationResult = cardNotActiveValidationStep.verify(new Transaction(), new ArrayList<>());

        assertTrue(authorizationResult.notValid());
        assertEquals(ViolationType.CARD_NOT_ACTIVE.getDisplayString(), authorizationResult.getViolations().get(0));

    }
}
