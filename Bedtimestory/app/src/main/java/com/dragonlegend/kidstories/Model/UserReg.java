package com.dragonlegend.kidstories.Model;

public class UserReg {
    private  String first_name;
    private String last_name;
    private String email;
    private String phone;
    private String Password;


    public UserReg(String first_name, String last_name, String email, String phone, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        Password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return Password;
    }
}
