package com.palettepaintbox.palettepaintbox;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.view.View;

public class ColorPickerView extends View {

    private static final int CENTER_X = 100;
    private static final int CENTER_RADIUS = 32;

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
        mPaint.setStrokeWidth(32);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(Color.GREEN);
        mCenterPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;

        canvas.translate(CENTER_X, CENTER_X);

        canvas.drawOval(new RectF(-r, -r, r, r), mPaint);
        canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);

//        if (mTrackingCenter) {
//            int c = mCenterPaint.getColor();
//            mCenterPaint.setStyle(Paint.Style.STROKE);
//
//            if (mHighlightCenter) {
//                mCenterPaint.setAlpha(0xFF);
//            } else {
//                mCenterPaint.setAlpha(0x80);
//            }
//            canvas.drawCircle(0, 0,
//                    CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
//                    mCenterPaint);
//
//            mCenterPaint.setStyle(Paint.Style.FILL);
//            mCenterPaint.setColor(c);
//        }
    }

}
