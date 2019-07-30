package com.taksycraft.testapplicatons.backgroundtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.activities.FirstActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundtakTestingActivity extends AppCompatActivity {

    private TextView tv;
    private ExecutorService tpe;
    private String TAG= BackgroundtakTestingActivity.class.getSimpleName();
    private int count=1;
    private Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);
        tv= (TextView)findViewById(R.id.tv);
        Log.e( TAG, "Availble processors " + Runtime.getRuntime().availableProcessors()+"  " );
        tpe = Executors.newFixedThreadPool(2);
//        startAsyncTask();


    }



    private void startAsyncTask() {
        for (int i = 0 ;i<4;i++)
            new  MyAsyncTask().execute(null,null);
    }

    public void onClkStart(View view) {
//        startNewThread();
        startForeGroundServiceForOnlyOREOorAboveDevices();


    }

    private void startForeGroundServiceForOnlyOREOorAboveDevices() {
        intentService = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this,intentService);
    }

    private void startNewThread() {
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0 ;i<100;i++)
                {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e( TAG, Thread.currentThread().getId()+"  "+i);

                }
            }
        });
    }

    public void onClkStop(View view) {
        stopService(intentService);
    }

    public class MyRunnable implements Runnable  {
        private   String str="MyRunnable";
        public MyRunnable(String str) {
            this.str =str;
        }

        @Override
        public void run() {
            for (int i = 0 ;i<100;i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e( TAG, Thread.currentThread().getName()+str );
            }
        }
    }
    class MyAsyncTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects) {
            for (int i = 0;i<100;i++)
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("AsyncTask",Thread.currentThread().getId()+"");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.e("AsyncTask", "onPostExecute");

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }
    @Override
    public void onSaveInstanceState(Bundle outState ) {
        super.onSaveInstanceState(outState );
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }
}
