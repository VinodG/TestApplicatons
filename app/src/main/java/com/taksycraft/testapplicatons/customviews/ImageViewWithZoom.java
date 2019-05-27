package com.taksycraft.testapplicatons.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.taksycraft.testapplicatons.R;
//for image zooming - https://www.vogella.com/tutorials/AndroidTouch/article.html#singletouch
//for moving -- https://stackoverflow.com/questions/9398057/android-move-a-view-on-touch-move-action-move
//public class ImageViewWithZoom  extends View implements View.OnTouchListener {
public class ImageViewWithZoom  extends FrameLayout implements View.OnTouchListener {
    private static final String TAG =ImageViewWithZoom.class.getSimpleName() ;
    private final ImageView iv;
    private Drawable image;
    private float scaleFactor = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;
    int X=0,Y=0;
    private boolean isValid;
    private int _xDelta;
    private int _yDelta;

    public ImageViewWithZoom(Context context) {
        super(context);
        image = context.getResources().getDrawable(R.drawable.apple);
        setFocusable(true);
        image.setBounds(0, 0, image.getIntrinsicWidth(),
                image.getIntrinsicHeight());
        scaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleListener());
        setOnTouchListener(this);
        iv = new ImageView(context );
        iv.setBackground(image);
        addView(iv);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the image bounderies
//        if(isValid)

        canvas.save();
        iv.setScaleX(scaleFactor);
        iv.setScaleY(scaleFactor);
//        canvas.scale(scaleFactor, scaleFactor);
//        Log.e(TAG,"onDraw "+scaleFactor);
//        image.draw(canvas);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        invalidate();
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
         invalidate();
        return true;
    }

    private class ScaleListener extends
            ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();

            // don't let the object get too small or too large.
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            invalidate();
            return true;
        }
    }
}
