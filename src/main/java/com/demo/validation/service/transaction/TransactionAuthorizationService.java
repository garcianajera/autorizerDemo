package com.demo.validation.service.transaction;

import com.demo.model.Transaction;
import com.demo.validation.service.AuthorizationResult;

public interface TransactionAuthorizationService {

    AuthorizationResult authorize(Transaction transaction);

}
