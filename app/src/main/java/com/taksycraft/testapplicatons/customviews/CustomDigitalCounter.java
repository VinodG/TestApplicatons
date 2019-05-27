package com.taksycraft.testapplicatons.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


import java.util.Calendar;

public class CustomDigitalCounter extends View {

    public static final String MM_Min ="MM_Min" ;
    private Paint mPaint;
    private boolean isInit;  // it will be true once the clock will be initialized.
    private int backgroundColor =Color.TRANSPARENT;
    private int mTextColor =Color.BLACK;
    private float mTextSize =20;
    private long mEndTime=0;
    private Typeface typeFace;
    private OnTickListener listener;
    private String timeFormat;

    public CustomDigitalCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDigitalCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public  void setTimeFormat(String timeFormat)
    {
        this.timeFormat =timeFormat;
    }

    public void setTypeFace(Typeface typeFace) {
        this.typeFace = typeFace;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /** initialize necessary values */
        if (!isInit) {
            mPaint = new Paint();
            isInit = true;  // set true once initialized
        }
        /** draw the canvas-color */
        canvas.drawColor(backgroundColor);
        /** circle border */
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);


/** border of hours */
        int fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mTextSize, getResources().getDisplayMetrics());
        mPaint.setTextSize(fontSize);  // set font size (optional)
        Calendar calendar = Calendar.getInstance();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        if(mEndTime==0) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);
            hour = hour > 12 ? hour - 12 : hour;
            canvas.drawText( getNumberFromDigit(hour) + ":" + getNumberFromDigit(minutes) + ":" + getNumberFromDigit(seconds), 0, mTextSize, mPaint);  // you can draw dots to denote hours as alternative
        }
        else {
            if(typeFace!=null)
            {
                mPaint.setTypeface(typeFace);
            }

            long timeDiff  =  mEndTime-calendar.getTimeInMillis() ;
            if(listener!=null)
            {
                listener.onTick(this, timeDiff);
            }
            if(timeDiff>=0) {
                String time = getDuration(timeDiff);
                canvas.drawText(time, 0, mTextSize, mPaint);
            }
            else {
                canvas.drawText("00:00:00", 0, mTextSize, mPaint);
            }
        }
        /** invalidate the appearance for next representation of time  */
        postInvalidateDelayed(1000);
        invalidate();
    }
    public void setTextColor(int textColor)
    {
        mTextColor =textColor;
    }
    public void setTextSize(int textSize)
    {
        mTextSize =textSize;
    }
    public void setEndTime(long endTime)
    {
        mEndTime= endTime;
    }
    private    String getDuration(long milliseconds)
    {
        if(!TextUtils.isEmpty(timeFormat) && timeFormat.equalsIgnoreCase(CustomDigitalCounter.MM_Min))
        {
            int min= (int) ((milliseconds / (1000 * 60)));
            return getNumberFromDigit(min) + " mins";
        }else{
            int seconds = (int) (milliseconds / 1000) % 60;
            int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
            int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
            if (hours >= 1)
                return getNumberFromDigit(hours)+":"+getNumberFromDigit(minutes) + ":" + getNumberFromDigit(seconds);
            else{
                return  getNumberFromDigit(minutes) + ":" + getNumberFromDigit(seconds);
            }
        }
    }

    private    String getNumberFromDigit(int digit)
    {
        return  digit<=9 ? "0"+digit: ""+digit;

    }
    public void setOnTickListener(OnTickListener listener )
    {
        this.listener =listener;
    }
    public    interface OnTickListener{
        public  void onTick(View view, long timeInMills);
    }


}
