package com.zv.geochat.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChatMessage {
    private String id;
    private String userName;
    private String body;
    private String date;

    public ChatMessage() {
    }

    public ChatMessage(String userName, String body) {
        this.userName = userName;
        this.body = body;
    }

    public ChatMessage(String id, String userName, String body, String date) { //Assignment 2 - Date defining
        this.id = id;
        this.userName = userName;
        this.body = body;
        this.date = date; //Assignment 2 - Date defining
        //adding date here might not be necessary
    }


    public String getUserName() {
        return userName;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }

    //create a getter and setter function for date - Assignment 2
    ////
    public String getDate()
    {
        DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        date = simpleDateFormat.format(calendar.getTime());
        return date;
    }

    public String getChatDate() { return date; } //Using to send the exact date of chat from DB

    public void setDate(String date) { this.date = date; }
    ////

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", body='" + body + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
