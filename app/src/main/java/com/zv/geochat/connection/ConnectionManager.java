package com.zv.geochat.connection;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ConnectionManager {
    private static final String TAG = "ConnectionManager";
    private Socket socket;
    private final Context context;
    private final ChatEventHandler chatEventHandler;
    private String mUserName;
    private boolean connected;

    public ConnectionManager(Context context, ChatEventHandler chatEventHandler) {
        this.context = context;
        this.chatEventHandler = chatEventHandler;
    }

    public boolean isConnected() {
        return connected;
    }

    public void connectToServer(final String serverUri, String userName) {
        if(serverUri == null || serverUri.isEmpty()){
            showToast("Set Server URI in Settings/General!");
            return;
        }

        mUserName = userName; // need it when joining chat
        initServerConnection(serverUri);
    }

    public void disconnectFromServer() {
        if (socket == null) {
            showToast("Server not connected!");
            return;
        }
        closeServerConnection();
        chatEventHandler.onDisconnected(); // this is needed, since we may not catch event
    }

    private void joinChat(String userName) {
        Log.v(TAG, "Joining chat...)");
        socket.emit(ChatEvent.ADD_USER, userName);
    }

    public void leaveChat(String userName) {
        Log.v(TAG, "Leaving chat...");
        if (socket != null && socket.connected()) {
            socket.emit(ChatEvent.USER_LEFT, userName);
        } else {
            Log.d(TAG, "skip emitting [user left] event to server - server not connected");
        }
    }

    private void initServerConnection(String serverUri) {
        Log.v(TAG, "Initi connection to server: "+ serverUri);
        try {
            socket = IO.socket(serverUri);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        socket.on(Socket.EVENT_CONNECT, onConnect);
        socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);

        socket.on(ChatEvent.LOGIN, onLogin);
        socket.on(ChatEvent.USER_JOINED, onUserJoined);
        socket.on(ChatEvent.USER_LEFT, onUserLeft);
        socket.on(ChatEvent.NEW_MESSAGE, onNewMessage);
        socket.on(ChatEvent.TYPING, onTyping);
        socket.on(ChatEvent.STOP_TYPING, onStopTyping);

        socket.connect();
    }

    private void closeServerConnection() {
        socket.disconnect();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);

        socket.off(ChatEvent.LOGIN, onLogin);
        socket.off(ChatEvent.USER_JOINED, onUserJoined);
        socket.off(ChatEvent.USER_LEFT, onUserLeft);
        socket.off(ChatEvent.NEW_MESSAGE, onNewMessage);
        socket.off(ChatEvent.TYPING, onTyping);
        socket.off(ChatEvent.STOP_TYPING, onStopTyping);
    }

    public void attemptSend(String userName, String message) {
        if (socket == null || !socket.connected()) {
            showToast("Server not connected! Message not sent.");
            return;
        }
        // perform the sending message attempt.
        socket.emit(ChatEvent.NEW_MESSAGE, message);
        chatEventHandler.onAttemptSend(userName, message);
    }

    private void showToast(final String text){
        Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text,
                        Toast.LENGTH_LONG).show();
            }
        }, 1000 );
    }

    ///////////////////////////////////////////////////
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.v(TAG, "> Server connected");
            chatEventHandler.onConnected();
            connected = true;
            joinChat(mUserName);
        }
    };


    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.v(TAG, "X Server disconnected");
            chatEventHandler.onDisconnected();
            connected = false;
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.v(TAG, "Connect Error " + args[0]);
            showToast("Server connection error: " + args[0]);
            connected = false;
        }
    };
    private Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.v(TAG, "Connect Timeout " + args[0]);
            showToast("Server connection timeout: " + args[0]);
            connected = false;
        }
    };


    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[Login] event, data=" + data);
            chatEventHandler.onLogin(mUserName, data);
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[User Joined] event, data=" + data);
            chatEventHandler.onUserJoined(data);
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[User Left] event, data=" + data);
            chatEventHandler.onUserLeft(data);
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[New Message] event, data=" + data);
            chatEventHandler.onNewMessage(data);
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[Typing] event, data=" + data);
            // TODO: implement
        }
    };


    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            Log.v(TAG, "[Stop Typing] event, data=" + data);
            // TODO: implement
        }
    };
}
