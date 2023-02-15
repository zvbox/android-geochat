package com.zv.geochat.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zv.geochat.R;
import com.zv.geochat.model.ChatMessage;

import java.util.List;

import co.dift.ui.SwipeToAction;


public class ChatMessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> items;


    /** References to the views for each data item **/
    public class ChatMessageViewHolder extends SwipeToAction.ViewHolder<ChatMessage> {
        public TextView userName;
        public TextView chatMessageBody;
        public ImageView imageView;
        //Declaring Message ID and Message Date for Assignment 2
        public TextView messageID;
        public TextView messageDate;

        public ChatMessageViewHolder(View v) {
            super(v);

            userName = (TextView) v.findViewById(R.id.userName);
            chatMessageBody = (TextView) v.findViewById(R.id.body);
            imageView = (ImageView) v.findViewById(R.id.image);
            //create to map the text view for date field and id here - Assignment 2
            messageID = (TextView) v.findViewById(R.id.msgID);
            messageDate = (TextView) v.findViewById(R.id.msgDate);
        }
    }

    /** Constructor **/
    public ChatMessagesAdapter(List<ChatMessage> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage item = items.get(position);
        ChatMessageViewHolder vh = (ChatMessageViewHolder) holder;
        vh.userName.setText(item.getUserName());
        vh.chatMessageBody.setText(item.getBody());
        vh.data = item;
        //doing the same for id and date - Assignment 2
        vh.messageID.setText("  Message ID: " + item.getId());
        vh.messageDate.setText("    " + item.getChatDate());
    }
}