package com.synergy.Services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.synergy.APIClient;
import com.synergy.Constants;
import com.synergy.EquipmentSearch.EquipmentSearchActivity;
import com.synergy.EquipmentSearch.PmTaskActivity;
import com.synergy.MainActivityLogin;
import com.synergy.R;
import com.synergy.Search.EditFaultOnSearchActivity;
import com.synergy.Search.EditFaultReportActivity;
import com.synergy.Workspace.WorkspaceActivity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.synergy.MainActivityLogin.SHARED_PREFS;

public class MyFirebaseInstanceService extends FirebaseMessagingService {
    private FusedLocationProviderClient client;
    double longitude, latitude;

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        client = LocationServices.getFusedLocationProviderClient(MyFirebaseInstanceService.this);
        locationMethod();
        Log.d("TEST123", "onMessageReceived: " + remoteMessage.getData());

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("message");
        String click_action = remoteMessage.getData().get("click_action");
        String workspace = remoteMessage.getData().get("workspace");
        String id = remoteMessage.getData().get("id");
        String equipCode = "";
        String taskNumber = "";
        String afterImage = "";
        String beforeImage = "";
        String source = "";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Intent intent = null;

        if (click_action.equals(Constants.EDITFAULTREPORT_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(this, EditFaultOnSearchActivity.class);
            //     intent.putExtra("equipcode", equipCode);
            intent.putExtra("frId", id);
            intent.putExtra("workspaceId", workspace);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);

        }
        if (click_action.equals(Constants.PMTASK_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(this, PmTaskActivity.class);
            intent.putExtra("taskId", Integer.parseInt(id));
            intent.putExtra("taskNumber", taskNumber);
            intent.putExtra("afterImage", afterImage);
            intent.putExtra("beforeImage", beforeImage);
            intent.putExtra("source", source);
            intent.putExtra("workspace", workspace);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "my_channel_id";
        CharSequence channelName = "My Channel";
        int importance = 0;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_MAX;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.circularcmmslogo)
               .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }

    @SuppressLint("MissingPermission")
    private void locationMethod() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }
        });
    }
}

/*


public class MyFirebaseInstanceService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("TEST123", "onMessageReceived: " + remoteMessage.getData());

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("message");
        String click_action = remoteMessage.getData().get("click_action");
        String workspace = remoteMessage.getData().get("workspace");
        String id = remoteMessage.getData().get("id");
        String equipCode = "";
        String taskNumber = "";
        String afterImage = "";
        String beforeImage = "";
        String source = "";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = null;

        if (click_action.equals(Constants.EDITFAULTREPORT_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(this, EditFaultReportActivity.class);
            intent.putExtra("equipcode", equipCode);
            intent.putExtra("frId", id);
            intent.putExtra("workspaceId", workspace);
        } else if (click_action.equals(Constants.PMTASK_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(this, PmTaskActivity.class);
            intent.putExtra("taskId", Integer.parseInt(id));
            intent.putExtra("taskNumber", taskNumber);
            intent.putExtra("afterImage", afterImage);
            intent.putExtra("beforeImage", beforeImage);
            intent.putExtra("source", source);
            intent.putExtra("workspace", workspace);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String channelId = "my_channel_id";
        CharSequence channelName = "My Channel";
        int importance = 0;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_MAX;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.circularcmmslogo)
                .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, notification);
    }
}*/
