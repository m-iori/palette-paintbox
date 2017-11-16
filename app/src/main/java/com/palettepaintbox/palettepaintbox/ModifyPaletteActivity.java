package com.palettepaintbox.palettepaintbox;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifyPaletteActivity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button mSelectedColor;
    private ArrayList<String> colors;
    private ArrayList<Button> buttons;
    private int mSelectedIndex;
    private EditText nameInput;
    private EditText hexText;
    private int paletteID;
    private FeedReaderDbHelper mDbHelper;
    private String currentTheme;

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new FeedReaderDbHelper(this);

        currentTheme = getCurrentTheme();
        if(currentTheme.equals("dark")){
            setTheme(R.style.darkTheme);
        }
        else{
            setTheme(R.style.lightTheme);
        }

        setContentView(R.layout.palette_creator_and_editor);
        LinearLayout layout = findViewById(R.id.creator_and_editor);

        nameInput = findViewById(R.id.paletteNameInput);
        hexText = findViewById(R.id.paletteColorInput);

        hexText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Pattern p = Pattern.compile("#[0-9A-Fa-f]{6}");
                String colorString = hexText.getText().toString();
                Matcher m = p.matcher(colorString);
                boolean valid = m.matches();
                if(valid){
                    Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_circle);
                    backgroundShape.mutate().setColorFilter(Color.parseColor(colorString), PorterDuff.Mode.MULTIPLY);
                    mSelectedColor.setBackground(backgroundShape);
                    colors.set(mSelectedIndex, colorString.substring(1,colorString.length()));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}

        });

        Toolbar tb = findViewById(R.id.editorToolbar);
        setSupportActionBar(tb);

        if((savedInstanceState != null) && savedInstanceState.containsKey("COLORS_KEY")) {
            colors = savedInstanceState.getStringArrayList("COLORS_KEY");
        } else {
            colors = new ArrayList<>();

            Intent i = getIntent();
            paletteID = i.getIntExtra("paletteID", -1);

            if (paletteID > -1) {
                Palette palette = Palette.getPalette(this, paletteID);
                nameInput.setText(palette.getName());
                String hexCode = "#FFFFFF";
                colors = palette.getColors();
                if (colors.size() > 0) {
                    hexCode = colors.get(colors.size() - 1);
                }
                hexText.setText(hexCode);

            } else {
                colors.add("FFFFFF");
            }
        }

        mLinearLayout = findViewById(R.id.palette_linear_layout);
        addColorsToView();

        OnColorChangedListener l = new OnColorChangedListener() {
            public void colorChanged(int color) {
                String hexColor = String.format("%06X", (0xFFFFFF & color));
                EditText hexText = findViewById(R.id.paletteColorInput);
                String colorWithHash = "#" + hexColor;
                hexText.setText(colorWithHash);
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_circle);
                backgroundShape.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
                mSelectedColor.setBackground(backgroundShape);
                colors.set(mSelectedIndex, hexColor);
            }
        };

        int backgroundColor;
        if(currentTheme.equals("dark")){
            backgroundColor = Color.BLACK;
        }
        else{
            backgroundColor = Color.WHITE;
        }

        ColorPickerView colorPickerView = new ColorPickerView(layout.getContext(), l, backgroundColor);
        layout.addView(colorPickerView);
    }

    private void addColorsToView(){
        buttons = new ArrayList<>();
        mLinearLayout.removeAllViews();
        for(String color : this.colors) {
            Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_circle);
            String hexColor = "#" + color;
            backgroundShape.mutate().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.MULTIPLY);
            Button colorButton = new Button(mLinearLayout.getContext());

            WindowManager wm = (WindowManager) mLinearLayout.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;

            colorButton.setBackground(backgroundShape);
            colorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedColor = (Button)v;
                    mSelectedIndex = buttons.indexOf(v);
                }
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width/7, width/7);
            layoutParams.setMargins(5, 5, 5, 5);
            colorButton.setLayoutParams(layoutParams);
            mLinearLayout.addView(colorButton);
            buttons.add(colorButton);
        }
        mSelectedColor = buttons.get(buttons.size()-1);
        mSelectedIndex = buttons.size()-1;

        if (colors.size() < 6) {
            Drawable backgroundShape = null;
            if(currentTheme.equals("light")) {
                backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.ic_add_black_24dp);
            }
            else{
                backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.ic_add_white_24dp);
            }
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

    protected String getCurrentTheme(){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // SQL to get all palettes
        Cursor cursor = db.rawQuery(
                "SELECT theme FROM Preferences",
                null
        );

        try {
            while (cursor.moveToNext()) {
                String currentTheme = cursor.getString(cursor.getColumnIndex("theme"));
                if (currentTheme.equals("dark")) {
                    return "dark";
                } else {
                    return "light";
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return "light";
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

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("COLORS_KEY", this.colors);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
}
