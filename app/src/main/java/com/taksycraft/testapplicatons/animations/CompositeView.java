package com.taksycraft.testapplicatons.animations;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class CompositeView {
    private static   String TAG = CompositeView.class.getSimpleName();
    private final Context context;
    private   int llMainWidth= 0 ,llMainHeight = 0 ,childWidth= 0 , childHeight= 0 ;
    private   LinearLayout llMain;
    private ViewChange mainLayoutChangeListener;
    private View viewChild;
    private View mainChild;
    private ResizeAnimation anim;
    private boolean isAnimationStarted  = false;
    private boolean isExpanded  = true;

    public CompositeView(Context context) {
        this.context = context;
        this.llMain = new LinearLayout(context);
        llMain.setOrientation(LinearLayout.VERTICAL);
        llMain.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    public CompositeView(Context context,int width, int height) {
        this.context = context;
        this.llMain = new LinearLayout(context);
        llMain.setOrientation(LinearLayout.VERTICAL);
        this.llMainWidth = width;
        this.llMainHeight = height;
        llMain.setLayoutParams(new ViewGroup.LayoutParams(llMainWidth, llMainHeight));
    }
    public void setMainLayout( int LayoutId ,   int modifiedWidth , int modifiedHeight) {
        View view = LayoutInflater.from(context).inflate(LayoutId,null);
        this.mainChild = view;
        mainChild.setLayoutParams(new LinearLayout.LayoutParams(llMainWidth,llMainHeight));
        this.childWidth = modifiedWidth;
        this.childHeight  = modifiedHeight;
        if(llMain!=null && llMain.getChildCount()>0)
            llMain.removeViewAt(0);
        llMain.addView(mainChild, 0 );
    }
    public void setChildLayout( int LayoutId  ) {
        final View view = LayoutInflater.from(context).inflate(LayoutId,null);
        this.viewChild = view;
        this.viewChild.setLayoutParams(new LinearLayout.LayoutParams( childWidth,  childHeight));
        if(llMain!=null && llMain.getChildCount()>1) {
            llMain.removeViewAt(1);
            llMain.addView(viewChild, 1);
        }
        else
        {
            llMain.addView(viewChild);
        }
    }
    public void setMainLayoutChangeListener(ViewChange layoutChangeListener)
    {
        this.mainLayoutChangeListener = layoutChangeListener;
    }
    public void collapse()
    {
        if(anim!=null)
            anim .cancel();
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llMainWidth+", "+llMainHeight+", "+childWidth+", "+childHeight  );
        anim = new ResizeAnimation( mainChild,llMainWidth,llMainHeight,childWidth,childHeight);
        mainChild.setAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationStarted = true;
                if(viewChild!=null)
                    viewChild.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                if(mainLayoutChangeListener!=null)
                {
                    mainLayoutChangeListener.onCollapse();
                }
                isAnimationStarted = false;
                isExpanded =false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llMainWidth+", "+llMainHeight+", "+childWidth+", "+childHeight  );

            }
        });
        if(isExpanded)
            mainChild.startAnimation(anim);
    }
    public void expand()
    {
        if(anim!=null)
            anim .cancel();
        anim = new ResizeAnimation( mainChild, childWidth,childHeight,llMainWidth,llMainHeight);
        mainChild.setAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimationStarted = true;
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                if(viewChild!=null)
                    viewChild.setVisibility(View.GONE);
                if(mainLayoutChangeListener!=null)
                {
                    mainLayoutChangeListener.onExpand();
                }
                isAnimationStarted = false;
                isExpanded =true;
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+llMainWidth+", "+llMainHeight+", "+childWidth+", "+childHeight  );
            }
        });
        if(!isExpanded)
            mainChild.startAnimation(anim);
    }
    public LinearLayout getView()
    {
        return llMain;
    }

    interface ViewChange
    {
        public void onCollapse();
        public void onExpand();
    }

    public View getViewChild() {
        return viewChild;
    }
    public View getMainChild() {
        return mainChild;
    }
}
