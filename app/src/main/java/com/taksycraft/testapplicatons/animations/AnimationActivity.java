package com.taksycraft.testapplicatons.animations;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.CommonAdapter;

import java.util.Vector;

import static androidx.dynamicanimation.animation.SpringAnimation.TRANSLATION_Y;
import static androidx.dynamicanimation.animation.SpringAnimation.X;
import static androidx.dynamicanimation.animation.SpringAnimation.Y;

public class AnimationActivity extends AppCompatActivity {

    private static final String TAG = AnimationActivity.class.getSimpleName();
    private ImageView iv;
    private LinearLayout screen;
    private  Handler handler = new Handler();
    private float dY;
    private float dX;
    private SpringAnimation xxAnimation;
    private ImageView iv2;
    private LinearLayout llKeypad;
    private RecyclerView rv;
    private CommonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        animationInitControls();
//        animationForKeypad();
//        compositeViewWithAnimation();
        nestedCompositeView();
    }

    private void nestedCompositeView() {
        final ResizableCompositeView compositeView = new ResizableCompositeView(this,500,1000);
        compositeView.setMainLayout(R.layout.activity_registation,500,500);
        compositeView.setChildLayout(R.layout.fragment_fragment1);
        ViewGroup childCompositieView = (ViewGroup)compositeView.getViewChild();
        childCompositieView.setBackgroundColor(Color.GREEN);
        compositeView.getMainChild().setBackgroundColor(Color.RED);
        final ResizableCompositeView compositeView2 = new ResizableCompositeView(this,500,500);
        compositeView2.setMainLayout(R.layout.activity_registation,250,500);
        compositeView2.setChildLayout(R.layout.activity_registation);
        View childCompositieView2 = compositeView2.getMainChild();
        childCompositieView.addView(compositeView2.getView());
        View expand1 = compositeView.getMainChild().findViewById(R.id.etInput);
        View collapse1 = compositeView.getMainChild().findViewById(R.id.btnRegistration);
        View expand2 = childCompositieView2.findViewById(R.id.etInput);
        View collapse2 = childCompositieView2.findViewById(R.id.btnRegistration);
        expand1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.expand();
            }
        });
        collapse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.collapse();
            }
        });
        expand2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView2.expand();
            }
        });
        collapse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView2.collapse();
            }
        });
        setContentView(compositeView.getView());

    }

    private void compositeViewWithAnimation() {
        final ResizableCompositeView compositeView = new ResizableCompositeView(this,1000,1500);
        compositeView.setMainLayout(R.layout.activity_composite_one,1000,1000);
        compositeView.setChildLayout(R.layout.activity_composite_two);
        compositeView.getViewChild().setBackgroundColor(Color.GREEN);
        compositeView.getMainChild().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.collapse();
            }
        });
        compositeView.getViewChild().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.expand();
            }
        });
        View view = compositeView.getView();
        setContentView(view);
        llKeypad = (LinearLayout)view.findViewById(R.id.llKeypad);
        final LinearLayout llOuter =  (LinearLayout)view. findViewById(R.id.llOuter);
        Button btnOpenKeypad = (Button) view.findViewById(R.id.btnOpenKeypad);
        rv= (RecyclerView)view. findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        final Vector<String>vec = new Vector<String>();
        for (int i= 0 ;i<100;i++)
            vec.add("Item "+(i+1));
        adapter = new CommonAdapter(vec, new CommonAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {
                holder.getTextView().setText(vec.get(position));
            }

            @Override
            public void onItemClick(Object object, int position) {

            }
        });
        compositeView.setMainLayoutChangeListener(new ResizableCompositeView.ViewChange() {
            @Override
            public void onCollapse() {
                rv.smoothScrollToPosition(vec.size()-1);
            }

            @Override
            public void onExpand() {

            }
        });
        rv.setAdapter(adapter);
        btnOpenKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.collapse();
            }
        });
        llKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compositeView.expand();
            }
        });


    }

    private void animationForKeypad() {
        setContentView(R.layout.activity_customkeypad_animation);
        llKeypad = (LinearLayout)findViewById(R.id.llKeypad);
        final LinearLayout llOuter = (LinearLayout) findViewById(R.id.llOuter);
        Button btnOpenKeypad = (Button) findViewById(R.id.btnOpenKeypad);
        rv= (RecyclerView)findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        final Vector<String>vec = new Vector<String>();
        for (int i= 0 ;i<100;i++)
            vec.add("Item "+(i+1));
        adapter = new CommonAdapter(vec, new CommonAdapter.ListListener() {
            @Override
            public void onBindViewHolder(CommonAdapter.CommonHolder holder, int position) {
                holder.getTextView().setText(vec.get(position));
            }

            @Override
            public void onItemClick(Object object, int position) {

            }
        });
        rv.setAdapter(adapter);
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llOuter.getHeight());
        btnOpenKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                rv.startAnimation(new ResizeAnimation( rv,rv.getWidth(),-1,rv.getWidth() ,-500 ));
//                llOuter.startAnimation(new ResizeAnimation( llOuter,llOuter.getWidth(),1500,llOuter.getWidth() ,1000 ));
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llOuter.getWidth()+", "+1500+", "+llOuter.getWidth()+", "+1000);
                ResizeAnimation anim = new ResizeAnimation( llOuter,llOuter.getWidth(),1500,llOuter.getWidth() ,1000 );
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                        llKeypad.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                        rv.smoothScrollToPosition(vec.size()-1);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                    }
                });
                llOuter.startAnimation(anim );
            }
        });
        llKeypad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llOuter.getWidth()+", "+1000+", "+llOuter.getWidth()+", "+1500);

                ResizeAnimation anim = new ResizeAnimation( llOuter,llOuter.getWidth(),1000,llOuter.getWidth() ,1500 );
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                        rv.smoothScrollToPosition(vec.size()-1);
                        llKeypad.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                    }
                });
                llOuter.startAnimation(anim );

            }
        });

    }

    private void animationInitControls() {
        setContentView(R.layout.activity_animation);
        iv=(ImageView)findViewById(R.id.iv);
        iv2=(ImageView)findViewById(R.id.iv2);
//        fadingAnimation(iv);
//        transitionAnimation(iv);
//        springAnimation(iv);
        resizeViewAnimation(iv);
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

    private void resizeViewAnimation(final ImageView iv) {
        iv2.startAnimation(new ResizeAnimation( iv2,500,500,500 ,1000 ));
    }
    private void moveViewsDownAnimation() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        ObjectAnimator anim = ObjectAnimator.ofFloat(iv, "translationY", 0f, 200 );
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(iv2, "translationY", 0f, 200 );
        anim.setInterpolator(linearInterpolator);
        anim2.setInterpolator(linearInterpolator);
        anim.setDuration(500).start();
        anim2.setDuration(500).start();
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
