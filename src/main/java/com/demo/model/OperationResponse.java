package com.demo.model;

import java.util.List;

public class OperationResponse {

    private AccountResponse account;
    private List<String> violations;

    public OperationResponse() {
    }

    public OperationResponse(AccountResponse account, List<String> violations) {
        this.account = account;
        this.violations = violations;
    }

    public List<String> getViolations() {
        return violations;
    }

    public AccountResponse getAccount() {
        return account;
    }

}
