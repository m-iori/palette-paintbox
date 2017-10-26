package com.palettepaintbox.palettepaintbox;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModifyPaletteActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button mSelectedColor;

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_creator_and_editor);
        LinearLayout layout = (LinearLayout) findViewById(R.id.creator_and_editor);

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
                backgroundShape.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mSelectedColor.setBackground(backgroundShape);
            }
        };

        ColorPickerView colorPickerView= new ColorPickerView(layout.getContext(), l);
        layout.addView(colorPickerView);
        ArrayList<String> paletteColors = new ArrayList<>();
        paletteColors.add("FFFFFF");
        Palette newPalette = new Palette(-1, null, paletteColors);

        mLinearLayout = (LinearLayout) findViewById(R.id.palette_linear_layout);
        Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
        backgroundShape.mutate().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.MULTIPLY);
        Button colorButton = new Button(mLinearLayout.getContext());
        colorButton.setBackground(backgroundShape);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120,120);
        layoutParams.setMargins(5,5,5,5);
        colorButton.setLayoutParams(layoutParams);
        mLinearLayout.addView(colorButton);
        mSelectedColor = colorButton;
    }
}
