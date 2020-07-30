package com.dollarsbank.dao;

import com.dollarsbank.model.Account;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.DollarsBankConnection;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class DollarsBankDaoImplementation implements DollarsBankDao{

    Scanner input = new Scanner(System.in);

    //method for registering new customers
    public int registerCustomer(Customer customer) throws ClassNotFoundException {

        System.out.println("+-------------------------------+");
        System.out.println("| Enter Details For New Account |");
        System.out.println("+-------------------------------+");

        String insert_customer_sql = "INSERT INTO customer (name, address, phone, userid, password)" +
                "VALUES (?, ?, ?, ?, ?)";

        int result = 0;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String userId = null;

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(insert_customer_sql);

            System.out.println("Customer Name:");
                String name = input.nextLine();
                    preparedStatement.setString(1, name);
            System.out.println("Customer Address:");
                String address = input.nextLine();
                    preparedStatement.setString(2, address);
            System.out.println("Customer Contact Number");
                String phone = input.nextLine();
                    preparedStatement.setString(3, phone);
            System.out.println("User Id:");
                    userId = input.nextLine();
                    preparedStatement.setString(4, userId);
            System.out.println("Password: (Minimum of 8 Characters With At Least 1 Lowercase, 1 Uppercase, & 1 Special)");
                String pw = input.nextLine();
                    preparedStatement.setString(5, pw);

            //System.out.println(preparedStatement);

            result = preparedStatement.executeUpdate();

            System.out.println(result);

        }catch (SQLIntegrityConstraintViolationException exists) {
            System.out.println("Customer with a User Id `" + userId + "` or that phone number already exists");
        }catch (SQLException e){
            e.printStackTrace();
        }

        return result;

    }

    //method for logging in and checking that the customer userId and password are correct
    public boolean findCustomer(Customer customer) throws ClassNotFoundException {

        System.out.println("+---------------------+");
        System.out.println("| Enter Login Details |");
        System.out.println("+---------------------+");

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
    public boolean checkCustomer(Customer customer) throws ClassNotFoundException {

        boolean customerexists = false;

        Class.forName("com.mysql.cj.jdbc.Driver");

        String find_customer_sql = "SELECT * FROM customer WHERE userid = ?";

        try {

            Connection connection = DollarsBankConnection.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(find_customer_sql);

            preparedStatement.setString(1, customer.getUserId());

            //System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            customerexists = rs.next();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return customerexists;

    }

    @Override
    public List<Customer> getCustomerInfo() {
        return null;
    }

    @Override
    public List<Transaction> getTransactions() {
        return null;
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }

    @Override
    public void updateAccount(Account account) {

    }
}
