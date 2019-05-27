package com.taksycraft.testapplicatons.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class ImageSwappingView extends View {

    private boolean isInit;  // it will be true once the clock will be initialized.
    private int minimumIncrement=5;
    private int alpha=minimumIncrement;
    private boolean isIncrement;
    private long previousTimeSec;
    private int mMinAlpha=0; //0-tranparent [0-255], 255 -black screen
    private int mMaxAlpha = 220; //[0-255]
    private long mIdleTimeSec = 5;
    private OnChangeImageListener listener;

    public ImageSwappingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageSwappingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setIdleTimeSec(long mIdleTimeSec) {
        this.mIdleTimeSec = mIdleTimeSec;
    }

    /**
    it is being triggered, when it fades to black color
     */
    public void setOnChangeImage(OnChangeImageListener listener)
    {
        this.listener = listener;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            isInit = true;  // set true once initialized
        }
        if( shouldBeIdle() )
        {
            alpha = mMinAlpha;
            setBackgroundColor(Color.argb( alpha , 0,0,0));
        }
        else
        {
            if(isIncrement)
                alpha = alpha+minimumIncrement;
            else
                alpha=alpha-minimumIncrement;
            if(alpha>mMaxAlpha)
            {
                alpha=mMaxAlpha;
                isIncrement=false;
                if(listener!=null)
                {
                    listener.changeImage();
                }
            }
            if(alpha<mMinAlpha) {
                previousTimeSec = Calendar.getInstance().getTimeInMillis() / 1000;
                isIncrement = true;
                alpha =mMinAlpha;
            }
            setBackgroundColor(Color.argb( alpha , 0,0,0));
        }
        postInvalidateDelayed(100);
    }
    private boolean shouldBeIdle() {
        long currentSeconds = Calendar.getInstance().getTimeInMillis() / 1000;
        if(currentSeconds-previousTimeSec<mIdleTimeSec)
            return true;
        else {
            return false;
        }
    }
    public   interface OnChangeImageListener
    {
        public void changeImage();
    }

}
