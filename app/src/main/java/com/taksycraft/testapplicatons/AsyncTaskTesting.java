package com.taksycraft.testapplicatons;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class AsyncTaskTesting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0 ;i<4;i++)
            new MyAsyncTask().execute();
    }
    class MyAsyncTask extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] objects) {
            for (int i = 0;i<10;i++)
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
}
