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
public class ImageViewWithZoom  extends View /*implements View.OnTouchListener*/ {
    private static final String TAG =ImageViewWithZoom.class.getSimpleName() ;
    private Drawable image;
    private float scaleFactor = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;

    public ImageViewWithZoom(Context context) {
        super(context);
        image = context.getResources().getDrawable(R.drawable.apple);
        setFocusable(true);
        image.setBounds(0, 0, image.getIntrinsicWidth(),
                image.getIntrinsicHeight());
        scaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the image bounderies
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
        image.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
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
