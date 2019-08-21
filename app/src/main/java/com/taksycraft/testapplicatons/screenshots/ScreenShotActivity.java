package com.taksycraft.testapplicatons.screenshots;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

import java.io.IOException;

public class ScreenShotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);

    }

    public void takeScreenShot(View view) {
        Process process;
        try {
            process = Runtime.getRuntime().exec(
                    "su   input keyevent 120");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
