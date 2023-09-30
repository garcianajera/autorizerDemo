package com.demo.service;

import com.demo.model.Account;
import com.demo.model.OperationRequest;
import com.demo.model.OperationResponse;
import com.demo.model.Transaction;
import com.demo.validation.service.transaction.TransactionAuthorizationService;

import java.util.List;

public interface OperationService {

    List<OperationRequest> parseInput(List<String> input);

    List<OperationResponse> processOperations(List<OperationRequest> operationRequests);

    OperationResponse processAccount(Account account);

    OperationResponse processTransaction(Transaction transaction, TransactionAuthorizationService transactionAuthorizationService);

    List<String> getOperationResponsesInJsonFormat(List<OperationResponse> operationResponses);

}
