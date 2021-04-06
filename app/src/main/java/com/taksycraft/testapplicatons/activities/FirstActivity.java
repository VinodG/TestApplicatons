package com.taksycraft.testapplicatons.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.firebasedatabase.ChatDO;

public class FirstActivity extends AppCompatActivity {

//    private   String TAG = FirstActivity.class.getSimpleName();
    private   String TAG = "FirstActivity";
    private ChatDO object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
        setContentView(R.layout.activity_first);
        setTitle("FirstActivity");
        object = new ChatDO();
        object.msg= "FirstActivity";
        giveRing();

    }

    private void giveRing() {
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ringtone.stop();
            }
        },6000);
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
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+" intent obj -> "+object.msg);
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
    public void onNext(View view) {
        Intent intent  = new Intent(FirstActivity.this,SecondActivity.class);

        intent.putExtra("object",object);
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
    }
}
