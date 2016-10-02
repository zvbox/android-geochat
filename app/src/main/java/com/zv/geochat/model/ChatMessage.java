package com.zv.geochat.model;

public class ChatMessage {
    private String userName;
    private String body;

    public ChatMessage(String userName, String body) {
        this.userName = userName;
        this.body = body;
    }

    public String getUserName() {
        return userName;
    }

    public String getBody() {
        return body;
    }
}
