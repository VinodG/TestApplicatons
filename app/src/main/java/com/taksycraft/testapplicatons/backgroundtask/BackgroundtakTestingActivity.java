package com.taksycraft.testapplicatons.backgroundtask;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Messenger serviceMessanger = null;
    private Messenger activityMessagener = new Messenger(new ActivityHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);
        tv= (TextView)findViewById(R.id.tv);
        Log.e( TAG, "Availble processors " + Runtime.getRuntime().availableProcessors()+"  " );
        tpe = Executors.newFixedThreadPool(2);

    }

    public void onClkStart(View view) {
//        startForeGroundServiceForOnlyOREOorAboveDevices();
//        startInteractiveService();
        startIntentService();
    }

    private void startInteractiveService() {
        intentService = new Intent(this, InteractiveService.class);
        bindService(intentService, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                log(" onServiceConnected");
                serviceMessanger  = new Messenger(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                log(" onServiceDisconnected");
                serviceMessanger = null;

            }
        },BIND_AUTO_CREATE);
    }
    private void startIntentService() {
        intentService = new Intent(this, MyIntentService.class);
        startService( intentService);
    }

    private void startForeGroundServiceForOnlyOREOorAboveDevices() {
        intentService = new Intent(this, ForegroundService.class);
        ContextCompat.startForegroundService(this,intentService);
    }



    public void onClkStop(View view) {
        stopService(intentService);
    }

    public void onClkSend(View view) {
        EditText etInput = (EditText) findViewById(R.id.etInput);
        Message msg = Message.obtain();
        msg.replyTo = activityMessagener;
        Bundle bundle = new Bundle();
        bundle.putString("MyString", etInput.getText().toString());
        msg.setData(bundle);
        try {
            serviceMessanger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        etInput.setText("");

    }

    class ActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            log();
            super.handleMessage(msg);
            log("handleMessage in Activity" );
            Bundle data = msg.getData();
            String dataString = data.getString("timestamp");
            tv.setText(dataString+"");
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
        startInteractiveService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
        stopService(intentService);
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
    private void log(String str)
    {
        Log.e(TAG,new Exception().getStackTrace()[1].getMethodName()+"->"+str );
    }
    private void log()
    {
        Log.e(TAG,new Exception().getStackTrace()[1].getMethodName());
    }


}
