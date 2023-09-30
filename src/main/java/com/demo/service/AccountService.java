package com.demo.service;

import com.demo.model.Account;

public interface AccountService {

    Account saveAccount(Account account);

    Account getAccountFromSession();

    void setAccountInSession(Account account);

}
