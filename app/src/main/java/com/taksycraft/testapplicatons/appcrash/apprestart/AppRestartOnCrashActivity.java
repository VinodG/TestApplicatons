package com.taksycraft.testapplicatons.appcrash.apprestart;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.taksycraft.testapplicatons.R;

import androidx.appcompat.app.AppCompatActivity;

public class AppRestartOnCrashActivity extends AppCompatActivity {
//    https://medium.com/@ssaurel/how-to-auto-restart-an-android-application-after-a-crash-or-a-force-close-error-1a361677c0ce
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_restart_on_crash);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        if (getIntent().getBooleanExtra("crash", false)) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show();
        }
    }

    public void crashMe(View v) {
        throw new NullPointerException();
    }
}
