package com.taksycraft.testapplicatons.customviews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taksycraft.testapplicatons.R;

public class BackgroundDrawableTesingActivity extends AppCompatActivity {

    private TextView tv;
    private TextView tv2;
    private ImageView ivSwitch;
    private TextView myText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_drawable_tesing);
        tv= (TextView)findViewById(R.id.tv);
        tv2= (TextView)findViewById(R.id.tv2);
        ivSwitch= (ImageView)findViewById(R.id.ivSwitch);
        tv.setBackground(new BackgroundDrawable()
                .setBackgroundColor(Color.BLACK)
                .setCornerRadius(30,30, 0, 0)
                .getDrawable());
        tv2.setBackground(new BackgroundDrawable()
                .setBackgroundColor(Color.GREEN)
                .setCornerRadius(30)
                .getDrawable());
        tv.setBackgroundResource(R.drawable.apple);
        Drawable drawable = getResources().getDrawable(R.drawable.apple);
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_OUT);
        tv2.setText(getAlpha(12,"#123456"));
        for(int i = 0;i<101;i++)
        {
            Log.e("Myapp",i+" % - "+getAlpha(i,"#123456"));
        }


        ivSwitch.setImageBitmap(flip(getRoundedCloseIcon()));
        setScrolls();
    }

    private void setScrolls() {
        myText =(TextView)findViewById(R.id.myText);
        myText.setSelected(true);
        ScrollTextView scrolltext=(ScrollTextView) findViewById(R.id.scrolltext);
        scrolltext.setTextColor(Color.BLACK);
        scrolltext.startScroll();
        ScrollTextView scrolltext2=(ScrollTextView) findViewById(R.id.scrolltext2);
        scrolltext2.setTextColor(Color.BLACK);
        scrolltext2.setScrollSpeed(200f);
        scrolltext2.startScroll();
    }

    public static Bitmap flip(Bitmap src)
    {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return  dst ;
    }
    private Bitmap getRoundedCloseIcon() {
//        Drawable switchDrawable = getResources().getDrawable(R.drawable.icn_popup_only_x_close);
        Drawable switchDrawable = getResources().getDrawable(R.drawable.icn_calendar_previous);
        Drawable switchDrawable2 = new BackgroundDrawable()
                .setCornerRadius(35)
                .setBackgroundColor(Color.BLACK)
                .getDrawable();
        Bitmap bmSwitch= convertToBitmap(switchDrawable,50,50);
        Bitmap bmBackGround= convertToBitmap(switchDrawable2,60,60);
        int bitmap1Width = bmSwitch.getWidth();
        int bitmap1Height = bmSwitch.getHeight();
        int bitmap2Width = bmBackGround.getWidth();
        int bitmap2Height = bmBackGround.getHeight();

        float marginLeft = (float) (bitmap2Width * 0.5 - bitmap1Width * 0.5 );
        float marginTop = (float) (bitmap2Height * 0.5 - bitmap1Height * 0.5  );

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap2Width, bitmap2Height, bmBackGround.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bmBackGround, new Matrix(), null);
        canvas.drawBitmap(bmSwitch, marginLeft, marginTop, null);
        return finalBitmap;
    }
    private Bitmap getCombinedBitmap(Drawable drawableMain, Drawable backGround) {
        drawableMain.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        Bitmap bmSwitch= convertToBitmap(drawableMain,50,50);
        Bitmap bmBackGround= convertToBitmap(backGround,60,60);
        int bitmap1Width = bmSwitch.getWidth();
        int bitmap1Height = bmSwitch.getHeight();
        int bitmap2Width = bmBackGround.getWidth();
        int bitmap2Height = bmBackGround.getHeight();

        float marginLeft = (float) (bitmap2Width * 0.5 - bitmap1Width * 0.5 );
        float marginTop = (float) (bitmap2Height * 0.5 - bitmap1Height * 0.5  );

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap2Width, bitmap2Height, bmBackGround.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bmBackGround, new Matrix(), null);
        canvas.drawBitmap(bmSwitch, marginLeft, marginTop, null);
        return finalBitmap;
    }

    private Bitmap getSwitch_ON_Drawable() {
        Drawable switchDrawable = new BackgroundDrawable()
                .setCornerRadius(50)
                .setBackgroundColor(Color.GREEN)
                .setBorder(1,Color.WHITE)
                .getDrawable();
        Drawable switchDrawable2 = new BackgroundDrawable()
                .setCornerRadius(50)
                .setBorder(1,Color.BLACK)
                .setBackgroundColor(Color.WHITE)
                .getDrawable();
        Bitmap bmSwitch= convertToBitmap(switchDrawable,100,50);
        Bitmap bmSwitch2= convertToBitmap(switchDrawable2,60,50);
//        return bitmapOverlayToCenter( bmSwitch,bmSwitch2,false) ;
        return bitmapOverlayToCenter( bmSwitch,bmSwitch2,true) ;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    public Bitmap bitmapOverlayToCenter(Bitmap bitmap1, Bitmap overlayBitmap,boolean isLeftAlign) {
        int bitmap1Width = bitmap1.getWidth();
        int bitmap1Height = bitmap1.getHeight();
        int bitmap2Width = overlayBitmap.getWidth();
        int bitmap2Height = overlayBitmap.getHeight();

        float marginLeft = (float) (bitmap1Width * 0.5 - bitmap2Width * 0.5);
        float marginTop = (float) (bitmap1Height * 0.5 - bitmap2Height * 0.5);
        if (isLeftAlign)
        {
            marginLeft = 0;
        }else{
            marginLeft = bitmap1Width - bitmap2Width;
        }

        Bitmap finalBitmap = Bitmap.createBitmap(bitmap1Width, bitmap1Height, bitmap1.getConfig());
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap1, new Matrix(), null);
        canvas.drawBitmap(overlayBitmap, marginLeft, marginTop, null);
        return finalBitmap;
    }
    public Bitmap getCircleBitmap1(Bitmap source) {
        int size = Math.min(source.getHeight(), source.getWidth());

        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = Color.RED;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, size, size);
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, rect, rect, paint);

        return output;
    }

    private String getAlpha(int number,String colorConstants) {
        try{
            if(number>100)
                number=100;
            if(number<0)
                number=0;
            String subStr  =   colorConstants.replace("#","");
            String str = Integer.toString(Math.round(number*(255.0f/100)),16);
            if(str.length()==1)
                str ="0"+str;
            return ( "#"+str+subStr);
        }catch (Exception e)
        {
            e.printStackTrace();
            return "#000000";
        }
    }
}
