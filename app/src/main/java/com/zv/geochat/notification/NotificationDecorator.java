package com.zv.geochat.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import com.zv.geochat.ChatActivity;
import com.zv.geochat.R;

public class NotificationDecorator {

    private static final String TAG = "NotificationDecorator";
    public static final String CHANNEL_ID = "geoChatChannel_1";
    private final Context context;
    private final NotificationManager notificationMgr;

    public NotificationDecorator(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationMgr = notificationManager;
        createChannel(notificationManager, CHANNEL_ID);
    }

    private void createChannel(NotificationManager notificationManager, String channelId){
        NotificationChannel notificationChannel = new NotificationChannel(channelId, "GeoChat Channel",
                NotificationManager.IMPORTANCE_LOW);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.GREEN);
        notificationChannel.enableVibration(true);
        //notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(notificationChannel);
    }

    public void displaySimpleNotification(String title, String contentText) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // notification message
        try {


            Notification noti = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(contentText)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .build();

            noti.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationMgr.notify(0, noti);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
