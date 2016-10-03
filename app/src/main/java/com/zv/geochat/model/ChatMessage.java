package com.zv.geochat.model;

public class ChatMessage {
    private String id;
    private String userName;
    private String body;

    public ChatMessage() {
    }

    public ChatMessage(String userName, String body) {
        this.userName = userName;
        this.body = body;
    }

    public ChatMessage(String id, String userName, String body) {
        this.id = id;
        this.userName = userName;
        this.body = body;
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
                '}';
    }
}
