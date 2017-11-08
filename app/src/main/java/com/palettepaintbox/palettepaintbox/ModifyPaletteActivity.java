package com.palettepaintbox.palettepaintbox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ModifyPaletteActivity extends AppCompatActivity {

    private FeedReaderDbHelper mDbHelper;
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
        LinearLayout layout = (LinearLayout) findViewById(R.id.creator_and_editor);

        mDbHelper = new FeedReaderDbHelper(this);

        nameInput = (EditText)(findViewById(R.id.paletteNameInput));
        hexText = (EditText) findViewById(R.id.paletteColorInput);

        Toolbar tb = (Toolbar) findViewById(R.id.editorToolbar);
        setSupportActionBar(tb);

        colors = new ArrayList<>();

        Intent i = getIntent();
        paletteID = i.getIntExtra("paletteID", -1);

        if(paletteID > -1) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT p.paletteName, c.pColor FROM Palettes p, PalettesToColors c " +
                            "WHERE p.paletteID = c.pID AND p.paletteID = '" + paletteID + "' ORDER BY p.paletteID DESC",
                    null
            );

            String paletteName = "Palette not found.";
            String hexCode = "FFFFFF";
            int p=0;
            try {
                while (cursor.moveToNext()) {
                    Log.v("datadata", cursor.getString(cursor.getColumnIndex("pColor")));
                    colors.add(cursor.getString(cursor.getColumnIndex("pColor")));
                    if (p==0) {
                        paletteName = cursor.getString(cursor.getColumnIndex("paletteName"));
                        hexCode = "#" + colors.get(0);
                    }
                    p++;
                }
            } finally {
                cursor.close();
            }
            nameInput.setText(paletteName);
            hexText.setText(hexCode);

        } else {
            colors.add("FFFFFF");
        }

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                String hexColor = String.format("%06X", (0xFFFFFF & color));
                EditText hexText = (EditText) findViewById(R.id.paletteColorInput);
                hexText.setText("#" + hexColor);
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
        if (paletteID > -1) {
            updateExistingPalette(nameInput.getText().toString(), colors);
        } else {
            paletteID = saveNewPalette(nameInput.getText().toString(), colors);
        }

        // Opens the Single View
        Intent intent = new Intent(self, ViewSingleActivity.class);
        intent.putExtra("paletteID", paletteID);
        startActivity(intent);
        finish();
    }

    // Saves a new palette
    public int saveNewPalette(String paletteName, ArrayList<String> paletteColors){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add the name
        values.put("paletteName", paletteName);
        long pid = db.insert("Palettes", null, values);


        for(String color : paletteColors) {
            // TODO: Check if palette already has 6 colors before inserting!
            values = new ContentValues();
            values.put("pID", pid+0);
            values.put("pColor", color);
            db.insert("PalettesToColors", null, values);
        }

        mDbHelper.close();
        return (int)(pid);
    }

    // Updates an existing palette
    public int updateExistingPalette(String paletteName, ArrayList<String> paletteColors){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String paletteFilter = "paletteID=" + paletteID;
        // Add the palette
        values.put("paletteName", paletteName);
        long pid = db.update("Palettes", values, paletteFilter, null);
        //delete existing colors for palette
        String colorFilter = "pID=" + paletteID;
        db.delete("PalettesToColors",colorFilter,null);

        System.out.print(colors);
        for(String color : colors) {
            //add colors for palette
            values = new ContentValues();
            values.put("pID", pid+0);
            values.put("pColor", color);
            db.insert("PalettesToColors", null, values);
        }

        mDbHelper.close();
        return (int)(pid);
    }

    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
