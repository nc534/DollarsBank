package com.dollarsbank.model;

import java.sql.Timestamp;

public class Transaction {
    private int transaction_id;
    private Timestamp transaction_date;
    private String transaction_type;
    private int transfer_from;
    private int transfer_to;
    private double transaction_amount;

    public Transaction() {

    }

    public Transaction(Timestamp transaction_date, String transaction_type, int transfer_from, int transfer_to, double transaction_amount) {
        this.transaction_date = transaction_date;
        this.transaction_type = transaction_type;
        this.transfer_from = transfer_from;
        this.transfer_to = transfer_to;
        this.transaction_amount = transaction_amount;
    }

    public  void setTransactionId(int transaction_id) { this.transaction_id = transaction_id; }

    public int getTransactionId() {
        return transaction_id;
    }

    public Timestamp getTransactionDate() {
        return transaction_date;
    }

    public void setTransactionDate(Timestamp transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getTransactionType() {
        return transaction_type;
    }

    public void setTransactionType(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getTransferFrom() {
        return transfer_from;
    }

    public void setTransferFrom(int transfer_from) {
        this.transfer_from = transfer_from;
    }

    public int getTransferTo() {
        return transfer_to;
    }

    public void setTransferTo(int transfer_to) {
        this.transfer_to = transfer_to;
    }

    public double getTransactionAmount() {
        return transaction_amount;
    }

    public void setTransactionAmount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }
}
