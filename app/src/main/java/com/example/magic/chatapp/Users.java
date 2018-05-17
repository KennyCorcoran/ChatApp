package com.example.magic.chatapp;
// Name: Kevin Corcoran
// Student No: C00110665

public class Users {

    private String user;

    public Users(){

    }

    public Users(String userName){

        this.user = userName;

    }

    public void setUserName(String userName) {
        this.user = userName;   //sets username as current user name
    }

    public String getUserName() {
        return user;
    }

}
