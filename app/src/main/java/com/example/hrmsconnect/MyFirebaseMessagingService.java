package com.example.hrmsconnect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        showNotification(remoteMessage.getNotification().getBody());
        Log.e("Data>>>>>",remoteMessage.getNotification().getBody()+"");

    }

    private void showNotification(String message) {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("com.example.hrmsconnect",
                    "HRMS Connect",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "com.example.hrmsconnect")
                .setSmallIcon(R.mipmap.logo2) // notification icon
                .setContentTitle("HRMSConnect") // title for notification
                .setContentText(message)// message for notification // clear notification after click
                .setAutoCancel(true);
        Intent intent = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());

//        Intent i=new Intent(this, MainActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
//                .setAutoCancel(true)
//                .setSmallIcon(R.mipmap.logo2)
//                .setContentTitle("HRMSConnect")
//                .setContentText(message)
//                .setContentIntent(pendingIntent);
//
//
//
//        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        manager.notify(0,builder.build());
    }


}
