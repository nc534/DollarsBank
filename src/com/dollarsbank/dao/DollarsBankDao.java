package com.dollarsbank.dao;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;

import java.util.List;

public interface DollarsBankDao {
    Customer getCustomerInfo(String userId);
    List<Transaction> getTransactions(String userId);
    void addTransaction(String userId, String account_type, String transaction_type, int transfer_from, int transfer_to, double transaction_amount);
    void addTransfer(String userId, int account_id, int transfer_to, double transaction_amount);
    void addAccount(String userId, String account, double deposit);
    Account getAccountInfo(int customerId, String account);
}
