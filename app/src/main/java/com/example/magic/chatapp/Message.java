package com.example.magic.chatapp;
// Kevin Corcoran C00110665

public class Message {
    private String content, userName;

    public Message(){

    }

    public Message(String content, String userName){

        this.content = content;
        this.userName = userName;
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
}
