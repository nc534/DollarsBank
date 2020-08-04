package com.dollarsbank.dao;

import com.dollarsbank.controller.DollarsBankController;
import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.ConsoleColor;
import com.dollarsbank.utility.DollarsBankConnection;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class DollarsBankDaoImplementation implements DollarsBankDao{

    Customer customer = new Customer();
    Scanner input = new Scanner(System.in);

    //method for registering new customers
    public int registerCustomer(Customer customer) throws ClassNotFoundException {

        System.out.println(ConsoleColor.BLUE + "+-------------------------------+");
        System.out.println("| Enter Details For New Account |");
        System.out.println("+-------------------------------+" + ConsoleColor.RESET);

        String insert_customer_sql = "INSERT INTO customer (name, address, phone, userid, password)" +
                "VALUES (?, ?, ?, ?, ?)";

        boolean customerexists;
        int result = 0;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String phone;
        String userId;
        boolean isValid = false;

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(insert_customer_sql);

            while(!isValid) {
                System.out.println("Customer Name:");
                String name = input.nextLine();
                if(name.equals("") || name.equals(" ")) {
                    System.out.println("There was no input. Please input your name.");
                } else {
                    isValid = true;
                    preparedStatement.setString(1, name);
                    customer.setName(name);
                }
            }
            System.out.println("Customer Address:");
                String address = input.nextLine();
                    preparedStatement.setString(2, address);
                    customer.setAddress(address);
            System.out.println("Customer Contact Number");
                    phone = input.nextLine();
                    preparedStatement.setString(3, phone);
                    customer.setPhone(phone);
            System.out.println("User Id:");
                    userId = input.nextLine();
                    preparedStatement.setString(4, userId);
                    customer.setUserId(userId);
            System.out.println("Password: (Minimum of 8 Characters With At Least 1 Lowercase, 1 Uppercase, & 1 Special)");
                String pw = input.nextLine();
                    preparedStatement.setString(5, pw);
                    customer.setPassword(pw);

            //System.out.println(preparedStatement);

            //check if customer already exists
            customerexists = checkCustomer(userId, phone);

            //only insert new customer if customer does not already exists
            if(!customerexists){
                result = preparedStatement.executeUpdate();
                DollarsBankController.createAccount(userId);
            }

            //System.out.println(result);

        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;

    }

    //method for logging in and checking that the customer userId and password are correct
    public boolean findCustomer(Customer customer) throws ClassNotFoundException {

        System.out.println(ConsoleColor.BLUE + "+---------------------+");
        System.out.println("| Enter Login Details |");
        System.out.println("+---------------------+" + ConsoleColor.RESET);

        boolean customerexists = false;

        Class.forName("com.mysql.cj.jdbc.Driver");

        //Binary makes sure customer userId and password are case sensitive
        String find_customer_sql = "SELECT * FROM customer WHERE BINARY userid = ? AND BINARY password = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(find_customer_sql);

            System.out.println("User Id:");
                String userId = input.nextLine();
                    preparedStatement.setString(1, userId);
                    customer.setUserId(userId);
            System.out.println("Password:");
                String pw = input.nextLine();
                    preparedStatement.setString(2, pw);

            //System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            customerexists = rs.next();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return customerexists;

    }

    //method for checking if the customer already exists when registering
    public boolean checkCustomer(String userId, String phone) throws ClassNotFoundException {

        boolean customerexists = false;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String find_customer_exists_sql = "SELECT * FROM customer WHERE userid = ? OR phone = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(find_customer_exists_sql);

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, phone);

            //System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            customerexists = rs.next();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return customerexists;

    }

    public boolean accountExists(String userId, String account_type) {
        boolean accountExists = false;

        String account_exists_sql = "SELECT * FROM account a INNER JOIN customer c ON c.customer_id = a.customer_id WHERE " +
                "userid = ? AND account_type = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(account_exists_sql);

            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, account_type);

            //System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            accountExists = rs.next();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return  accountExists;
    }

    public boolean updateBalance(double transaction_amount, int account_id, int transaction_id) {

        boolean updated = false;

        String update_balance_sql = "UPDATE account a INNER JOIN transaction t " +
                "ON a.account_id = t.account_id " +
                "SET account_balance = " +
                "case " +
                "   when transaction_type = 'deposit' then account_balance + " + transaction_amount +
                "   when transaction_type = 'withdraw' then account_balance - " + transaction_amount +
                "   else account_balance " +
                "end " +
                "where a.account_id = ? AND transaction_id = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(update_balance_sql);

            preparedStatement.setInt(1, account_id);
            preparedStatement.setInt(2, transaction_id);

            //will return false since it is an update
            updated = preparedStatement.execute();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public double getBalance(int customer_id, int account_id) {
        double balance = 0;

        String get_balance_sql = "SELECT * FROM account a INNER JOIN customer c " +
                "ON c.customer_id = a.customer_id " +
                "WHERE a.customer_id = ? AND account_id = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(get_balance_sql);

            preparedStatement.setInt(1, customer_id);
            preparedStatement.setInt(2, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();

            Account currentAccount = new Account();

            currentAccount.setAccountId(rs.getInt("account_id"));
            currentAccount.setAccountType(rs.getString("account_type"));
            currentAccount.setAccountCreation(rs.getTimestamp("account_creation"));
            currentAccount.setInitialDeposit(rs.getDouble("initial_deposit"));
            currentAccount.setAccountBalance(rs.getDouble("account_balance"));

            balance = currentAccount.getAccountBalance();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return balance;
    }

    @Override
    public Customer getCustomerInfo(String userId) {
        String customer_info_sql = "SELECT * FROM customer WHERE userid = ?";

        try{
            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(customer_info_sql);
            preparedStatement.setString(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setUserId(rs.getString("userid"));
            customer.setPassword(rs.getString("password"));
            customer.setName(rs.getString("name"));
            customer.setPhone(rs.getString("phone"));
            customer.setAddress(rs.getString("address"));

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public Transaction getTransactions(String userId) {

        int customer_id = getCustomerInfo(userId).getCustomerId();

        String get_transactions_sql = "SELECT c.customer_id, transaction_id, a.account_id, account_type, transaction_date, transaction_type," +
                "transfer_from, transfer_to," +
                "transaction_amount FROM transaction t " +
                "INNER JOIN account a " +
                "ON t.account_id = a.account_id " +
                "INNER JOIN customer c " +
                "ON a.customer_id = c.customer_id " +
                "WHERE c.customer_id = ? " +
                "ORDER BY transaction_date DESC " +
                "LIMIT 5;";

        Transaction transaction = new Transaction();

        try{

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(get_transactions_sql);
            preparedStatement.setInt(1, customer_id);

            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            transaction.setTransactionId(rs.getInt("transaction_id"));
            transaction.setTransactionDate(rs.getTimestamp("transaction_date"));
            transaction.setTransactionType(rs.getString("transaction_type"));
            transaction.setTransferFrom(rs.getInt("transfer_from"));
            transaction.setTransferTo(rs.getInt("transfer_to"));
            transaction.setTransactionAmount(rs.getDouble("transaction_amount"));

        } catch (SQLException e) {
            System.out.println("No Transactions Were Made!");
        }
        return transaction;
    }

    @Override
    public void addTransaction(String userId, String transaction_type, double transaction_amount) {

        int account_id = getAccountInfo(getCustomerInfo(userId).getCustomerId()).getAccountId();
        boolean transactionCreated;
        PreparedStatement preparedStatement = null;
        String add_transaction_sql;

        if(transaction_type.equals("initial_deposit")) {

            add_transaction_sql = "INSERT into transaction(account_id, transaction_date, transaction_type, transaction_amount)" +
                    "values " +
                    "(" + account_id + ",  " +
                    "(select account_creation from account where account_id = " + account_id + "), ?, ?";

            try {

                Connection connection = DollarsBankConnection.getConnection();

                preparedStatement = connection.prepareStatement(add_transaction_sql);
                preparedStatement.setString(1, transaction_type);
                preparedStatement.setDouble(2, transaction_amount);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            add_transaction_sql = "insert into transaction(account_id, transaction_date, transaction_type, transaction_amount) " +
            "values (?,  now(), ?, ?)";

            try{

                Connection connection = DollarsBankConnection.getConnection();

                preparedStatement = connection.prepareStatement(add_transaction_sql);
                preparedStatement.setInt(1, account_id);
                preparedStatement.setString(2, transaction_type);
                preparedStatement.setDouble(3, transaction_amount);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        try {
            assert preparedStatement != null;
            transactionCreated = preparedStatement.execute();

            if (transactionCreated) {
                getTransactions(getCustomerInfo(userId).getUserId());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addAccount(String userId, String account, double deposit) {

        int customer_id = getCustomerInfo(userId).getCustomerId();
        boolean accountCreated;

        String add_account_sql = "Insert into account (customer_id, account_type, account_creation, initial_deposit, account_balance)" +
                "values (?, ?, now(), ?, initial_deposit)";

        try{
            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(add_account_sql);
            preparedStatement.setInt(1, customer_id);
            preparedStatement.setString(2, account);
            preparedStatement.setDouble(3, deposit);
            accountCreated = preparedStatement.execute();

            if(accountCreated){
                getAccountInfo(customer_id);
            }

        } catch (SQLException e) {
                e.printStackTrace();
        }
    }

    @Override
    public Account getAccountInfo(int customer_id){

        String find_account_sql = "SELECT * from account where customer_id = ?";

        Account currentAccount = new Account();

        try{
            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(find_account_sql);
            preparedStatement.setInt(1, customer_id);
            ResultSet rs = preparedStatement.executeQuery();

            rs.next();

            currentAccount.setAccountId(rs.getInt("account_id"));
            currentAccount.setAccountType(rs.getString("account_type"));
            currentAccount.setAccountCreation(rs.getTimestamp("account_creation"));
            currentAccount.setInitialDeposit(rs.getDouble("initial_deposit"));
            currentAccount.setAccountBalance(rs.getDouble("account_balance"));

        } catch (SQLException e) {
            System.out.println("You Do Not Have An Account Created!");
        }

        return currentAccount;

    }
}
