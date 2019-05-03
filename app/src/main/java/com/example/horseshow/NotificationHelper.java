package com.example.horseshow;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationHelper { public static void displayNotification(Context context, String title, String body) {

    Intent intent = new Intent(context, competitor_activity.class);  //change this location

    PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            100,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT
    );

    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

    NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(context);
    mNotificationMgr.notify(1, mBuilder.build());

}

}