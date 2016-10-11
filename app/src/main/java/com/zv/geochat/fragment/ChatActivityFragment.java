package com.zv.geochat.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zv.geochat.Constants;
import com.zv.geochat.R;
import com.zv.geochat.adapter.ChatBubbleAdapter;
import com.zv.geochat.location.MyLocationProvider;
import com.zv.geochat.model.ChatMessage;
import com.zv.geochat.model.ChatMessageBody;
import com.zv.geochat.service.ChatService;

import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;

public class ChatActivityFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String TAG = "ChatActivityFragment";
    private EditText edtMessage;
    private ListView messageListView;
    private ChatBubbleAdapter adapter;
    private MyLocationProvider myLocationProvider;
    private boolean shareLocation;


    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_send_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage(edtMessage.getText().toString());
                edtMessage.getText().clear();
            }
        });

        edtMessage = (EditText) v.findViewById(R.id.edtMessage);
        messageListView = (ListView)v.findViewById(R.id.messageList);
        adapter = new ChatBubbleAdapter(getActivity(), new ArrayList<ChatMessage>());
        messageListView.setAdapter(adapter);
        myLocationProvider = new MyLocationProvider(getContext());
        loadPreferences();
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerServiceStateChangeReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mServiceStateChangeReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        myLocationProvider.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        myLocationProvider.connect();
    }

    private void sendMessage(String messageText){

        ChatMessageBody msgBody = null;
        if (shareLocation && myLocationProvider.getLastLocation()!= null){
            double longitude = myLocationProvider.getLastLocation().getLongitude();
            double latitude = myLocationProvider.getLastLocation().getLatitude();
            msgBody = new ChatMessageBody(messageText, longitude, latitude);
            Snackbar.make(getView(), "Location - lng: " + longitude +", lat: " + latitude,
                    Snackbar.LENGTH_LONG).show();
        } else {
            msgBody = new ChatMessageBody(messageText);
        }

        Bundle data = new Bundle();
        data.putInt(ChatService.CMD, ChatService.CMD_SEND_MESSAGE);
        data.putString(ChatService.KEY_MESSAGE_TEXT, msgBody.toJson());
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);

    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        messageListView.setSelection(messageListView.getCount() - 1);
    }



    //------- listening broadcasts from service
    /**
     * Listens for service state change broadcasts
     */
    private final BroadcastReceiver mServiceStateChangeReceiver = new BroadcastReceiver() {
        private static final String TAG = "BroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            Log.d(TAG, "received broadcast message from service: " + action);

            if (Constants.BROADCAST_SERVER_CONNECTED.equals(action)) {
                ChatMessageBody msgBody = new ChatMessageBody("Connected");
                ChatMessage chatMessage = new ChatMessage("Status: ", msgBody, true);
                displayMessage(chatMessage);
            } else if (Constants.BROADCAST_SERVER_NOT_CONNECTED.equals(action)) {
                ChatMessageBody msgBody = new ChatMessageBody("Disconnected");
                ChatMessage chatMessage = new ChatMessage("Status: ", msgBody, true);
                displayMessage(chatMessage);
            } else if (Constants.BROADCAST_USER_JOINED.equals(action)) {
                String userName = data.getString(Constants.CHAT_USER_NAME);
                int userCount = data.getInt(Constants.CHAT_USER_COUNT, 0);
                ChatMessageBody msgBody = new ChatMessageBody(" joined. Users: "+userCount);
                ChatMessage chatMessage = new ChatMessage(userName, msgBody, true);
                displayMessage(chatMessage);
            } else if (Constants.BROADCAST_USER_LEFT.equals(action)) {
                String userName = data.getString(Constants.CHAT_USER_NAME);
                int userCount = data.getInt(Constants.CHAT_USER_COUNT, 0);
                ChatMessageBody msgBody = new ChatMessageBody(" left. Users: "+userCount);
                ChatMessage chatMessage = new ChatMessage(userName, msgBody, true);
                displayMessage(chatMessage);
            } else if (Constants.BROADCAST_NEW_MESSAGE.equals(action)) {
                String userName = data.getString(Constants.CHAT_USER_NAME);
                String message = data.getString(Constants.CHAT_MESSAGE);
                ChatMessageBody msgBody = ChatMessageBody.fromJson(message);
                ChatMessage chatMessage = new ChatMessage(userName, msgBody);
                displayMessage(chatMessage);
            } else if (Constants.BROADCAST_USER_TYPING.equals(action)) {
                // TODO
            } else {
                Log.v(TAG, "do nothing for action: " + action);
            }
        }
    };


    private void registerServiceStateChangeReceiver() {
        Log.d(TAG, "registering service state change receiver...");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.BROADCAST_NEW_MESSAGE);
        intentFilter.addAction(Constants.BROADCAST_USER_TYPING);
        intentFilter.addAction(Constants.BROADCAST_SERVER_CONNECTED);
        intentFilter.addAction(Constants.BROADCAST_SERVER_NOT_CONNECTED);
        intentFilter.addAction(Constants.BROADCAST_USER_JOINED);
        intentFilter.addAction(Constants.BROADCAST_USER_LEFT);
        getActivity().registerReceiver(mServiceStateChangeReceiver, intentFilter);
    }

    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        shareLocation = prefs.getBoolean(Constants.PREF_KEY_SHARE_LOCATION, false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.v(TAG, "onSharedPreferenceChanged() key=" + key);
        loadPreferences();
    }
}
