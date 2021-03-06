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

    private String orientation;
    private boolean orientationChanged = false;

    private final int[] mColors = new int[] {
            0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
            0xFFFFFF00, 0xFFFF0000
    };
    private Paint mPaint;

    private Bitmap mBitmap;
    private Canvas mBitmapCanvas;
    private int backgroundColor = Color.WHITE;
    private Paint mDrawPaint = new Paint();

    private ModifyPaletteActivity.OnColorChangedListener mListener;

    public ColorPickerView(Context c, ModifyPaletteActivity.OnColorChangedListener l, int backgroundColor) {
        super(c);
        mListener = l;
        this.backgroundColor = backgroundColor;
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

        if ((mBitmap == null) || (this.orientationChanged)) {
            this.orientationChanged = false;
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
            mBitmapCanvas = new Canvas(mBitmap);
            mBitmapCanvas.drawColor(backgroundColor); // clear previously drawn stuff
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

        int eventX = Math.round(event.getX());
        int eventY = Math.round(event.getY());
        if ((eventX < mBitmap.getWidth()) && (eventY < mBitmap.getHeight())) {
            int pixel = mBitmap.getPixel(eventX, eventY);
            mListener.colorChanged(pixel);
        }
        //CHECK IF POINT IS IN CIRCLE before doing anything - use distance formula from point to center and compare to radius
        //OR if pixel is white, -1, then ignore it
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if ( ((w > h) && (orientation == "Vertical")) ||
             ((h > w) && (orientation == "Horizontal")) ) {
            this.orientationChanged = true;
        }
        if (w > h) {
            orientation = "Horizontal";
        } else {
            orientation = "Vertical";
        }
        super.onSizeChanged(w,h,oldw,oldh);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
