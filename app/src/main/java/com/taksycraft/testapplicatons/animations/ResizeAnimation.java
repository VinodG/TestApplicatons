package com.taksycraft.testapplicatons.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

//https://stackoverflow.com/questions/8140571/resizing-layouts-programmatically-as-animation
/**
 * an animation for resizing the view.
 */
public class ResizeAnimation extends Animation {
    private View mView;
    private float mToHeight;
    private float mFromHeight;

    private float mToWidth;
    private float mFromWidth;

    public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
        mToHeight = toHeight;
        mToWidth = toWidth;
        mFromHeight = fromHeight;
        mFromWidth = fromWidth;
        mView = v;
        setDuration(1000);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float height =
                (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
        float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
        ViewGroup.LayoutParams p = mView.getLayoutParams();
        p.height = (int) height;
        p.width = (int) width;
        mView.requestLayout();
    }
}