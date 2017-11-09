package com.palettepaintbox.palettepaintbox;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModifyPaletteActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button mSelectedColor;
    int currentColor;
    private ArrayList<String> colors;
    private ArrayList<Button> buttons;
    private int mSelectedIndex;
    private EditText nameInput;
    private EditText hexText;
    private int paletteID;


    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_creator_and_editor);
        LinearLayout layout = findViewById(R.id.creator_and_editor);

        nameInput = findViewById(R.id.paletteNameInput);
        hexText = findViewById(R.id.paletteColorInput);

        Toolbar tb = (Toolbar) findViewById(R.id.editorToolbar);
        setSupportActionBar(tb);

        colors = new ArrayList<>();

        Intent i = getIntent();
        paletteID = i.getIntExtra("paletteID", -1);

        if(paletteID > -1) {
            Palette palette = Palette.getPalette(this, paletteID);
            nameInput.setText(palette.getName());
            String hexCode = "#FFFFFF";
            colors=palette.getColors();
            if (colors.size() > 0) {
                hexCode = colors.get(colors.size()-1);
            }
            hexText.setText(hexCode);

        } else {
            colors.add("FFFFFF");
        }

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                String hexColor = String.format("%06X", (0xFFFFFF & color));
                EditText hexText = (EditText) findViewById(R.id.paletteColorInput);
                String colorWithHash = "#" + hexColor;
                hexText.setText(colorWithHash);
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_circle);
                backgroundShape.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mSelectedColor.setBackground(backgroundShape);
                currentColor = color;
                colors.set(mSelectedIndex, hexColor);
            }
        };

        ColorPickerView colorPickerView = new ColorPickerView(layout.getContext(), l);
        layout.addView(colorPickerView);

        mLinearLayout = (LinearLayout) findViewById(R.id.palette_linear_layout);
        addColorsToView();

    }

    private void addColorsToView(){
        buttons = new ArrayList<>();
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
                    mSelectedIndex = buttons.indexOf(v);
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
                    mSelectedColor = buttons.get(colors.size()-1);
                    mSelectedIndex = buttons.indexOf(mSelectedColor);
                }
            });
            mLinearLayout.addView(addButton);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                processSavePalette(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar_editor, menu);
        return true;
    }

    protected void processSavePalette(AppCompatActivity self){
        // Save in database
        String paletteName = nameInput.getText().toString();
        if (paletteID > -1) {
            Palette.updatePalette(this, new Palette(paletteID, paletteName, colors));
        } else {
            paletteID = Palette.createPalette(this, new Palette(-1, paletteName, colors));
        }

        // Opens the Single View
        Intent intent = new Intent(self, ViewSingleActivity.class);
        intent.putExtra("paletteID", paletteID);
        startActivity(intent);
        finish();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
