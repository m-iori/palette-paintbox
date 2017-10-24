package com.palettepaintbox.palettepaintbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ColorPickerView extends View {

    private int centerX, centerY;

    private final int[] mColors;
    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private Paint mDrawPaint = new Paint();

    ColorPickerView(Context c) {
        super(c);
        mColors = new int[] {
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000
        };

        Shader s = new SweepGradient(0, 0, mColors, null);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(s);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(300);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        this.centerX = this.getMeasuredWidth() / 2;
        this.centerY = this.getMeasuredHeight() - centerX;
        float r = mPaint.getStrokeWidth() / 2;

        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            mBitmapCanvas = new Canvas(mBitmap);
            mBitmapCanvas.drawColor(Color.WHITE); // clear previously drawn stuff
            mBitmapCanvas.translate(centerX, centerY);
            mBitmapCanvas.drawOval(new RectF(-r, -r, r, r), mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mDrawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int pixel = mBitmap.getPixel(Math.round(event.getX()),Math.round(event.getY()));
        //CHECK IF POINT IS IN CIRCLE before doing anything - use distance formula from point to center and compare to radius
        //have a listener that sets the color in the linear layout palette_linear_layout

//        Drawable backgroundShape = ContextCompat.getDrawable(this.getContext(),R.drawable.color_circle);
//        backgroundShape.mutate().setColorFilter(pixel, PorterDuff.Mode.MULTIPLY);
//        Button colorButton = new Button(this.getContext());
//        colorButton.setBackground(backgroundShape);
//
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120,120);
//        layoutParams.setMargins(5,5,5,5);
//        colorButton.setLayoutParams(layoutParams);
//        mLinearLayout.addView(colorButton);
        System.out.println("touch event");
        System.out.println(pixel);
        return true;
    }

}
