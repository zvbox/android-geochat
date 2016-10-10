package com.zv.geochat;

public interface Constants {

    // preferences
    String PREF_KEY_USER_NAME ="key_user_name";
    String PREF_KEY_SERVER_URI ="key_server_uri";
    String PREF_KEY_SHARE_LOCATION ="key_share_location";
    String PREF_KEY_NOTIFICATIONS_NEW_MESSAGE ="key_notifications_new_message";
    String PREF_KEY_NOTIFICATIONS_NEW_MESSAGE_RINGTONE ="key_notifications_new_message_ringtone";
    String PREF_KEY_NOTIFICATIONS_NEW_MESSAGE_VIBRATE ="key_notifications_new_message_vibrate";

    // broadcasts
    String BASE = "com.zv.geochat.";
    String BROADCAST_NEW_MESSAGE = BASE + "NEW_MESSAGE";
    String BROADCAST_SERVER_CONNECTED = BASE + "SERVER_CONNECTED";
    String BROADCAST_SERVER_NOT_CONNECTED = BASE + "SERVER_NOT_CONNECTED";
    String BROADCAST_USER_TYPING = BASE + "USER_TYPING";
    String BROADCAST_USER_STOP_TYPING = BASE + "USER_STOP_TYPING";
    String BROADCAST_USER_LEFT = BASE + "USER_LEFT";
    String BROADCAST_USER_JOINED = BASE + "USER_JOINED";

    String CHAT_USER_COUNT = "CHAT_USER_COUNT";
    String CHAT_USER_NAME = "CHAT_USER_NAME";
    String CHAT_MESSAGE = "CHAT_MESSAGE";

}
