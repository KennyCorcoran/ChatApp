package com.example.magic.chatapp;
// Kevin Corcoran C00110665

import java.util.Date;

public class Message {
    private String content, userName;
   // private String time;

    public Message(){

    }

    public Message(String content, String userName, String time){

        this.content = content;
        this.userName = userName;
       // this.time = time;

    }
    public String getContent(){

        return content;
    }
    public void setContent(String content){
        this.content = content; //sets content as current content
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;   //sets username as current user name
    }
/*
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
*/
}
