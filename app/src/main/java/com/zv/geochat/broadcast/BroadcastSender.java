package com.zv.geochat.broadcast;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zv.geochat.Constants;

public class BroadcastSender {
    private static final String TAG ="BroadcastSender";
    private final Context context;

    public BroadcastSender(Context context){
        this.context = context;
    }

    public void sendUserJoined(String userName, int userCount) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_USER_JOINED");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_USER_JOINED);

        Bundle data = new Bundle();
        data.putInt(Constants.CHAT_USER_COUNT, userCount);
        data.putString(Constants.CHAT_USER_NAME, userName);
        intent.putExtras(data);

        context.sendBroadcast(intent);
    }

    public void sendUserLeft(String userName, int userCount) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_USER_LEFT");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_USER_LEFT);

        Bundle data = new Bundle();
        data.putInt(Constants.CHAT_USER_COUNT, userCount);
        data.putString(Constants.CHAT_USER_NAME, userName);
        intent.putExtras(data);

        context.sendBroadcast(intent);
    }


    public void sendNewMessage(String userName, String message) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_NEW_MESSAGE");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_NEW_MESSAGE);

        Bundle data = new Bundle();
        data.putString(Constants.CHAT_MESSAGE, message);
        data.putString(Constants.CHAT_USER_NAME, userName);
        intent.putExtras(data);

        context.sendBroadcast(intent);
    }

    public void sendNotConnected() {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_NOT_CONNECTED");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_SERVER_NOT_CONNECTED);
        context.sendBroadcast(intent);
    }

    public void sendConnected() {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_CONNECTED");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_SERVER_CONNECTED);

        context.sendBroadcast(intent);
    }

    public void sendTyping(String userName) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_USER_TYPING");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_USER_TYPING);
        context.sendBroadcast(intent);
    }

    public void sendStopTyping(String userName) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_USER_STOP_TYPING");
        Intent intent = new Intent();
        intent.setAction(Constants.BROADCAST_USER_STOP_TYPING);
        context.sendBroadcast(intent);
    }
}
