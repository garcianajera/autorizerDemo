package com.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountResponse {

    @JsonProperty("active-card")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean hasActiveCard;

    @JsonProperty("available-limit")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer availableLimit;

    public AccountResponse() {

    }

    public AccountResponse(Account account) {
        if (account != null) {
            this.availableLimit = account.getAvailableLimit();
            this.hasActiveCard = account.hasActiveCard();
        }
    }

    public Boolean hasActiveCard() {
        return hasActiveCard;
    }

    public Integer getAvailableLimit() {
        return availableLimit;
    }
}
