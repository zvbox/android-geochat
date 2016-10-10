package com.zv.geochat.connection;

/**
 * Keys are specific to implementation of
 * http://chat.socket.io chat server
 * see http://socket.io/blog/native-socket-io-and-android/
 */
interface ChatMessageKey {
    String MESSAGE = "message";
    String NUM_USERS = "numUsers";
    String USER_NAME = "username";
}
