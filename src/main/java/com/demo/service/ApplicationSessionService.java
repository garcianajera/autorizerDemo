package com.demo.service;

import java.util.UUID;

public interface ApplicationSessionService {

    UUID getAccountFromSession();

    void setAccountInSession(UUID accountId);

}
