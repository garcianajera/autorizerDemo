package com.demo.model;

public enum ViolationType {
    ACCOUNT_ALREADY_INITIALIZED("account-already-initialized"),
    ACCOUNT_NOT_INITIALIZED("account-not-initialized"),
    CARD_NOT_ACTIVE("card-not-active"),
    INSUFFICIENT_LIMIT("insufficient-limit"),
    HIGH_FREQUENCY_SMALL_INTERVAL("high-frequency-small-interval"),
    DOUBLED_TRANSACTION("doubled-transaction");

    private final String displayString;

    ViolationType(String displayString) {
        this.displayString = displayString;
    }

    public String getDisplayString() {
        return displayString;
    }
}
