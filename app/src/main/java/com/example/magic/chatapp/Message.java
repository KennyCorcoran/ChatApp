package com.example.magic.chatapp;
// Kevin Corcoran C00110665

import java.util.Date;

public class Message {
    private String content, userName;
   // private Date time;

    public Message(){

    }

    public Message(String content, String userName/*, Date time*/){

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
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
 */
}
