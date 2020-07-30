package com.dollarsbank.dao;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;

import java.util.List;

public interface DollarsBankDao {
    public List<Customer> getCustomerInfo();
    public List<Transaction> getTransactions();
    public void addTransaction(Transaction transaction);
    public void updateAccount(Account account);
}
