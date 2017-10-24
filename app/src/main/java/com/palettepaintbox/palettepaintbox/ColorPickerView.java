package com.palettepaintbox.palettepaintbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

    private int centerX, centerY;

    private final int[] mColors;
    private Paint mPaint;
    private Paint mCenterPaint;

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

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(Color.GREEN);
        mCenterPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.centerX = this.getMeasuredWidth() / 2;
        this.centerY = this.getMeasuredHeight() - centerX;
        float r = mPaint.getStrokeWidth() / 2;
        this.getMeasuredHeight();
        canvas.translate(centerX, centerY);
        canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return true;
    }

}
