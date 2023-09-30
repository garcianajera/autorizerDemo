package com.demo.validation.service.account;

import com.demo.model.Account;
import com.demo.validation.service.AuthorizationResult;

public interface AccountAuthorizationService {

    AuthorizationResult authorize(Account account);

}
