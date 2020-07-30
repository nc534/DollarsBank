package com.dollarsbank.model;

public class Customer {
    private int customer_id;
    private String user_id;
    private String password;
    private String name;
    private String phone;
    private String address;

    public Customer() {

    }

    public Customer(String user_id, String password, String name, String phone, String address) {
        this.user_id = user_id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
