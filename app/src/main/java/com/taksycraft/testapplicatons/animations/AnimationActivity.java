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
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taksycraft.testapplicatons.R;

import static androidx.dynamicanimation.animation.SpringAnimation.*;

public class AnimationActivity extends AppCompatActivity {

    private static final String TAG = AnimationActivity.class.getSimpleName();
    private ImageView iv;
    private LinearLayout screen;
    private  Handler handler = new Handler();
    private float dY;
    private float dX;
    private SpringAnimation xxAnimation;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        iv=(ImageView)findViewById(R.id.iv);
        iv2=(ImageView)findViewById(R.id.iv2);
//        fadingAnimation(iv);
//        transitionAnimation(iv);
//        springAnimation(iv);
        moveDownAnimation(iv);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AnimationActivity.this, "clicked on apple",Toast.LENGTH_SHORT).show();
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AnimationActivity.this, "clicked on background",Toast.LENGTH_SHORT).show();
            }
        });
//        imageViewDragSpringAnimation();
    }

    private void moveDownAnimation(final ImageView iv) {
        iv2.startAnimation(new ResizeAnimation( iv2,500,500,500 ,1000 ));
//        LinearInterpolator linearInterpolator = new LinearInterpolator();
//        ObjectAnimator anim = ObjectAnimator.ofFloat(iv, "translationY", 0f, 200 );
//        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv2, "translationY", 0f, 200 );
//        anim.setInterpolator(linearInterpolator);
//        anim2.setInterpolator(linearInterpolator);
//        anim.setDuration(500).start();
//        anim2.setDuration(500).start();
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
            xAnimation = (SpringAnimation) createSpringAnimation(iv, X, iv.getX(),
                    SpringForce.STIFFNESS_HIGH, SpringForce.DAMPING_RATIO_NO_BOUNCY);
            xxAnimation = createSpringAnimation(iv, X, 1500,
                    SpringForce.STIFFNESS_HIGH, SpringForce.DAMPING_RATIO_NO_BOUNCY);
            yAnimation = createSpringAnimation(iv, Y, iv.getY(),
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
        SpringAnimation springAnim = new SpringAnimation(iv, TRANSLATION_Y);
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
