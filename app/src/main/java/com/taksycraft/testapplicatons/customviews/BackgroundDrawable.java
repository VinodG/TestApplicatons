package com.taksycraft.testapplicatons.customviews;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

public class BackgroundDrawable {
    private  GradientDrawable drawable;
    private int drawableType = GradientDrawable.RECTANGLE;
    private int borderColor= Color.TRANSPARENT;
    private int backgroundColor =Color.WHITE;
    //private int borderColor= Color.TRANSPARENT;
    //private int backgroundColor =Color.TRANSPARENT;
    private int cornerRadiusWidth =0;
    private int borderWidth =0;
    private int[] gradientColors;
    private GradientDrawable.Orientation gradientShapeType =GradientDrawable.Orientation.TOP_BOTTOM;
    private int topLeft;
    private int topRight;
    private int bottomLeft;
    private int bottomRight;

    public BackgroundDrawable()
    {
        drawable =  new GradientDrawable();
    }

    public Drawable getDrawable ()
    {
        setView();
        return drawable;

    }
    public BackgroundDrawable  setBorder(int borderWidth,int borderColor )
    {
        this.borderWidth =borderWidth;
        this.borderColor = borderColor;
        return this;
    }
    public BackgroundDrawable  setBorder( int borderColor )
    {
        return  setBorder(borderWidth,borderColor);
    }
    public BackgroundDrawable  setBackgroundColor( int backgroundColor )
    {
        this.backgroundColor =backgroundColor;
        return  this;
    }
    public BackgroundDrawable  setCornerRadius( int cornerRadiusWidth )
    {
        this.cornerRadiusWidth =cornerRadiusWidth;
        return  this;
    }
    public BackgroundDrawable  setCornerRadius( int topleft,int topRight,int bottomRight   , int bottomLeft )
    {
        this.topLeft=topleft;
        this.topRight=topRight;
        this.bottomLeft=bottomLeft;
        this.bottomRight=bottomRight;
        return  this;
    }
    private void  setView ()
    {
        drawable.setShape(drawableType);
        drawable.setColor(backgroundColor);
        if(cornerRadiusWidth!=0)
            drawable.setCornerRadius(cornerRadiusWidth);
        else {
            drawable.mutate();
            drawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight,bottomRight, bottomRight,
                    bottomLeft, bottomLeft, });
        }
        drawable.setStroke(borderWidth, borderColor);
        drawable.setColors(gradientColors);
        drawable.setOrientation( gradientShapeType);

    }
    public BackgroundDrawable setDrawableType(BackgroundDrawableType type)
    {
        switch (type)
        {
            case OVAL:
                drawableType =GradientDrawable.OVAL;
                break;

            case RECTANGLE:
            default: drawableType =GradientDrawable.RECTANGLE;
                break;
        }
        return this;

    }
    public BackgroundDrawable setGradientColors(int [] gradientColors,BackgroundDrawableGradientType gradientShape)
    {
        this.gradientColors =gradientColors;
        this.gradientShapeType  =getGradientType(gradientShape) ;
        return this;
    }
    public BackgroundDrawable setGradientColors(int [] gradientColors )
    {
        this.gradientColors =gradientColors;
        return setGradientColors( gradientColors, BackgroundDrawableGradientType.TOP_BOTTOM);
    }

    private GradientDrawable.Orientation getGradientType(BackgroundDrawableGradientType gradientShape) {
        switch (gradientShape)
        {
            case LEFT_RIGHT:
                gradientShapeType =GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case TOP_BOTTOM:
                gradientShapeType =GradientDrawable.Orientation.TOP_BOTTOM;
            default: gradientShapeType =GradientDrawable.Orientation.TOP_BOTTOM;
                break;
        }
        return gradientShapeType;
    }

//    public void setGradientOrienationShapeType(BackgroundDrawableGradientType gradientShape) {
//        switch (gradientShape)
//        {
//            case LEFT_RIGHT:
//                gradientShapeType =GradientDrawable.Orientation.LEFT_RIGHT;
//                break;
//            case TOP_BOTTOM:
//                gradientShapeType =GradientDrawable.Orientation.TOP_BOTTOM;
//            default: gradientShapeType =GradientDrawable.Orientation.TOP_BOTTOM;
//                break;
//        }
//    }

    public enum BackgroundDrawableType
    {
        //        LINE,LINEAR_GRADIENT,OVAL,RADIAL_GRADIENT, RECTANGLE , RING ,SWEEP_GRADIENT ;
        OVAL, RECTANGLE   ;
    }
    public enum BackgroundDrawableGradientType
    {
        //        LINE,LINEAR_GRADIENT,OVAL,RADIAL_GRADIENT, RECTANGLE , RING ,SWEEP_GRADIENT ;
        LEFT_RIGHT ,TOP_BOTTOM;
    }
    public BackgroundDrawable clone(BackgroundDrawable backgroundDrawable)
    {
        BackgroundDrawable drawable = new BackgroundDrawable();
        drawable.drawable= backgroundDrawable.drawable;
        drawable.drawableType = backgroundDrawable.drawableType;
        drawable.borderColor= backgroundDrawable.borderColor;
        drawable.backgroundColor =backgroundDrawable.backgroundColor;
        drawable.cornerRadiusWidth =backgroundDrawable.cornerRadiusWidth;
        drawable.borderWidth =backgroundDrawable.borderWidth;
        drawable. gradientColors=backgroundDrawable.gradientColors;
        drawable.gradientShapeType =backgroundDrawable.gradientShapeType;
        return drawable;

    }

}