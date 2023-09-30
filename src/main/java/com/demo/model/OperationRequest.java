package com.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OperationRequest {

    private Account account;

    private Transaction transaction;

    @JsonProperty("allow-list")
    private AllowList allowList;

    public AllowList getAllowList() {
        return allowList;
    }

    public void setAllowList(AllowList allowList) {
        this.allowList = allowList;
    }

    public OperationRequest(Account account) {
        this.account = account;
    }

    public OperationRequest(Transaction transaction) {
        this.transaction = transaction;
    }

    public OperationRequest() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
