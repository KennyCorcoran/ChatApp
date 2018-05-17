package com.example.magic.chatapp;
// Name: Kevin Corcoran
// Student No: C00110665

import java.util.Date;

public class Message {
    private String content, userName;
    private String time;

    public Message(){

    }

    public Message(String content, String userName, String time){

        this.content = content;
        this.userName = userName;
        this.time = time;

    }
    public String getContent(){

        return content;
    }


    public String getUserName() {
        return userName;
    }

   

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
