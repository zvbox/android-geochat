package com.zv.geochat.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.zv.geochat.model.ChatMessage;
import com.zv.geochat.model.ChatMessageBody;
import com.zv.geochat.provider.GeoChatProviderMetadata.*;

import java.util.ArrayList;
import java.util.List;


public class ChatMessageStore {

	private static final String TAG = "ChatMessageStore";
	private Context context;

	public ChatMessageStore(Context context) {
		this.context = context;
	}

	public List<ChatMessage> getList() {
		Log.v(TAG, "{db} get list");
		List<ChatMessage> chatMessageList = null;
		Uri uri = ChatMessageTableMetaData.CONTENT_URI;
		ContentResolver contentResolver = context.getContentResolver();
		Cursor c = null;
		try {
			c = contentResolver.query(uri,
					null, //projection
					null, //selection string
					null, //selection args array of strings
					null); //sort order
			chatMessageList = getChatMessageList(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}
		Log.v(TAG, "{db} get list result size: " + chatMessageList.size());
		return chatMessageList;
	}

	public ChatMessage getById(String id) {

		List<ChatMessage> chatMessageList = null;

		Uri uri = ChatMessageTableMetaData.CONTENT_URI;
		ContentResolver contentResolver = context.getContentResolver();
		String where = ChatMessageTableMetaData._ID + "=\'" + id + "\'";
		Log.v(TAG, "{db} WHERE: " + where);

		Cursor c = null;
		try {
			c = contentResolver.query(uri,
					null, //projection
					where, //selection string
					null, //selection args array of strings
					null); //sort order
			chatMessageList = getChatMessageList(c);
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return chatMessageList.get(0);
	}

	private List<ChatMessage> getChatMessageList(Cursor c) {
		List<ChatMessage> list = new ArrayList<>();
		int indexId = c.getColumnIndex(ChatMessageTableMetaData._ID);
		int indexUserName = c.getColumnIndex(ChatMessageTableMetaData.USER_NAME);
		int indexMsgBody = c.getColumnIndex(ChatMessageTableMetaData.MSG_BODY);

		//walk through the rows based on indexes
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setId(c.getString(indexId));
			chatMessage.setUserName(c.getString(indexUserName));
			String body = c.getString(indexMsgBody);
            ChatMessageBody msgBody = null;
            try{
                msgBody = ChatMessageBody.fromJson(body);
                if(msgBody == null){
                    msgBody = new ChatMessageBody("");
                }
            } catch (JsonSyntaxException jse){
                // non json format, should not happen if db is clean
                // of old records - previous milestones
                msgBody = new ChatMessageBody(body);
            }

			chatMessage.setBody(msgBody);
			list.add(chatMessage);
			//Log.v(TAG, "getChatMessageList -- found: " + chatMessage);
		}
		return list;
	}

	public void update(ChatMessage chatMessage) {
		Log.v(TAG, "{db} update chatMessage " + chatMessage);
		ContentResolver contentResolver = context.getContentResolver();
		ContentValues cv = new ContentValues();
		cv.put(ChatMessageTableMetaData.USER_NAME, chatMessage.getUserName());
		cv.put(ChatMessageTableMetaData.MSG_BODY, chatMessage.getBody().toJson());

		Uri uri = ChatMessageTableMetaData.CONTENT_URI;
		Log.v(TAG, "{db} insert uri: " + uri);
		String where = ChatMessageTableMetaData._ID+ "=\'" + chatMessage.getId() + "\'";
		int numRowsUpdated = contentResolver.update(uri, cv, where, null);
		Log.v(TAG, "{db} updated rows count: " + numRowsUpdated);
		if (numRowsUpdated == 0) {
			Log.v(TAG, "{db} peer is not in db, need to insert new" );
			insert(chatMessage);
		}
	}

	public void insert(ChatMessage chatMessage) {
		ContentResolver contentResolver = context.getContentResolver();
		// add to db
		Log.v(TAG, "{db} +++++ add to db.... " + chatMessage);
		ContentValues cv = new ContentValues();
		cv.put(GeoChatProviderMetadata.ChatMessageTableMetaData.USER_NAME, chatMessage.getUserName());
		cv.put(GeoChatProviderMetadata.ChatMessageTableMetaData.MSG_BODY, chatMessage.getBody().toJson());

		Uri uri = GeoChatProviderMetadata.ChatMessageTableMetaData.CONTENT_URI;
		Log.v(TAG, "{db} insert uri: " + uri);
		Uri insertedUri = contentResolver.insert(uri, cv);
		Log.v(TAG, "{db} inserted uri: " + insertedUri);
	}

	public void deleteById(String id) {

		Log.v(TAG, "{db} delete from db.... id=" + id);
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = GeoChatProviderMetadata.ChatMessageTableMetaData.CONTENT_URI;
		Log.v(TAG, "{db} delete uri: " + uri);
		String where = GeoChatProviderMetadata.ChatMessageTableMetaData._ID + "=\'" + id + "\'";
		Log.v(TAG, "{db} WHERE: " + where);
		contentResolver.delete(uri, where, null);
	}
}
