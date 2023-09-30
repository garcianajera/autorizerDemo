package com.demo.util;

import com.demo.model.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonUtilTest {

    @Test
    void parseAccount() {
        OperationRequest operationRequest = JsonUtil.parse("{\"account\": {\"active-card\": true, \"available-limit\": 100}}", OperationRequest.class);
        assertEquals(100, operationRequest.getAccount().getAvailableLimit());

        assertTrue(operationRequest.getAccount().hasActiveCard());
    }

    @Test
    void parseTransaction() {
        OperationRequest operationRequest = JsonUtil.parse("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\"}}", OperationRequest.class);
        assertEquals("Burger King", operationRequest.getTransaction().getMerchant());
        assertEquals(20, operationRequest.getTransaction().getAmount());
        LocalDateTime date = LocalDateTime.parse("2019-02-13T10:00:00.000Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        assertEquals(date, operationRequest.getTransaction().getTime());
    }

    @Test
    void testToJsonString() {
        OperationResponse operationResponse = new OperationResponse(new AccountResponse(new Account(true, 100)), new ArrayList<>());
        String json = JsonUtil.toJsonString(operationResponse);

        assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[]}", json);
    }

    @Test
    void testToJsonStringWithViolations() {
        List<String> violations = Arrays.asList(ViolationType.ACCOUNT_ALREADY_INITIALIZED.getDisplayString(), ViolationType.CARD_NOT_ACTIVE.getDisplayString());
        OperationResponse operationResponse = new OperationResponse(new AccountResponse(new Account(true, 100)), violations);
        String json = JsonUtil.toJsonString(operationResponse);

        assertEquals("{\"account\":{\"active-card\":true,\"available-limit\":100},\"violations\":[\"account-already-initialized\",\"card-not-active\"]}", json);
    }
}
