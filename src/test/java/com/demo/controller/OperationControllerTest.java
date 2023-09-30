package com.demo.controller;

import com.demo.model.*;
import com.demo.service.OperationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationControllerTest {

    @Test
    void processOperations() {

        List<String> request = new ArrayList<>();
        request.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}}");
        request.add("{\"transaction\": {\"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-\n" +
                "13T11:00:00.000Z\"}}");

        OperationService operationService = Mockito.mock(OperationService.class);
        List<OperationRequest> operationRequests = Arrays.asList(new OperationRequest(new Account(true, 100)),
                new OperationRequest(new Transaction("Burger King", 20, LocalDateTime.now())));
        Mockito.when(operationService.parseInput(request)).thenReturn(operationRequests);

        List<OperationResponse> operationResponses =
                Arrays.asList(new OperationResponse(new AccountResponse(new Account(true, 100)), Collections.emptyList()),
                        new OperationResponse(new AccountResponse(new Account(true, 80)), Collections.emptyList()));

        Mockito.when(operationService.processOperations(operationRequests)).thenReturn(operationResponses);
        List<String> responseInJson = new ArrayList<>();
        responseInJson.add("{\"account\": {\"active-card\": true, \"available-limit\": 100}, \"violations\": []}");
        responseInJson.add("{\"account\": {\"active-card\": true, \"available-limit\": 80}, \"violations\": []}");

        Mockito.when(operationService.getOperationResponsesInJsonFormat(operationResponses)).thenReturn(responseInJson);

        OperationController operationController = new OperationController(operationService);


        List<String> response = operationController.processOperations(request);

        assertEquals(2, response.size());
        assertEquals(responseInJson.get(0), response.get(0));
        assertEquals(responseInJson.get(1), response.get(1));
    }
}
