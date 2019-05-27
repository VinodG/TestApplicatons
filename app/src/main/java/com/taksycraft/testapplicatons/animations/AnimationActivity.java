package com.taksycraft.testapplicatons.animations;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taksycraft.testapplicatons.R;

public class AnimationActivity extends AppCompatActivity {

    private ImageView iv;
    private LinearLayout screen;
    private  Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        iv=(ImageView)findViewById(R.id.iv);
        AlphaAnimation fadein = new AlphaAnimation(-1, 4);
        //fadein.setFillAfter(true);
        fadein.setDuration(3000);
        iv.startAnimation(fadein);
        screen = (LinearLayout) findViewById(R.id.screen);

        (new Thread(){
            int i;
            boolean isIncrement=true;
            @Override
            public void run(){
                for(  i=0; i<=255; ){
                    handler.post(new Runnable(){
                        public void run(){
                            if(i>254 || i<0)
                            {
                                isIncrement =!isIncrement;
                            }
                            screen.setBackgroundColor(Color.argb(255, i, i, i));
                            if(isIncrement) {
                                i++;
                            }
                            else {
                                i--;
                            }
                        }
                    });
                    // next will pause the thread for some time
                    try{
                        if(i==254)
                            sleep(3000);
                        else
                            sleep(10);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
