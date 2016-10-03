package com.zv.geochat.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GeoChatProviderMetadata {

	public static final String AUTHORITY = "com.zv.geochat.provider.GeoChatProvider";
	public static final String DATABASE_NAME = "geochat.db";
	public static final int DATABASE_VERSION = 1;

	private GeoChatProviderMetadata() {
	}

	public static final class ChatMessageTableMetaData implements BaseColumns {
		private ChatMessageTableMetaData() {
		}

		public static final String TABLE_NAME = "chat_message";
		// uri and MIME type definitions
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + ChatMessageTableMetaData.TABLE_NAME);
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.zv.geochat.model.chatmessage";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.zv.geochat.model.chatmessage";
		public static final String DEFAULT_SORT_ORDER = "_id ASC";
		// Additional Columns start here.
		// string type
		public static final String USER_NAME = "user_name";
		public static final String MSG_BODY = "msg_body";
	}
}
