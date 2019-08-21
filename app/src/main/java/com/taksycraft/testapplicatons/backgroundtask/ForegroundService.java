package com.taksycraft.testapplicatons.backgroundtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.activities.FirstActivity;

//https://androidwave.com/foreground-service-android-example/
public class ForegroundService extends Service {
    private static final String TAG = ForegroundService.class.getSimpleName() ;
    private static final int NOTIFICATION_ID =  1 ;
    private static final String CHANNEL_ID ="ForegroundServiceChannel" ;
    private NotificationManager notificationManager;

    public ForegroundService() {


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
        notificationManager  =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChrono.updateNotification(getApplicationContext(),
                false,
                NOTIFICATION_ID,
                "title",
                "text",
                notificationManager);

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        //start background task
        startCounting();
        //
        return START_STICKY ;
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

    private void startCounting(  ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<600;i++)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG,Thread.currentThread().getId()+" -- "+i);
                    NotificationChrono.update(NOTIFICATION_ID,notificationManager);
                }
                stopSelf();
            }
        }).start();
    }


}
