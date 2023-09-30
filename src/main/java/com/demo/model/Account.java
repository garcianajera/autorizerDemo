package com.demo.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Account extends BaseEntity {

    @JsonProperty("active-card")
    private boolean hasActiveCard;

    @JsonProperty("available-limit")
    private int availableLimit;

    public Account() {
    }

    public Account(boolean hasActiveCard, int availableLimit) {
        this.hasActiveCard = hasActiveCard;
        this.availableLimit = availableLimit;
    }

    public boolean hasActiveCard() {
        return hasActiveCard;
    }

    public int getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(int availableLimit) {
        this.availableLimit = availableLimit;
    }

}
