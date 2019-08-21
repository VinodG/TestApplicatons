package com.taksycraft.testapplicatons.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class SignatureView extends View implements View.OnTouchListener{
    private  String TAG =SignatureView.class.getSimpleName() ;
    private boolean init =true;
    private Paint mPaint;
    private Path path;
    private float lastTouchX;
    private float lastTouchY;
    private int brushColor=Color.GREEN;
    private float brushWidth = 6f;
    private RectF rectF = new RectF();
    private int backGroundColor = Color.TRANSPARENT;

    public SignatureView(Context context) {
        super(context);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+"1");
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+"3");
    }

    public SignatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+"2");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(init)
        {
            rectF = new RectF(this.getLeft(), this.getTop(), this.getRight(),
                    this.getBottom());
            Log.e(TAG,new Exception().getStackTrace()[0].getMethodName()+" left "+rectF.left
        +" right -"+rectF.right+" top- "+rectF.top+" bottom-"+rectF.bottom);
            path = new Path();
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(brushWidth);
            mPaint.setColor(brushColor);
            setBackgroundColor(backGroundColor);
            setOnTouchListener(this);
            init= false;

        }
        canvas.drawPath(path,mPaint);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();
//        Log.e(TAG," X - "+eventX+" Y - "+eventY );

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(eventX, eventY);
                lastTouchX = eventX;
                lastTouchY  = eventY;
                return true;

            case MotionEvent.ACTION_MOVE:

            case MotionEvent.ACTION_UP:

                int historySize = event.getHistorySize();
                for (int i = 0; i < historySize; i++) {
                    float historicalX = event.getHistoricalX(i);
                    float historicalY = event.getHistoricalY(i);
                    path.lineTo(historicalX, historicalY);
                }
                path.lineTo(eventX, eventY);
                invalidate();

                break;

            default:

                return false;
        }

        lastTouchX = eventX;
        lastTouchY = eventY;

        return true;
    }
    public void clear()
    {
        path.reset();
        this.invalidate();
        init =true;
    }

    public void setBrushWidth(float brushWidth) {
        try {
            this.brushWidth = brushWidth;
            mPaint.setStrokeWidth(this.brushWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBrushColor(int brushColor) {
        this.brushColor = brushColor;
        try {
            mPaint.setColor(this.brushColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Bitmap getBitMap()
    {
        Bitmap bm = null;

        // set the signature bitmap
        if (bm == null) {
            bm = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
        }

        // important for saving signature
        final Canvas canvas = new Canvas(bm);
        this.draw(canvas);
        return bm;

    }

}
