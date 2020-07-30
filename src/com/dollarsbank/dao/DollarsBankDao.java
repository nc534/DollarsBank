package com.dollarsbank.dao;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;

import java.util.List;

public interface DollarsBankDao {
    List<Customer> getCustomerInfo();
    List<Transaction> getTransactions();
    void addTransaction(Transaction transaction);
    void updateAccount(Account account);
}
