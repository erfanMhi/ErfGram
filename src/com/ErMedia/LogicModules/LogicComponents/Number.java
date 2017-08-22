package com.ErMedia.LogicModules.LogicComponents;

import java.util.ArrayList;

/**
 * Created by root on 1/5/17.
 */
public class Number {
    private long number;
    private String user;
    private ArrayList<Message> messages;

    public Number(long number,String user){
        this.number = number;
        this.user = user;
        messages = new ArrayList();
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
