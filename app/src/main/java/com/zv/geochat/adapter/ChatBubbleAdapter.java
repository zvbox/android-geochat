package com.zv.geochat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zv.geochat.R;
import com.zv.geochat.model.ChatMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatBubbleAdapter extends BaseAdapter {

    private final List<ChatMessage> chatMessages;
    private Activity context;

    public ChatBubbleAdapter(Activity context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatMessage getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (chatMessage.isStatusUpdate()){
            holder.bubbleMessageContainer.setVisibility(View.GONE);
            holder.txtUser.setText(chatMessage.getUserName() + chatMessage.getBody());
            // TODO: use a proper way - should be a part of message data
            String messageDateTime = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
            holder.txtInfo.setText(messageDateTime);
        } else {
            holder.bubbleMessageContainer.setVisibility(View.VISIBLE);
            holder.txtMessage.setText(chatMessage.getBody());
            holder.txtUser.setText(chatMessage.getUserName());
            // TODO: use a proper way - should be a part of message data
            String messageDateTime = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(Calendar.getInstance().getTime());
            holder.txtInfo.setText(messageDateTime);
        }

        return convertView;
    }

    public void add(ChatMessage message) {
        chatMessages.add(message);
    }

    public void add(List<ChatMessage> messages) {
        chatMessages.addAll(messages);
    }


    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.bubbleMessageContainer = (LinearLayout)v.findViewById(R.id.bubbleMessageContainer);
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.txtUser= (TextView) v.findViewById(R.id.txtUser);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }


    private static class ViewHolder {
        public LinearLayout bubbleMessageContainer;
        public TextView txtMessage;
        public TextView txtUser;
        public TextView txtInfo;
    }
}
