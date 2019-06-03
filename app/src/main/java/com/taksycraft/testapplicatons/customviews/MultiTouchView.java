package com.taksycraft.testapplicatons.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.taksycraft.testapplicatons.R;

//public class ImageViewWithZoom  extends View implements View.OnTouchListener {
public class MultiTouchView extends View/* implements View.OnTouchListener*/ {
    private static final String TAG = MultiTouchView.class.getSimpleName() ;
    private Drawable image;
    private float scaleFactor = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;
    private ImageView iv;

    public MultiTouchView(Context context) {
        super(context);
//        iv= new ImageView(context);
//        iv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT ) );
//        addView(iv);
        image = context.getResources().getDrawable(R.drawable.apple);
        setFocusable(true);
        image.setBounds(0, 0, image.getIntrinsicWidth(),
                image.getIntrinsicHeight());
//        iv.setImageDrawable(image);
        scaleGestureDetector = new ScaleGestureDetector(context,
                new ScaleListener());
//        setOnTouchListener(this);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        Log.e(TAG,"onLayout "+scaleFactor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Set the image bounderies
        canvas.save();
        canvas.scale(scaleFactor, scaleFactor);
//        Log.e(TAG,"onDraw "+scaleFactor);
//        iv.setLayoutParams(new FrameLayout.LayoutParams((int)(iv.getLayoutParams().width*scaleFactor),(int)(iv.getLayoutParams().height*scaleFactor)));
        image.draw(canvas);
//        iv.setImageDrawable(image);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        passEvent(event);
        invalidate();
        return true;
    }
    private void passEvent(MotionEvent motionEvent)
    {
        // get masked (not specific to a pointer) action
        int maskedAction = motionEvent.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "ACTION_DOWN");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.e(TAG, "ACTION_POINTER_DOWN");
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
//                Log.e(TAG, "ACTION_MOVE");
                break;
            }
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "ACTION_UP");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.e(TAG, "ACTION_POINTER_UP");
                break;
            case MotionEvent.ACTION_CANCEL: {
                Log.e(TAG, "ACTION_CANCEL");
                break;
            }
        }
    }
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//
////        invalidate();
//        return false;
//    }

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
