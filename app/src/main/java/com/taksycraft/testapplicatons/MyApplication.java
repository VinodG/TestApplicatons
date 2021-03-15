package com.taksycraft.testapplicatons;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.taksycraft.testapplicatons.common.PreferenceUtils;
import com.taksycraft.testapplicatons.retrofit.DBClient;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    public static MyApplication instance;
    private PreferenceUtils pref;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        pref = new PreferenceUtils(this);
        String fcm = pref.getStringFromPreference("FCM", "");
        if(TextUtils.isEmpty(fcm))
            getFirebaseToken();
        else
            Log.e(TAG, "onCreate: " +fcm );

        new DBClient();

    }

    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        pref.saveString("FCM" , token);
                        // Log and toast
                        Log.d(TAG, token);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
