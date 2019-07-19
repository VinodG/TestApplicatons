package com.taksycraft.testapplicatons.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.taksycraft.testapplicatons.R;

public class FloatingView extends RelativeLayout {
    private   Context context =null;
    private int deviceWidth;
    private int deviceHeight;
    private View floatingView;

    public FloatingView(Context context) {
        super(context);
        this. context =context;
        init(context);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this. context =context;
        init(context);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this. context =context;
        init(context);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this. context =context;
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceWidth = deviceDisplay.x;
        deviceHeight = deviceDisplay.y;
        floatingView = LayoutInflater.from(context).inflate(R.layout.chat_item,null);
        floatingView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutParams params = ((LayoutParams) floatingView.getLayoutParams());
        params.addRule( RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule( RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins( 0,0, 130,60);
        floatingView.setBackgroundColor(Color.RED);
        addView(floatingView,params);
    }
    public View getChatParentView()
    {
        return floatingView;
    }
    public View getNotificationView()
    {
        return floatingView;
    }
}
