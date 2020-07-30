drop database if exists `dollarsbank_db`;

CREATE DATABASE `dollarsbank_db` ;

use dollarsbank_db;

drop table if exists transaction;
drop table if exists account;
drop table if exists customer;

CREATE TABLE if not exists customer(

    customer_id int auto_increment PRIMARY KEY,
    userid varchar(25) UNIQUE NOT NULL,
    password varchar(25) NOT NULL,
    name varchar(50) NOT NULL,
    phone varchar(25) UNIQUE NOT NULL,
    address varchar(100) NOT NULL
);

CREATE TABLE if not exists account(
   account_id int auto_increment PRIMARY KEY,
   customer_id int,
   account_type varchar(10) not null,
   account_creation datetime not null,
   initial_deposit double not null ,
   account_balance double not null,
   constraint fk_customer_id
       foreign key (customer_id)
           references customer(customer_id)
);

CREATE TABLE if not exists transaction(
    transaction_id int auto_increment PRIMARY KEY,
    account_id int,
    transaction_date datetime not null,
    transaction_type varchar(15) not null,
    transfer_from int,
    transfer_to int,
    transaction_amount double not null,
    constraint fk_account_id
        foreign key (account_id)
            references account(account_id)
);
