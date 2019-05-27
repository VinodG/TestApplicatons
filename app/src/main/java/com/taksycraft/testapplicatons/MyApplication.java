package com.taksycraft.testapplicatons;

import android.app.Application;
import android.content.Context;

import com.taksycraft.testapplicatons.retrofit.DBClient;

public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DBClient client = new DBClient(getApplicationContext());
        new DBClient();

    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
