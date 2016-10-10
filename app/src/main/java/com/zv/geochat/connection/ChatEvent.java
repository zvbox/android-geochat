package com.zv.geochat.connection;

interface ChatEvent {
    String LOGIN = "login";
    String ADD_USER = "add user";
    String NEW_MESSAGE = "new message";
    String USER_JOINED = "user joined";
    String USER_LEFT = "user left";
    String TYPING = "typing";
    String STOP_TYPING = "stop typing";
}
