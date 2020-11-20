package com.synergy.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.synergy.R;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.NotNull;

public class MyFirebaseInstanceService extends FirebaseMessagingService {


        @Override
        public void onNewToken(@NotNull String token) {
            super.onNewToken(token);
            Log.e("newToken", token);
            Log.d("tag", "onNewToken: "+token);
            getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);

            String title=remoteMessage.getNotification().getTitle();
            String message=remoteMessage.getNotification().getBody();
            String click_action=remoteMessage.getNotification().getClickAction();


            /*NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.notification_icon);
            //notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());*/

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.notification_icon)
                    .build();

            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify(0, notification);
        }


    }
