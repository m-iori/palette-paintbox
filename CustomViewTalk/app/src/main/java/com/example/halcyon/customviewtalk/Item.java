package com.example.halcyon.customviewtalk;

import android.graphics.Shader;
/**
 * Created by Halcyon on 10/12/2017.
 */

public class Item {
    protected String mLabel;
    protected int mStartAngle;
    protected int mEndAngle;
    protected Shader mShader;

    protected Item(String label, int startangle, int endangle, Shader shader){
        this.mLabel = label;
        this.mStartAngle = startangle;
        this.mEndAngle = endangle;
        this.mShader = shader;
    }
}
