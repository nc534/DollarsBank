package com.dollarsbank.model;

import java.time.LocalDateTime;

public class Account {
    private int account_id;
    private String account_type;
    private LocalDateTime account_creation;
    private double initial_deposit;
    private double account_balance;

    Customer customer = new Customer();

    public Account(){

    }

    public Account(String account_type, LocalDateTime account_creation, double initial_deposit, double account_balance) {
        this.account_type = account_type;
        this.account_creation = account_creation;
        this.initial_deposit = initial_deposit;
        this.account_balance = account_balance;
    }

    public int getAccountId() {
        return account_id;
    }

    public String getAccountType() {
        return account_type;
    }

    public void setAccountType(String account_type) {
        this.account_type = account_type;
    }

    public LocalDateTime getAccountCreation() {
        return account_creation;
    }

    public void setAccountCreation(LocalDateTime account_creation) {
        this.account_creation = account_creation;
    }

    public double getInitialDeposit() {
        return initial_deposit;
    }

    public void setInitialDeposit(double initial_deposit) {
        this.initial_deposit = initial_deposit;
    }

    public double getAccountBalance() {
        return account_balance;
    }

    public void setAccountBalance(double account_balance) {
        this.account_balance = account_balance;
    }



}
