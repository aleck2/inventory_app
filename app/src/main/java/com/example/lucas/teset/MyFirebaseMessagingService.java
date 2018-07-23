package com.example.lucas.teset;

/**
 * Created by Lucas on 7/16/18.
 */
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.*;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Evens are off
    // Odds are on
    static final long[] DEFAULT_VIBRATE_PATTERN = {0, 600, 250, 250};
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Intent myIntent = new Intent(MyFirebaseMessagingService.this, ViewNotification.class);
        sendNotification(remoteMessage.getNotification(), data);
        for(Map.Entry<String, String> entry: data.entrySet()) {
            Log.i("Messaging Service", entry.getKey() + " : " + entry.getValue());

                if (entry.getKey().equals("title") || entry.getKey().equals("price") || entry.getKey().equals("url")) {
                    Object value = entry.getValue();
                    myIntent.putExtra(entry.getKey(), (String) value);
                }
            }

            startActivity(myIntent);
        }

    /**
     * Create and show a custom notification containing the received FCM message.
     *
     * @param notification FCM notification payload received.
     * @param data FCM data payload received.
     */
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
//        Intent intent = new Intent(this, ViewNotification.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);

        Intent myIntent = new Intent(MyFirebaseMessagingService.this, ViewNotification.class);
        MyFirebaseMessagingService.this.startActivity(myIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification Channel is required for Android O and above
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationChannel channel = new NotificationChannel("myId", getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("very special channel description");
        channel.setShowBadge(true);
        channel.canShowBadge();
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.setVibrationPattern(DEFAULT_VIBRATE_PATTERN);
        channel.enableVibration(true);
        channel.enableVibration(false);
        notificationManager.createNotificationChannel(channel);
        createNotification();
//        }

    }

    public void createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "myId")
                .setSmallIcon(R.mipmap.kettlebell)
                //.setOnlyAlertOnce(true)
                .setContentTitle("Cha-Ching")
                .setContentText("Item Found"); // TODO put item title here
        Notification n = mBuilder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, n);

    }


}
