package com.demo.service.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.demo.model.*;
import com.demo.service.AccountService;
import com.demo.service.OperationService;
import com.demo.service.TransactionService;
import com.demo.util.JsonUtil;
import com.demo.validation.service.AuthorizationResult;
import com.demo.validation.service.account.AccountAuthorizationService;
import com.demo.validation.service.transaction.TransactionAuthorizationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OperationServiceImpl implements OperationService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AccountAuthorizationService accountAuthorizationService;
    private final TransactionAuthorizationService defaulTransactionAuthorizationService;
    private final TransactionAuthorizationService limitedTransactionAuthorizationService;

    @Inject
    public OperationServiceImpl(AccountService accountService, TransactionService transactionService, AccountAuthorizationService accountAuthorizationService,
                                @Named("defaulTransactionAuthorizationService") TransactionAuthorizationService defaulTransactionAuthorizationService,
                                @Named("limitedTransactionAuthorizationService") TransactionAuthorizationService limitedTransactionAuthorizationService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.accountAuthorizationService = accountAuthorizationService;
        this.defaulTransactionAuthorizationService = defaulTransactionAuthorizationService;
        this.limitedTransactionAuthorizationService = limitedTransactionAuthorizationService;
    }


    @Override
    public List<OperationRequest> parseInput(List<String> input) {
        return input.stream().map(s -> JsonUtil.parse(s, OperationRequest.class)).collect(Collectors.toList());
    }

    @Override
    public List<OperationResponse> processOperations(List<OperationRequest> operationRequests) {

        TransactionAuthorizationService transactionAuthorizationService = defaulTransactionAuthorizationService;
        List<OperationResponse> operationResponses = new ArrayList<>();
        //we have two different operations, account and transaction
        for (OperationRequest operationRequest : operationRequests) {

            if (operationRequest.getAllowList() != null) {
                if (operationRequest.getAllowList().isActive()) {
                    transactionAuthorizationService = limitedTransactionAuthorizationService;
                }
            }

            if (operationRequest.getAccount() != null) {
                operationResponses.add(processAccount(operationRequest.getAccount()));
            }
            if (operationRequest.getTransaction() != null) {
                operationResponses.add(processTransaction(operationRequest.getTransaction(), transactionAuthorizationService));
            }
        }

        return operationResponses;
    }

    @Override
    public OperationResponse processAccount(Account account) {
        //we have to validate and authorize account
        AuthorizationResult authorizationResult = accountAuthorizationService.authorize(account);
        List<String> violations = new ArrayList<>();
        if (authorizationResult.notValid()) {
            violations.addAll(authorizationResult.getViolations());
        } else {
            accountService.setAccountInSession(account);
        }
        return new OperationResponse(new AccountResponse(account), violations);
    }

    @Override
    public OperationResponse processTransaction(Transaction transaction, TransactionAuthorizationService authorizationService) {
        //we have to validate and authorize transaction
        AuthorizationResult authorizationResult = authorizationService.authorize(transaction);
        Account account = accountService.getAccountFromSession();
        List<String> violations = new ArrayList<>();
        if (authorizationResult.notValid()) {
            violations.addAll(authorizationResult.getViolations());
        } else {
            account.setAvailableLimit(account.getAvailableLimit() - transaction.getAmount());
            accountService.saveAccount(account);
            transactionService.saveTransaction(transaction);
        }
        return new OperationResponse(new AccountResponse(account), violations);
    }


    @Override
    public List<String> getOperationResponsesInJsonFormat(List<OperationResponse> operationResponses) {
        List<String> operationResponseInJsonFormat = new ArrayList<>();
        for (OperationResponse operationResponse : operationResponses) {
            operationResponseInJsonFormat.add(JsonUtil.toJsonString(operationResponse));
        }
        return operationResponseInJsonFormat;
    }


}
