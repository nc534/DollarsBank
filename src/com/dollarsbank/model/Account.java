package com.dollarsbank.model;

import java.sql.Timestamp;

public class Account {
    private int account_id;
    private String account_type;
    private Timestamp account_creation;
    private double initial_deposit;
    private double account_balance;

    public Account(){

    }

    public Account(String account_type, Timestamp account_creation, double initial_deposit, double account_balance) {
        this.account_type = account_type;
        this.account_creation = account_creation;
        this.initial_deposit = initial_deposit;
        this.account_balance = account_balance;
    }

    public void setAccountId(int account_id) {this.account_id = account_id; }

    public int getAccountId() {
        return account_id;
    }

    public String getAccountType() {
        return account_type;
    }

    public void setAccountType(String account_type) {
        this.account_type = account_type;
    }

    public Timestamp getAccountCreation() {
        return account_creation;
    }

    public void setAccountCreation(Timestamp account_creation) {
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
