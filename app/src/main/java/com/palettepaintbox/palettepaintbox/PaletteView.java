package com.palettepaintbox.palettepaintbox;

//https://developer.android.com/training/custom-views/create-view.html
//https://developer.android.com/training/custom-views/index.html

import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

class PaletteView extends View {
    final float RADIUS = (float)(4.0);

    public PaletteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(204);
        canvas.drawCircle((float)(0.50), (float)(0.50), RADIUS, paint);

        // Draw the label text
        // canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY, mTextPaint);

        // canvas.drawCircle(mPointerX, mPointerY, mPointerSize, mTextPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}