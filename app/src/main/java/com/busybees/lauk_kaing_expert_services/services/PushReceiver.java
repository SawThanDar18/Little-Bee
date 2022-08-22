package com.busybees.lauk_kaing_expert_services.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.busybees.lauk_kaing_expert_services.MainActivity;
import com.busybees.lauk_kaing_expert_services.R;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String notificationTitle = intent.getStringExtra("title");
        String notificationText = "BusyBees";

        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
            /*NotiCallModel notiCallModel = new NotiCallModel();
            notiCallModel.setRecall("call");
            EventBus.getDefault().post(notiCallModel);*/

        }

        if (intent.getStringExtra("more") != null) {
            Log.e("notireceiver>>>>", intent.getStringExtra("more"));
            String hello =intent.getStringExtra("more");
            Intent notificationIntent = new Intent(context, MainActivity.class);

            notificationIntent.putExtra("notification_condition", hello);
            Log.e("hello>>>>", hello);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent resultIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder
                    (context, default_notification_channel_id)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setContentIntent(resultIntent);

            mBuilder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager mNotificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new
                        NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
                mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
            assert mNotificationManager != null;
            mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
        }

    }

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


}
