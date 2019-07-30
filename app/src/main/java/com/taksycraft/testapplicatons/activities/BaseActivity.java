package com.taksycraft.testapplicatons.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

public class BaseActivity extends AppCompatActivity {

    public static String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
        setContentView(R.layout.activity_first);

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
