package com.dollarsbank.controller;

import com.dollarsbank.dao.DollarsBankDao;
import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.ConsoleColor;
import com.dollarsbank.dao.DollarsBankDaoImplementation;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class DollarsBankController {

    public static void userMain(Customer customer){

        int choice;
        boolean loggedOut = false;
        int accountChoice;
        String account = null;
        double deposit;
        double withdraw;

        do {

            DollarsBankDaoImplementation DBDao = new DollarsBankDaoImplementation();

            System.out.println(ConsoleColor.BLUE + "+-------------------+");
            System.out.println("| WELCOME Customer! |");
            System.out.println("+-------------------+" + ConsoleColor.RESET);

            System.out.println("1. Deposit Amount");
            System.out.println("2. Withdraw Amount");
            System.out.println("3. Funds Transfer");
            System.out.println("4. Check Balance");
            System.out.println("5. View 5 Recent Transactions");
            System.out.println("6. Display Customer Information");
            System.out.println("7. Create an account");
            System.out.println("8. Sign Out");

            System.out.println();

            Scanner input = new Scanner(System.in);

            System.out.println(ConsoleColor.GREEN + "Enter Choice:" + ConsoleColor.RESET);

            try {
                choice = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please input a valid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Which account do you want to deposit to?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    accountChoice = input.nextInt();
                    switch(accountChoice){
                        case 1:
                    }
                    break;
                case 2:
                    System.out.println("Which account do you want to withdraw from");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    break;
                case 3:
                    System.out.println("Which account do you want to transfer funds to?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    System.out.println("3. Another Customer");
                    break;
                case 4:
                    System.out.println("Which account do you want to check the balance for?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    break;
                case 5:
                    System.out.println("These are your 5 recent transactions:");
                    Transaction transaction = DBDao.getTransactions(customer.getUserId());
                    int account_id = DBDao.getAccountInfo(DBDao.getCustomerInfo(customer.getUserId()).getCustomerId()).getAccountId();
                    if(transaction.getTransactionId() != 0) {
                        System.out.println(transaction.getTransactionType() + " of $" + transaction.getTransactionAmount() + " to account id " + account_id + " on " + transaction.getTransactionDate());
                    }

                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case 6:
                    System.out.println(ConsoleColor.PURPLE + "Your Information");
                    System.out.println("----------------" + ConsoleColor.RESET);
                    System.out.println(DBDao.getCustomerInfo(customer.getUserId()));
                    break;
                case 7:
                    System.out.println("What kind of account do you want to create?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    accountChoice = input.nextInt();
                    if(accountChoice == 1){
                        account = "savings";
                    }else if (accountChoice == 2){
                        account = "checking";
                    }else{
                        System.out.println("Not a valid option");
                    }

                    System.out.println("Creating a " + account + " account.");
                    System.out.println("How much do you want to deposit for the initial deposit?");
                    deposit = input.nextDouble();
                    DBDao.addAccount(customer.getUserId(), account, deposit);
                    DBDao.addTransaction(customer.getUserId());
                    break;
                case 8:
                    loggedOut = true;
                    System.out.println("Logged Out.");
                    break;
                default:
                    System.out.println("Invalid choice. Please input a valid choice");
                    break;
            }
        }while(!loggedOut);
    }

}
