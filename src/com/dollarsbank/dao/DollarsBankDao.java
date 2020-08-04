package com.dollarsbank.dao;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;

import java.util.List;

public interface DollarsBankDao {
    Customer getCustomerInfo(String userId);
    Transaction getTransactions(String userId);
    void addTransaction(String userId, String transaction_type, double transaction_amount);
    void addAccount(String userId, String account, double deposit);
    Account getAccountInfo(int customerId);
}
