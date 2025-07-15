package com.outgoings.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class Money {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    int id;

    private String currency;

    private double amount;

    private double lastCurrencyRate;

    private double currencyRateDifference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLastCurrencyRate() {
        return lastCurrencyRate;
    }

    public void setLastCurrencyRate(double lastCurrencyRate) {
        this.lastCurrencyRate = lastCurrencyRate;
    }

    public double getCurrencyRateDifference() {
        return currencyRateDifference;
    }

    public void setCurrencyRateDifference(double currencyRateDifference) {
        this.currencyRateDifference = currencyRateDifference;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
