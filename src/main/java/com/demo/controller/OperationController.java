package com.demo.controller;

import com.google.inject.Inject;
import com.demo.model.OperationResponse;
import com.demo.service.OperationService;

import java.util.List;

public class OperationController {

    private final OperationService operationService;

    @Inject
    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    public List<String> processOperations(List<String> operationsInJsonFormat) {
        //controller calls service layer and returns a list with operation responses in JSON
        List<OperationResponse> operationResponses = operationService.processOperations(operationService.parseInput(operationsInJsonFormat));
        return operationService.getOperationResponsesInJsonFormat(operationResponses);
    }

}
