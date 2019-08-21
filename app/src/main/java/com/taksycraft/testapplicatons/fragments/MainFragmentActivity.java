package com.taksycraft.testapplicatons.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.taksycraft.testapplicatons.R;

public class MainFragmentActivity extends AppCompatActivity {

    private LinearLayout llBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        llBody = (LinearLayout)findViewById(R.id.llBody);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setWindowParams(getWindow());
    }

    public void on1Click(View view) {
        Fragment fragment = new Fragment1();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.llBody,fragment);
        transaction.commit();
    }

    public void on2Click(View view) {
        Fragment fragment = new Fragment2();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.llBody,fragment);
        transaction.commit();
    }

    public void on3Click(View view) {
        MyBottomSheetFragment frag = new MyBottomSheetFragment();
        frag .setCancelable(false);
        frag.show(getSupportFragmentManager(),"vinod");
    }
    public static void setWindowParams(final Window window) {
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideNavBar(window);
            }

        });

    }
    public static  void hideNavBar(Window window) {

        if (Build.VERSION.SDK_INT >= 19) {
            View v = window.getDecorView();
            v.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
