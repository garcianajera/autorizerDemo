package com.demo.model;

import java.time.LocalDateTime;

public class Transaction extends BaseEntity {

    private String merchant;

    private int amount;

    private LocalDateTime time;

    public Transaction() {
    }

    public Transaction(String merchant, int amount, LocalDateTime time) {
        this.merchant = merchant;
        this.amount = amount;
        this.time = time;
    }

    public String getMerchant() {
        return merchant;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

}

