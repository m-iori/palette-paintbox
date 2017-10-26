package com.palettepaintbox.palettepaintbox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
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

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_creator_and_editor);
        LinearLayout layout = (LinearLayout) findViewById(R.id.creator_and_editor);

        Toolbar tb = (Toolbar) findViewById(R.id.editorToolbar);
        setSupportActionBar(tb);

        Intent i = getIntent();
        int pid = i.getIntExtra("paletteID", 1);
        if(pid > -1){
            // load the palette with this pid
        }

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
                backgroundShape.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mSelectedColor.setBackground(backgroundShape);
                currentColor = color;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                processNewPalette(this);
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

    protected void processNewPalette(AppCompatActivity self){
        ArrayList<String> newColors = new ArrayList<String>();
        //EditText colorInput = (EditText)(findViewById(R.id.paletteColorInput));
        //https://stackoverflow.com/questions/6539879/how-to-convert-a-color-integer-to-a-hex-string-in-android

        // Add all new colors here using this trick to convert from int to hex
        String hexc = String.format("%06X", (0xFFFFFF & currentColor));
        newColors.add(hexc);

        // Get name
        EditText nameInput = (EditText)(findViewById(R.id.paletteNameInput));

        // Save in database
        int newPaletteID = saveNewPalette(nameInput.getText().toString(), newColors);

        // Opens the Single View
        Intent intent = new Intent(self, ViewSingleActivity.class);
        intent.putExtra("paletteID", newPaletteID);
        startActivity(intent);
    }

    // Saves a new palette
    public int saveNewPalette(String paletteName, ArrayList<String> paletteColors){
        mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Add the name
        values.put("paletteName", paletteName);
        long pid = db.insert("Palettes", null, values);

        long newRowId = 0;

        for(String color : paletteColors) {
            // TODO: Check if palette already has 6 colors before inserting!
            values = new ContentValues();
            values.put("pID", pid+0);
            values.put("pColor", color);
            newRowId = db.insert("PalettesToColors", null, values);
        }

        mDbHelper.close();
        return (int)(pid);
    }

    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }
}
