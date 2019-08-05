package com.taksycraft.testapplicatons.backgroundtask;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.activities.FirstActivity;

public class MyIntentService extends IntentService {

    private static final int NOTIFICATION_ID =  1 ;
    private static final String CHANNEL_ID ="ForegroundIntentServiceChannel" ;
    private     String TAG = "MyIntentService";
    private NotificationManager notificationManager;

    public MyIntentService() {
        super("MyIntentService");
//        makeNotificationChannel();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        makeForegroundService();
        for (int i = 0 ;i<5 ;i++)
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e(TAG, Thread.currentThread().getName()+"   "+i);
//            NotificationChrono.update(NOTIFICATION_ID,i+" - seconds",notificationManager);
        }

    }
    private void makeNotificationChannel() {
        notificationManager  =
                (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationChrono.updateNotification(getApplicationContext(),
                false,
                NOTIFICATION_ID,
                "title",
                "text",
                notificationManager);
    }
    private void makeForegroundService() {
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, FirstActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Title")
                .setContentText("text")
                .setSmallIcon(R.drawable.icn_popup_close)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "Destroy ");
    }
}
