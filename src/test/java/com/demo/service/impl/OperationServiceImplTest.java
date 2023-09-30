package com.demo.service.impl;

import com.demo.model.*;
import com.demo.service.AccountService;
import com.demo.service.ApplicationSessionService;
import com.demo.service.TransactionService;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.account.AccountAuthorizationService;
import com.demo.validation.service.transaction.TransactionAuthorizationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OperationServiceImplTest {

    @Test
    void testParseInput() {
        OperationServiceImpl operationService = new OperationServiceImpl(null, null, null, null, null);

        List<String> input = new ArrayList<>();
        input.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        input.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\"}}");
        input.add("{\"transaction\": {\"merchant\": \"Habbib's\", \"amount\": 90, \"time\": \"2019-02-13T11:00:00.000Z\"}}");

        List<OperationRequest> operationRequests = operationService.parseInput(input);

        assertNotNull(operationRequests);
        assertEquals(3, operationRequests.size());
        assertEquals(100, operationRequests.get(0).getAccount().getAvailableLimit());
        assertTrue(operationRequests.get(0).getAccount().hasActiveCard());

        assertEquals("Burger King", operationRequests.get(1).getTransaction().getMerchant());
        assertEquals(LocalDateTime.parse("2019-02-13T10:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME), operationRequests.get(1).getTransaction().getTime());

        assertEquals("Habbib's", operationRequests.get(2).getTransaction().getMerchant());
        assertEquals(LocalDateTime.parse("2019-02-13T11:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME), operationRequests.get(2).getTransaction().getTime());

    }


    @Test
    void parseInputWithAllowList(){
        OperationServiceImpl operationService = new OperationServiceImpl(null, null, null, null, null);

        List<String> input = new ArrayList<>();
        input.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        input.add("{\"allow-list\": {\"active\": true}}");
        input.add("{\"allow-list\": {\"active\": false}}");

        List<OperationRequest> operationServiceResponse = operationService.parseInput(input);
        assertTrue(operationServiceResponse.get(1).getAllowList().isActive());
        assertFalse(operationServiceResponse.get(2).getAllowList().isActive());

    }

    @Test
    void testProcessTransaction() {

        TransactionService transactionService = Mockito.mock(TransactionService.class);
        TransactionAuthorizationService transactionAuthorizationService = Mockito.mock(TransactionAuthorizationService.class);
        ApplicationSessionService applicationSessionService = Mockito.mock(ApplicationSessionService.class);
        AccountService accountService = Mockito.mock(AccountService.class);

        AuthorizationResult authorizationResult = new AuthorizationResult(true, null);
        Mockito.when(transactionAuthorizationService.authorize(Mockito.any(Transaction.class))).thenReturn(authorizationResult);

        UUID accountId = UUID.randomUUID();
        Mockito.when(applicationSessionService.getAccountFromSession()).thenReturn(accountId);

        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction("merchant", 50, now);
        Account account = new Account(true, 100);
        Mockito.when(transactionService.saveTransaction(transaction)).thenReturn(transaction);
        Mockito.when(accountService.getAccountFromSession()).thenReturn(account);
        Mockito.when(accountService.saveAccount(account)).thenReturn(account);

        assertEquals(100, account.getAvailableLimit());
        OperationServiceImpl operationService = new OperationServiceImpl(accountService, transactionService, null, transactionAuthorizationService, null);

        OperationResponse operationResponse = operationService.processTransaction(transaction, transactionAuthorizationService);
        assertNotNull(operationResponse.getAccount());
        assertEquals(50, operationResponse.getAccount().getAvailableLimit());

        Mockito.verify(transactionService, Mockito.times(1)).saveTransaction(transaction);
        Mockito.verify(accountService, Mockito.times(1)).saveAccount(account);
    }

    @Test
    void testProcessTransactionAuthorizationInvalid() {

        TransactionAuthorizationService transactionAuthorizationService = Mockito.mock(TransactionAuthorizationService.class);
        ApplicationSessionService applicationSessionService = Mockito.mock(ApplicationSessionService.class);
        AccountService accountService = Mockito.mock(AccountService.class);

        List<String> violations = Collections.singletonList(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString());
        AuthorizationResult authorizationResult = new AuthorizationResult(false, violations);
        Mockito.when(transactionAuthorizationService.authorize(Mockito.any(Transaction.class))).thenReturn(authorizationResult);

        UUID accountId = UUID.randomUUID();
        Mockito.when(applicationSessionService.getAccountFromSession()).thenReturn(accountId);

        Account account = new Account(true, 100);
        Mockito.when(accountService.getAccountFromSession()).thenReturn(account);

        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction("merchant", 50, now);
        OperationServiceImpl operationService = new OperationServiceImpl(accountService, null, null, transactionAuthorizationService, null);

        assertEquals(100, account.getAvailableLimit());
        OperationResponse operationResponse = operationService.processTransaction(transaction, transactionAuthorizationService);
        assertNotNull(operationResponse.getAccount());
        assertEquals(100, operationResponse.getAccount().getAvailableLimit());
        assertEquals(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString(), operationResponse.getViolations().get(0));

    }


    @Test
    void testProcessAccount() {
        AccountService accountService = Mockito.mock(AccountService.class);
        AccountAuthorizationService accountAuthorization = Mockito.mock(AccountAuthorizationService.class);

        AuthorizationResult authorizationResult = new AuthorizationResult(true, null);
        Mockito.when(accountAuthorization.authorize(Mockito.any(Account.class))).thenReturn(authorizationResult);
        UUID accountId = UUID.randomUUID();

        OperationServiceImpl operationService = new OperationServiceImpl(accountService, null, accountAuthorization, null, null);

        Account account = new Account(true, 100);
        account.setId(accountId);

        Mockito.doNothing().when(accountService).setAccountInSession(account);

        OperationResponse operationResponse = operationService.processAccount(account);

        assertEquals(account.getAvailableLimit(), operationResponse.getAccount().getAvailableLimit());
        Mockito.verify(accountService, Mockito.times(1)).setAccountInSession(account);
    }

    @Test
    void testProcessAccountAuthorizationInvalid() {
        AccountAuthorizationService accountAuthorization = Mockito.mock(AccountAuthorizationService.class);

        List<String> violations = Collections.singletonList(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString());
        AuthorizationResult authorizationResult = new AuthorizationResult(false, violations);

        Mockito.when(accountAuthorization.authorize(Mockito.any(Account.class))).thenReturn(authorizationResult);

        OperationServiceImpl operationService = new OperationServiceImpl(null, null, accountAuthorization, null, null);

        Account account = new Account(true, 100);
        OperationResponse operationResponse = operationService.processAccount(account);

        assertTrue(operationResponse.getAccount().hasActiveCard());
        assertEquals(100, operationResponse.getAccount().getAvailableLimit());
        assertEquals(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString(), operationResponse.getViolations().get(0));
    }

    @Test
    void testProcessOperationsAccount() {
        OperationServiceImpl operationService = new OperationServiceImpl(null, null, null, null, null) {
            @Override
            public OperationResponse processAccount(Account account) {
                assertEquals(100, account.getAvailableLimit());
                assertTrue(account.hasActiveCard());
                return new OperationResponse(new AccountResponse(account), null);
            }

            @Override
            public OperationResponse processTransaction(Transaction transaction, TransactionAuthorizationService authorizationService) {
                assertEquals(50, transaction.getAmount());
                assertEquals("merchant", transaction.getMerchant());
                return new OperationResponse(new AccountResponse(new Account(true, 50)), null);
            }
        };

        List<OperationRequest> operationRequests = new ArrayList<>();
        Account account = new Account(true, 100);
        operationRequests.add(new OperationRequest(account));
        LocalDateTime now = LocalDateTime.now();
        Transaction transaction = new Transaction("merchant", 50, now);
        operationRequests.add(new OperationRequest(transaction));

        List<OperationResponse> operationResponses = operationService.processOperations(operationRequests);

        assertEquals(2, operationResponses.size());
        assertEquals(100, operationResponses.get(0).getAccount().getAvailableLimit());
        assertEquals(50, operationResponses.get(1).getAccount().getAvailableLimit());
    }

    @Test
    void testGetOperationResponsesInJsonFormat() {
        OperationServiceImpl operationService = new OperationServiceImpl(null, null, null, null, null);
        List<OperationResponse> operationResponses = new ArrayList<>();
        operationResponses.add(new OperationResponse(new AccountResponse(new Account(true, 100)), new ArrayList<>()));
        operationResponses.add(new OperationResponse(new AccountResponse(new Account(true, 100)), Collections.singletonList(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString())));


        List<String> responseInJson = operationService.getOperationResponsesInJsonFormat(operationResponses);

        assertEquals(2, responseInJson.size());
        assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}", responseInJson.get(0));
        assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[\"account-already-initialized\"]}", responseInJson.get(1));
    }
}
