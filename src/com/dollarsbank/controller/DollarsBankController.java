package com.dollarsbank.controller;

import com.dollarsbank.model.Customer;
import com.dollarsbank.model.Transaction;
import com.dollarsbank.utility.ConsoleColor;
import com.dollarsbank.dao.DollarsBankDaoImplementation;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DollarsBankController {

    static DollarsBankDaoImplementation DBDao = new DollarsBankDaoImplementation();
    static Scanner input = new Scanner(System.in);
    static String account;

    public static void mainMenu() throws ClassNotFoundException {
        int choice;
        boolean exit = false;

        do {

            System.out.println(ConsoleColor.BLUE + "+---------------------------+");
            System.out.println("| DOLLARSBANK Welcomes You! |");
            System.out.println("+---------------------------+" + ConsoleColor.RESET);

            System.out.println("1. Create New Customer Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");

            System.out.println();

            Scanner input = new Scanner(System.in);

            System.out.println(ConsoleColor.GREEN + "Enter Choice (1, 2, or 3):" + ConsoleColor.RESET);

            try {
                choice = input.nextInt();
            }catch (InputMismatchException e){
                System.out.println("Invalid choice. Please input either 1, 2, or 3.");
                continue;
            }

            Customer customer = new Customer();

            switch (choice) {
                case 1:
                    int result;

                    System.out.println(ConsoleColor.BLUE + "+-------------------------------+");
                    System.out.println("| Enter Details For New Account |");
                    System.out.println("+-------------------------------+" + ConsoleColor.RESET);

                    result = DBDao.registerCustomer(customer);

                    if (result != 0) {
                        System.out.println("Customer Account Created!");
                    } else {
                        System.out.println("Customer Account Failed to Create! Account with the User Id or Phone already exists.");
                    }
                    promptEnterKey();
                    break;
                case 2:
                    boolean customerexists;
                    String quit;

                    do {

                        quit = input.nextLine();
                        if(quit.equals("-1")){
                            break;
                        }

                        System.out.println(ConsoleColor.BLUE + "+---------------------+");
                        System.out.println("| Enter Login Details |");
                        System.out.println("+---------------------+" + ConsoleColor.RESET);

                        customerexists = DBDao.findCustomer(customer);

                        if (customerexists) {
                            userMain(customer);
                        } else {
                            System.out.println(ConsoleColor.RED + "Invalid Credentials. Try Again!" + ConsoleColor.RESET);
                            System.out.println(ConsoleColor.YELLOW + "Enter -1 to go back to main menu." + ConsoleColor.RESET);
                        }

                    } while (!customerexists);

                    break;
                case 3:
                    System.out.println("Goodbye!");
                    exit = true;
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please input either 1, 2, or 3.");
                    promptEnterKey();
                    break;
            }
        }while(!exit);
    }

    public static void userMain(Customer customer){

        String user_id = customer.getUserId();
        int customer_id = DBDao.getCustomerInfo(user_id).getCustomerId();
        int choice;
        boolean loggedOut = false;
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

                    account = null;

                    if(choice == 1){
                        System.out.println("Which account do you want to deposit to?");
                        transaction_type = "deposit";
                    }else{
                        System.out.println("Which account do you want to withdraw from?");
                        transaction_type = "withdraw";
                    }

                    while(account == null) {
                        account = account();
                    }

                    if(!DBDao.accountExists(user_id, account)) {
                        System.out.println("You do not have a " + account + " account.");
                    } else {
                        int account_id = DBDao.getAccountInfo(customer_id, account).getAccountId();

                        System.out.println("How much do you want to " + transaction_type + " from your " + account + " account?");
                        transaction_amount = input.nextDouble();

                        DBDao.addTransaction(user_id, account, transaction_type, 0, 0, transaction_amount);

                        List<Transaction> transaction = DBDao.getTransactions(user_id);

                        int transaction_id = transaction.get(0).getTransactionId();

                        System.out.println(transaction_id);

                        boolean updated = DBDao.updateBalance(transaction_amount, account_id, transaction_id);

                        if(!updated) {
                            if(transaction_type .equals("deposit")) {
                                System.out.println("$" + transaction_amount + " was deposited to your " + account + " account.");
                            }else{
                                System.out.println("$" + transaction_amount + " was withdrawn from your " + account + " account.");
                            }
                            double balance = DBDao.getBalance(customer_id, account_id);
                            System.out.println("New Balance: $" + balance);
                        }else {
                            System.out.println("Failed to update balance");
                        }

                    }

                    promptEnterKey();

                    break;
                case 3: //needs fixing
                    account = null;

                    while(account == null) {

                        transaction_type = "transfer";
                        String account2;

                        System.out.println("Which account do you want to transfer funds to?");
                        System.out.println("1. Savings");
                        System.out.println("2. Checking");
                        System.out.println("3. Another Customer");

                        int accountChoice = input.nextInt();

                        if(accountChoice == 1 || accountChoice == 2){

                            if(accountChoice == 1) {
                                account = "savings";
                                account2 = "checking";
                            }else{
                                account = "checking";
                                account2 = "savings";
                            }

                            int transfer_to = DBDao.getAccountInfo(customer_id, account).getAccountId();
                            int transfer_from = DBDao.getAccountInfo(customer_id, account2).getAccountId();

                            System.out.println("How much do you want to " + transaction_type + " to your " + account + " account from your " + account2 + " account?");
                            transaction_amount = input.nextDouble();

                            DBDao.addTransaction(user_id, account2, transaction_type, transfer_from, transfer_to, transaction_amount);

                            List<Transaction> transaction = DBDao.getTransactions(user_id);

                            int transaction_id = transaction.get(0).getTransactionId();

                            boolean updateFrom = DBDao.updateBalanceFrom(transaction_amount, transfer_from, transaction_id);
                            boolean updateTo = DBDao.updateBalanceTo(transaction_amount, transfer_to);

                            if(!updateFrom && !updateTo){
                                System.out.println("$" + transaction_amount + " was transferred from your " + account2 + " account to your " + account + " account.");
                                double balanceFrom = DBDao.getBalance(customer_id, transfer_from);
                                double balanceTo = DBDao.getBalance(customer_id, transfer_to);
                                System.out.println(account2 + " balance: $" + balanceFrom);
                                System.out.println(account + " balance: $" + balanceTo);
                            }
                        }
                    }
                    promptEnterKey();
                    break;
                case 4:
                    account = null;

                    while(account == null) {

                    System.out.println("Which account do you want to check the balance for?");

                    account = account();

                    }

                    if(!DBDao.accountExists(user_id, account)) {
                        System.out.println("You do not have a " + account + " account.");
                        account = null;
                    }else{
                        int account_id = DBDao.getAccountInfo(customer_id, account).getAccountId();
                        double balance = DBDao.getBalance(customer_id, account_id);
                        System.out.println(account + " account");
                        System.out.println("Balance: $" + balance);
                    }
                    promptEnterKey();

                    break;
                case 5:
                    System.out.println("These are your 5 recent transactions:");
                    List<Transaction> currentTransactions = DBDao.getTransactions(user_id);
                    if(currentTransactions.isEmpty()){
                        System.out.println("No Transactions Were Made!");
                    }else {
                        for (Transaction value : currentTransactions) {
                            if(value.getTransactionType().equals("transfer")){
                                System.out.println(value.getTransactionType() + " of $" + value.getTransactionAmount() + " to account id " + value.getTransferTo() + " from account id " + value.getTransferFrom() + " on " + value.getTransactionDate());
                            }else{
                                System.out.println(value.getTransactionType() + " of $" + value.getTransactionAmount() + " to account id " + value.getAccountId() + " on " + value.getTransactionDate());
                            }
                        }
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

    public static String account(){

        System.out.println("1. Savings");
        System.out.println("2. Checking");

        int accountChoice = input.nextInt();

        if(accountChoice == 1){
            account = "savings";
        }else if (accountChoice == 2){
            account = "checking";
        }else{
            System.out.println("Not a valid option\n");
        }
        return account;
    }

    public static void createAccount(String user_id){
        System.out.println("What kind of account do you want to create?");

        account = account();

        if(DBDao.accountExists(user_id, account)) {
            System.out.println("You already have a " + account + " account.");
            promptEnterKey();
        } else {
            System.out.println("Creating a " + account + " account.");
            System.out.println("How much do you want to deposit for the initial deposit?");
            double deposit = input.nextDouble();
            DBDao.addAccount(user_id, account, deposit);
            DBDao.addTransaction(user_id, account, "initial_deposit", 0, 0, deposit);
        }
    }

    public static void promptEnterKey(){
        System.out.println("\nPress a key to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

}
