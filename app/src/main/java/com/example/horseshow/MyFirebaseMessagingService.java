package com.example.horseshow;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import static android.support.constraint.Constraints.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingServic";
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            super.onMessageReceived(remoteMessage);

            if (remoteMessage.getData().size() > 0){ //check if message contains a data protocal
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                try{
                    JSONObject data = new JSONObject(remoteMessage.getData());
                    String jsonMessage = data.getString("extra_information");
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            if (remoteMessage.getNotification() !=  null){
                    String title = remoteMessage.getNotification().getTitle(); //get title
                    String message = remoteMessage.getNotification().getBody(); //get body
                    String chlick_action = remoteMessage.getNotification().getClickAction(); // get click action

                    Log.d(TAG, "Message Notification Title: " + title);
                    Log.d(TAG, "Message Notification Body: " + message);
                    Log.d(TAG, "Message Notification click_action: " + chlick_action);

                    sendNotification(title, message, chlick_action);
            }

          /*  if (remoteMessage.getNotification() != null) {
               String title = remoteMessage.getNotification().getTitle();  //this is to send notifications via the firebase console
                String message = remoteMessage.getNotification().getBody();
                Log.d(TAG, "onMessageReceived: Message Received: \n "+ "Title: " + title +"\n"
                        + "Message: " + message );

                sendNotification(title,message);
               // NotificationHelper.displayNotification(getApplicationContext(), title, message);
            } */
        }

        @Override
        public void onDeletedMessages() {

        }

    private void sendNotification(String title, String messageBody, String click_action) { // added String click_action
        Intent intent;
        if(click_action.equals("COMPETITOR")){
            intent = new Intent(this, competitor_activity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else if (click_action.equals("MAINACTIVITY")) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */,
                intent, PendingIntent.FLAG_ONE_SHOT);

            /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code * /, intent,
                PendingIntent.FLAG_ONE_SHOT); */

        String channelId  = "Horse_Show";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.horseicon)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

