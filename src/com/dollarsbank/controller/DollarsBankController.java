package com.dollarsbank.controller;

import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.ConsoleColor;
import com.dollarsbank.dao.DollarsBankDaoImplementation;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DollarsBankController {

    static DollarsBankDaoImplementation DBDao = new DollarsBankDaoImplementation();
    static Scanner input = new Scanner(System.in);
    static String account;

    public static void userMain(Customer customer){

        String user_id = customer.getUserId();
        int customer_id = DBDao.getCustomerInfo(user_id).getCustomerId();
        int account_id = DBDao.getAccountInfo(customer_id).getAccountId();
        int choice;
        boolean loggedOut = false;
        int accountChoice;
        String transaction_type;
        double transaction_amount;

        do {

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

            System.out.println(ConsoleColor.GREEN + "Enter Choice:" + ConsoleColor.RESET);

            try {
                choice = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please input a valid choice.");
                continue;
            }

            switch (choice) {
                case 1:
                case 2:

                    if(choice == 1){
                        System.out.println("Which account do you want to deposit to?");
                        transaction_type = "deposit";
                    }else{
                        System.out.println("Which account do you want to withdraw from?");
                        transaction_type = "withdraw";
                    }

                    System.out.println("1. Savings");
                    System.out.println("2. Checking");

                    accountChoice = input.nextInt();
                    account = account(accountChoice);

                    if(!DBDao.accountExists(user_id, account)) {
                        System.out.println("You do not have a " + account + " account.");
                    } else {

                        System.out.println("How much do you want to " + transaction_type + " from your " + account + " account?");
                        transaction_amount = input.nextDouble();

                        DBDao.addTransaction(user_id, transaction_type, transaction_amount);

                        int transaction_id = DBDao.getTransactions(user_id).getTransactionId();

                        boolean updated = DBDao.updateBalance(transaction_amount, account_id, transaction_id);

                        if(!updated) {
                            if(transaction_type .equals("deposit")) {
                                System.out.println("$" + transaction_amount + " was added to your " + account + " account.");
                            }else{
                                System.out.println("$" + transaction_amount + " was subtracted from your " + account + " account.");
                            }
                            double balance = DBDao.getBalance(customer_id, account_id);
                            System.out.println("New Balance $" + balance);
                        }else {
                            System.out.println("Failed to update balance");
                        }

                    }

                    promptEnterKey();

                    break;
                case 3:
                    System.out.println("Which account do you want to transfer funds to?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");
                    System.out.println("3. Another Customer");

                    accountChoice = input.nextInt();
                    account = account(accountChoice); //maybe remove options. another customer not accounted
                    break;
                case 4:
                    System.out.println("Which account do you want to check the balance for?");
                    System.out.println("1. Savings");
                    System.out.println("2. Checking");

                    accountChoice = input.nextInt();
                    account = account(accountChoice);

                    if(!DBDao.accountExists(user_id, account)) {
                        System.out.println("You do not have a " + account + " account.");
                        promptEnterKey();
                    }else{
                        double balance = DBDao.getBalance(customer_id, account_id);
                        System.out.println(account + " account");
                        System.out.println("Balance: $" + balance);
                        promptEnterKey();
                    }

                    break;
                case 5:
                    System.out.println("These are your 5 recent transactions:");
                    Transaction transaction = DBDao.getTransactions(user_id);
                    if(transaction.getTransactionId() != 0) {
                        System.out.println(transaction.getTransactionType() + " of $" + transaction.getTransactionAmount() + " to account id " + account_id + " on " + transaction.getTransactionDate());
                    }

                    promptEnterKey();

                    break;
                case 6:
                    System.out.println(ConsoleColor.PURPLE + "Your Information");
                    System.out.println("----------------" + ConsoleColor.RESET);
                    System.out.println(DBDao.getCustomerInfo(user_id));
                    promptEnterKey();
                    break;
                case 7:
                    createAccount(user_id);
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

    public static String account(int accountChoice){

        if(accountChoice == 1){
            account = "savings";
        }else if (accountChoice == 2){
            account = "checking";
        }else{
            System.out.println("Not a valid option");
        }
        return account;
    }

    public static void createAccount(String user_id){
        System.out.println("What kind of account do you want to create?");
        System.out.println("1. Savings");
        System.out.println("2. Checking");

        int accountChoice = input.nextInt();
        account = account(accountChoice);

        if(DBDao.accountExists(user_id, account)) {
            System.out.println("You already have a " + account + " account.");
            promptEnterKey();
        } else {
            System.out.println("Creating a " + account + " account.");
            System.out.println("How much do you want to deposit for the initial deposit?");
            double deposit = input.nextDouble();
            DBDao.addAccount(user_id, account, deposit);
            DBDao.addTransaction(user_id, "initial_deposit", deposit);
        }
    }

    public static void promptEnterKey(){
        System.out.println("\nPress a key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
