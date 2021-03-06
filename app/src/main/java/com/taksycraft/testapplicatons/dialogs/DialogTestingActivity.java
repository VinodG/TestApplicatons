package com.taksycraft.testapplicatons.dialogs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;

import com.taksycraft.testapplicatons.R;

import java.util.Locale;

public class DialogTestingActivity extends AppCompatActivity {
    private static final String TAG = "DialogTestingActivity";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setArabicLocale();
        setContentView(R.layout.activity_dialog_testing);
//        TopUpDialog di`alog = new TopUpDialog(this,R.style.DialogMenu);
//        dialog.show();

        dialog = new ProgressDialog(this ,R.style.progressbarStyle2);
        String message = "جار التحميل";
//        SpannableString spannableString =  new SpannableString(message);
//        Typeface typefaceSpan = Typeface.createFromAsset( getAssets( ),"fonts/droidkufi_regular.ttf"  );
//        spannableString.setSpan(typefaceSpan, 0, message.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onResume: "  );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: "  );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: "  );
        dialog.show();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 4000);
    }

    private void setArabicLocale() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Locale locale = new Locale("ar");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N){
            getApplicationContext().createConfigurationContext(configuration);
        } else {
            resources.updateConfiguration(configuration,displayMetrics);
        }
    }
}