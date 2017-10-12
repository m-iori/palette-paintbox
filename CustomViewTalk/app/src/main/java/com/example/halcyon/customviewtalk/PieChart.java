package com.example.halcyon.customviewtalk;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.BlurMaskFilter;
import java.util.ArrayList;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.LinearGradient;

/**
 * TODO: document your custom view class.
 */
public class PieChart extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private int mTextColor = 0;
    private Paint mPiePaint;
    private Paint mShadowPaint;
    private RectF mShadowBounds;
    private int mTextX;
    private int mTextY;
    private ArrayList<Item> mData;
    private int mShader;
    private RectF mBounds;
    private int mEndAngle;
    private int mStartAngle;
    private int mPointerX;
    private int mPointerY;
    private int mPointerSize;
    private int mCurrentItem;

    public PieChart(Context context) {
        super(context);
        init(null, 0);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieChart, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.PieChart_exampleString);
        mExampleColor = a.getColor(
                R.styleable.PieChart_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.PieChart_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.PieChart_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.PieChart_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        // mTextPaint = new TextPaint();
        // mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextColor = 0xff0000bb;
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPiePaint.setStyle(Paint.Style.FILL);
        mPiePaint.setColor(0xffff0000);
        mPiePaint.setTextSize(mTextHeight);

        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        mShadowBounds = new RectF(50, 100, 50, 100);
        mTextX = 50;
        mTextY = 50;
        int[] colors = {0, 100};
        float[] positions = {(float)(5.00), (float)(7.00)};

        Shader s = new LinearGradient((float)(100.00),
                (float)(100.00),
                (float)(250.00),
                (float)(400.00), colors, positions, Shader.TileMode.MIRROR);
        Item sample = new Item("Practice", 50, 70, s);
        mData = new ArrayList<>();
        mData.add(sample);
        mShader = 0;
        mBounds = new RectF(100, 300, 100, 300);
        mPointerX = 200;
        mPointerY = 250;
        mPointerSize = 200;
        mCurrentItem = 0;
        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the shadow
        canvas.drawOval(
                mShadowBounds,
                mShadowPaint
        );

        // Draw the label text
        canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY, mTextPaint);

        // Draw the pie slices
        for (int i = 0; i < mData.size(); ++i) {
            Item it = mData.get(i);
            mPiePaint.setShader(it.mShader);
            canvas.drawArc(mBounds,
                    360 - it.mEndAngle,
                    it.mEndAngle - it.mStartAngle,
                    true, mPiePaint);
        }

        // Draw the pointer
        canvas.drawLine(mTextX, mPointerY, mPointerX, mPointerY, mTextPaint);
        canvas.drawCircle(mPointerX, mPointerY, mPointerSize, mTextPaint);
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
