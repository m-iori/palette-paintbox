package com.palettepaintbox.palettepaintbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModifyPaletteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_creator_and_editor);
        LinearLayout layout = (LinearLayout) findViewById(R.id.creator_and_editor);
        ColorPickerView colorPickerView= new ColorPickerView(layout.getContext());
        layout.addView(colorPickerView);
        ArrayList<String> paletteColors = new ArrayList<>();
        paletteColors.add("FFFFFF");
        Palette newPalette = new Palette(-1, null, paletteColors);
    }
}
