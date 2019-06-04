package com.taksycraft.testapplicatons.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.taksycraft.testapplicatons.R;

public class FirstActivity extends AppCompatActivity {

    private   String TAG = FirstActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName());
        setContentView(R.layout.activity_first);
        setTitle("FirstActivity");
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

    public void onNext(View view) {
        startActivity(new Intent(FirstActivity.this,SecondActivity.class));
    }
}