package com.palettepaintbox.palettepaintbox;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ColorPickerView extends View {

    private int centerX, centerY;

    private final int[] mColors = new int[] {
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
            0xFFFFFF00, 0xFFFF0000
    };
    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private Paint mDrawPaint = new Paint();

    private ModifyPaletteActivity.OnColorChangedListener mListener;

    public ColorPickerView(Context c, ModifyPaletteActivity.OnColorChangedListener l) {
        super(c);
        mListener = l;
        initializeView();
    }

    public ColorPickerView(Context context) {
        super(context);
        initializeView();
    }
    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    private void initializeView(){
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
        //Hide keyboard if it's showing
        Activity activity = (Activity)getContext();
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

        int pixel = mBitmap.getPixel(Math.round(event.getX()),Math.round(event.getY()));
        mListener.colorChanged(pixel);
        //CHECK IF POINT IS IN CIRCLE before doing anything - use distance formula from point to center and compare to radius
        //OR if pixel is white, -1, then ignore it
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
