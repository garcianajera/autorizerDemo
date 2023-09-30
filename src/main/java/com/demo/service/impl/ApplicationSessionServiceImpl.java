package com.demo.service.impl;

import com.demo.service.ApplicationSessionService;

import java.util.UUID;

public class ApplicationSessionServiceImpl implements ApplicationSessionService {
    private UUID accountInSession;

    @Override
    public UUID getAccountFromSession() {
        return accountInSession;
    }

    @Override
    public void setAccountInSession(UUID accountId) {
        accountInSession = accountId;
    }
}
