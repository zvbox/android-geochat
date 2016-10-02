package com.zv.geochat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.zv.geochat.model.ChatMessage;
import com.zv.geochat.ui.adapter.ChatMessagesAdapter;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ChatMessagesAdapter adapter;
    SwipeToAction swipeToAction;

    List<ChatMessage> chatMessages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //----------------------------
        // facebook image library
        Fresco.initialize(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new ChatMessagesAdapter(this.chatMessages);
        recyclerView.setAdapter(adapter);

        swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener<ChatMessage>() {
            @Override
            public boolean swipeLeft(final ChatMessage itemData) {
                final int pos = removeBook(itemData);
                displaySnackbar(itemData.getUserName() + " removed", "Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addBook(pos, itemData);
                    }
                });
                return true;
            }

            @Override
            public boolean swipeRight(ChatMessage itemData) {
                //displaySnackbar(itemData.getUserName() + " loved", null, null);
                return true;
            }

            @Override
            public void onClick(ChatMessage itemData) {
                displaySnackbar(itemData.getUserName() + " clicked", null, null);
            }

            @Override
            public void onLongClick(ChatMessage itemData) {
                displaySnackbar(itemData.getUserName() + " long clicked", null, null);
            }
        });


        populate();
        //----------------------------
    }

    private void populate() {
        this.chatMessages.add(new ChatMessage("Peter", "Hi, my name is Peter!"));
        this.chatMessages.add(new ChatMessage("John", "Hi, my name is John!"));
        this.chatMessages.add(new ChatMessage("Rose", "Hi, my name is Rose!"));
        this.chatMessages.add(new ChatMessage("Amanda", "Hi, my name is Amanda!"));
        this.chatMessages.add(new ChatMessage("Mike", "Hi, my name is Mike!"));
    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action))
                .setTextColor(getResources().getColor(R.color.colorAccent));

        snack.show();
    }

    private int removeBook(ChatMessage chatMessage) {
        int pos = chatMessages.indexOf(chatMessage);
        chatMessages.remove(chatMessage);
        adapter.notifyItemRemoved(pos);
        return pos;
    }

    private void addBook(int pos, ChatMessage chatMessage) {
        chatMessages.add(pos, chatMessage);
        adapter.notifyItemInserted(pos);
    }

}
