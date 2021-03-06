package com.taksycraft.testapplicatons.backgroundtask;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadsActivity extends AppCompatActivity {

    private TextView tv;
    private ExecutorService tpe;
    private String TAG=ThreadsActivity.class.getSimpleName();
    private int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threads);
        tv= (TextView)findViewById(R.id.tv);
        Log.e( TAG, "Availble processors " + Runtime.getRuntime().availableProcessors()+"  " );
        tpe = Executors.newFixedThreadPool(2);
    }

    public void onClkStart(View view) {
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
    public class MyRunnable implements Runnable  {
        private   String str="MyRunnable";
        public MyRunnable(String str) {
            this.str =str;
        }

        @Override
        public void run() {
            for (int i = 0 ;i<10;i++)
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
