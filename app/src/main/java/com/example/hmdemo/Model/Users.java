package com.example.hmdemo.Model;

public class Users {
    private String email,name,number, password;

    public Users()
    {

    }

    public Users(String email, String name, String number, String password) {
        this.email = email;
        this.name = name;
        this.number = number;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
