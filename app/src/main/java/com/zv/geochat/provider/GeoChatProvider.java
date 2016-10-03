package com.zv.geochat.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.zv.geochat.provider.GeoChatProviderMetadata.*;

import java.util.HashMap;


public class GeoChatProvider extends ContentProvider {
    private static final String TAG = "GeoChatProvider";

    private static HashMap<String, String> sGroupsProjectionMap;

    static {
        sGroupsProjectionMap = new HashMap<String, String>();
        sGroupsProjectionMap.put(ChatMessageTableMetaData._ID, ChatMessageTableMetaData._ID);
        sGroupsProjectionMap.put(ChatMessageTableMetaData.USER_NAME, ChatMessageTableMetaData.USER_NAME);
        sGroupsProjectionMap.put(ChatMessageTableMetaData.MSG_BODY, ChatMessageTableMetaData.MSG_BODY);
    }


    // identification of all the incoming uri patterns
    private static final UriMatcher sUriMatcher;
    private static final int CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR = 1;
    private static final int CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR = 2;


    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // groups
        sUriMatcher.addURI(GeoChatProviderMetadata.AUTHORITY, ChatMessageTableMetaData.TABLE_NAME,
                CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(GeoChatProviderMetadata.AUTHORITY, ChatMessageTableMetaData.TABLE_NAME + "/#",
                CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR);
    }

    private DatabaseHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        Log.d(TAG, "main onCreate called");
        if (mOpenHelper == null) {
            mOpenHelper = new DatabaseHelper(getContext());
        }
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR:
                return ChatMessageTableMetaData.CONTENT_TYPE;

            case CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR:
                return ChatMessageTableMetaData.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String defaultSortOrder;

        switch (sUriMatcher.match(uri)) {
            case CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR:
                qb.setTables(ChatMessageTableMetaData.TABLE_NAME);
                qb.setProjectionMap(sGroupsProjectionMap);
                defaultSortOrder = ChatMessageTableMetaData.DEFAULT_SORT_ORDER;
                break;

            case CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR:
                qb.setTables(ChatMessageTableMetaData.TABLE_NAME);
                qb.setProjectionMap(sGroupsProjectionMap);
                qb.appendWhere(ChatMessageTableMetaData._ID + "="
                        + uri.getPathSegments().get(1));
                defaultSortOrder = ChatMessageTableMetaData.DEFAULT_SORT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // If no sort order is specified use the default
        String orderBy = getSortOrder(sortOrder, defaultSortOrder);

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, orderBy);
        // int i = c.getCount(); //example of getting a count

        // Tell the cursor what uri to watch,  so it knows when its source data changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private String getSortOrder(String sortOrder, String defaultSortOrder) {
        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = defaultSortOrder;
        } else {
            orderBy = sortOrder;
        }
        return orderBy;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId;
        switch (match) {
            case CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR:
                rowId = db.insert(ChatMessageTableMetaData.TABLE_NAME,
                        ChatMessageTableMetaData.USER_NAME, values);
                Uri insertedChatMessageUri = ContentUris.withAppendedId(
                        ChatMessageTableMetaData.CONTENT_URI, rowId);
                notifyChange(insertedChatMessageUri);
                return insertedChatMessageUri;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private void notifyChange(Uri uri) {
        Context context = getContext();
        context.getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String rowId;
        switch (sUriMatcher.match(uri)) {
            case CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR:
                count = db.delete(ChatMessageTableMetaData.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
                count = db.delete(ChatMessageTableMetaData.TABLE_NAME,
                        ChatMessageTableMetaData._ID + "=" + rowId
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        notifyChange(uri);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        String rowId;
        switch (sUriMatcher.match(uri)) {
            case CHAT_MESSAGE_INCOMING_COLLECTION_URI_INDICATOR:
                count = db.update(ChatMessageTableMetaData.TABLE_NAME,
                        values, selection, selectionArgs);
                break;

            case CHAT_MESSAGE_INCOMING_SINGLE_ITEM_URI_INDICATOR:
                rowId = uri.getPathSegments().get(1);
                count = db.update(ChatMessageTableMetaData.TABLE_NAME,
                        values, ChatMessageTableMetaData._ID + "=" + rowId
                                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        notifyChange(uri);
        return count;
    }
}
