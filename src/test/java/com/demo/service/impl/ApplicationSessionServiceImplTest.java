package com.demo.service.impl;

import com.demo.service.ApplicationSessionService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationSessionServiceImplTest {

    @Test
    void testGetAccountFromSession() {
        ApplicationSessionService applicationSessionService = new ApplicationSessionServiceImpl();
        UUID accountId = UUID.randomUUID();

        assertNull(applicationSessionService.getAccountFromSession());
        applicationSessionService.setAccountInSession(accountId);

        assertNotNull(applicationSessionService.getAccountFromSession());
        assertEquals(accountId, applicationSessionService.getAccountFromSession());
    }

    @Test
    void testSetAccountInSession() {
        ApplicationSessionService applicationSessionService = new ApplicationSessionServiceImpl();
        UUID accountId = UUID.randomUUID();

        assertNull(applicationSessionService.getAccountFromSession());
        applicationSessionService.setAccountInSession(accountId);

        assertNotNull(applicationSessionService.getAccountFromSession());
        assertEquals(accountId, applicationSessionService.getAccountFromSession());
    }
}
