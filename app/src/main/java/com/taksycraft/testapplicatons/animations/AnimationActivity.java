package com.taksycraft.testapplicatons.animations;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.taksycraft.testapplicatons.R;

public class AnimationActivity extends AppCompatActivity {

    private static final String TAG = AnimationActivity.class.getSimpleName();
    private ImageView iv;
    private LinearLayout screen;
    private  Handler handler = new Handler();
    private float dY;
    private float dX;
    private SpringAnimation xxAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        iv=(ImageView)findViewById(R.id.iv);
//        fadingAnimation(iv);
//        transitionAnimation(iv);
        springAnimation(iv);
//        imageViewDragSpringAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        springAnimation(iv);
    }

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private void imageViewDragSpringAnimation() {
//        https://www.journaldev.com/21896/android-spring-animation-physics-animation
        iv.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        iv.setOnTouchListener(touchListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.e(TAG,iv.getX()+"");
            xAnimation = createSpringAnimation(iv, SpringAnimation.X, iv.getX(),
                    SpringForce.STIFFNESS_HIGH, SpringForce.DAMPING_RATIO_NO_BOUNCY);
            xxAnimation = createSpringAnimation(iv, SpringAnimation.X, 1500,
                    SpringForce.STIFFNESS_HIGH, SpringForce.DAMPING_RATIO_NO_BOUNCY);
            yAnimation = createSpringAnimation(iv, SpringAnimation.Y, iv.getY(),
                    SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        }
    };

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    dX = v.getX() - event.getRawX();
                    dY = v.getY() - event.getRawY();
                    //  cancel animations
                    xAnimation.cancel();
                    yAnimation.cancel();
                    break;
                case MotionEvent.ACTION_MOVE:
//                    if(v.getX()<1000)
                        iv.animate()
                                .x(event.getRawX() + dX)
//                            .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                    break;
                case MotionEvent.ACTION_UP:
                    if(v.getX()<750)
                        xAnimation.start();
                    else
                        xxAnimation.start();
//                    yAnimation.start();
                    break;
            }
            return true;
        }
    };

    public SpringAnimation createSpringAnimation(View view,
                                                 DynamicAnimation.ViewProperty property,
                                                 float finalPosition,
                                                 float stiffness,
                                                 float dampingRatio) {
        SpringAnimation animation = new SpringAnimation(view, property);
        SpringForce springForce = new SpringForce(finalPosition);
        springForce.setStiffness(stiffness);
        springForce.setDampingRatio(dampingRatio);
        animation.setSpring(springForce);
        return animation;
    }

    private void springAnimation(ImageView iv) {
        SpringAnimation springAnim = new SpringAnimation(iv, SpringAnimation.TRANSLATION_Y);
        SpringForce springForce = new SpringForce();
        springForce.setFinalPosition(200f);
        springForce.setStiffness(SpringForce.STIFFNESS_LOW);
        springForce.setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim.setSpring(springForce);
        springAnim.start();
    }

    private void transitionAnimation(ImageView iv) {
        ObjectAnimator animation = ObjectAnimator.ofFloat(iv, "translationY", 200f);
        animation.setDuration(2000);
        animation.start();
    }

    private void fadingAnimation(ImageView iv) {
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
