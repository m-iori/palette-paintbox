package com.palettepaintbox.palettepaintbox;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModifyPaletteActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button mSelectedColor;
    private ArrayList<String> colors;
    private ArrayList<Button> buttons;

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
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
                EditText hexText = (EditText) findViewById(R.id.paletteColorInput);
                hexText.setText(hexColor);
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
                backgroundShape.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mSelectedColor.setBackground(backgroundShape);
            }
        };

        ColorPickerView colorPickerView= new ColorPickerView(layout.getContext(), l);
        layout.addView(colorPickerView);

        colors = new ArrayList<>();
        buttons = new ArrayList<>();

        colors.add("FFFFFF");
        colors.add("FFFFFF");

        mLinearLayout = (LinearLayout) findViewById(R.id.palette_linear_layout);
        addColorsToView();

    }

    private void addColorsToView(){
        mLinearLayout.removeAllViews();
        for(String color : this.colors) {
            Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_circle);
            String hexColor = "#" + color;
            backgroundShape.mutate().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.MULTIPLY);
            Button colorButton = new Button(mLinearLayout.getContext());
            colorButton.setBackground(backgroundShape);
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedColor = (Button)v;
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            layoutParams.setMargins(5, 5, 5, 5);
            colorButton.setLayoutParams(layoutParams);
            mLinearLayout.addView(colorButton);
            buttons.add(colorButton);
        }
        mSelectedColor = buttons.get(0);

        if (colors.size() < 6) {
            Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.ic_add_black_24dp);
            Button addButton = new Button(mLinearLayout.getContext());
            addButton.setBackground(backgroundShape);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 120);
            layoutParams.setMargins(5, 5, 5, 5);
            addButton.setLayoutParams(layoutParams);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    colors.add("FFFFFF");
                    addColorsToView();
                }
            });
            mLinearLayout.addView(addButton);
        }
    }
}
