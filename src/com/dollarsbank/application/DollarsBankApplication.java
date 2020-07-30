package com.dollarsbank.application;

import com.dollarsbank.dao.DollarsBankDao;
import com.dollarsbank.dao.DollarsBankDaoImplementation;
import com.dollarsbank.model.Customer;

import java.util.Scanner;

public class DollarsBankApplication {

    public static void main(String[] args) throws ClassNotFoundException {

        System.out.println("+---------------------------+");
        System.out.println("| DOLLARSBANK Welcomes You! |");
        System.out.println("+---------------------------+");

        System.out.println("1. Create New Customer Account");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        System.out.println();

        Scanner input = new Scanner(System.in);

        System.out.println("Enter Choice (1, 2, or 3):");
        int choice = input.nextInt();

        DollarsBankDaoImplementation DBDao = new DollarsBankDaoImplementation();
        Customer customer = new Customer();

        switch(choice) {
            case 1:
                int result = 0;
                result = DBDao.registerCustomer(customer);

                if(result != 0){
                    System.out.println("Customer Account Created!");
                }else{
                    System.out.println("Customer Account Failed to Create!");
                }

                break;
            case 2:
                boolean customerexists = false;
                customerexists = DBDao.findCustomer(customer);

                do {
                    if (customerexists) {
                        System.out.println("Logged In");
                    } else {
                        System.out.println("Invalid Credentials. Try Again!");
                        customerexists = DBDao.findCustomer(customer);
                    }
                }while(!customerexists);

                break;
            case 3:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
        }

    }
}
