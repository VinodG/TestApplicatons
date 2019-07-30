package com.taksycraft.testapplicatons.backgroundtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.activities.FirstActivity;

//    https://stackoverflow.com/questions/14885368/update-text-of-notification-not-entire-notification
public class NotificationChrono {
    public static Notification.Builder builder;

    public static  void updateNotification(Context context, boolean running,
                                           int id, String title, String text,
                                           NotificationManager notificationManager) {
        Log.e("NotificationChrono","updateNotification");
        Intent stopIntent =  new Intent(
                context, FirstActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context,
                0, stopIntent, 0);
// Create a notification and set the notification channel.

        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        Notification notification=null;
        int notifyID = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sets an ID for the notification, so it can be updated.

            CharSequence name = "ChannelName" ;// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder = new Notification.Builder( context )
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.icn_popup_close)
                    .setChannelId(CHANNEL_ID)
                     ;
        }
        else
        {
            builder = new Notification.Builder(
                    context)

                    .setContentText("context text")

                    .setContentTitle(title)

                    .setSmallIcon(R.drawable.apple)

                    .setAutoCancel(false)

                    .setOngoing(running)

                    .setOnlyAlertOnce(true)

                    .setContentIntent(
                            PendingIntent.getActivity(context, 10, new Intent(
                                    context, BackgroundtakTestingActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP), 0))
//                    .addAction(
//                            running ? android.R.drawable.ic_media_pause
//                                    : android.R.drawable.ic_media_play,
//                            running ? "pause" : "start", startPendingIntent)
                    .addAction(android.R.drawable.ic_media_ff,
                            "stop", stopPendingIntent);

        }


// Issue the notification.
        notificationManager.notify(notifyID , builder.build());






    }
    public static void update(int NOTIFICATION_ID, NotificationManager notificationManager)
    {
        builder.setContentTitle("title ");
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
