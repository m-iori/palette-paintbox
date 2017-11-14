package com.palettepaintbox.palettepaintbox;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/*
 * This Activity runs when viewing a single palette.
 */
public class ViewSingleActivity extends AppCompatActivity {

    private PaletteAdapter mAdapter;
    int currentPID;
    private FeedReaderDbHelper mDbHelper;

    public static String colorCodeFormat = "hex";

    // This is called when the app first opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new FeedReaderDbHelper(this);

        setColorFormat();

        String currentTheme = getCurrentTheme();
        if(currentTheme.equals("dark")){
            setTheme(R.style.darkTheme);
        }
        else{
            setTheme(R.style.lightTheme);
        }

        // Set view and toolbar
        setContentView(R.layout.activity_palette_single_viewer);
        Toolbar tb = findViewById(R.id.paletteToolbar);
        setSupportActionBar(tb);

        // Load the palette
        ArrayList<Palette> mOnePalette = new ArrayList<>();
        Intent i = getIntent();
        int pid = i.getIntExtra("paletteID", 1);
        currentPID = pid;
        mOnePalette = viewSinglePalette(mOnePalette,pid);

        // Set up dot view
        mAdapter = new PaletteAdapter(mOnePalette);
        RecyclerView mRecyclerSingleView = findViewById(R.id.singleview);
        mRecyclerSingleView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerSingleView.setAdapter(mAdapter);

        // Set up large view
        PaletteLargeAdapter mLarge = new PaletteLargeAdapter(mOnePalette);
        RecyclerView mRecyclerLargeView = findViewById(R.id.largeview);
        mRecyclerLargeView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerLargeView.setAdapter(mLarge);

    }

    protected ArrayList<Palette> viewSinglePalette(ArrayList<com.palettepaintbox.palettepaintbox.Palette> plist, int paletteID){
        Palette palette = Palette.getPalette(this, paletteID);
        plist.add(palette);
        return plist;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, ModifyPaletteActivity.class);
                intent.putExtra("paletteID", currentPID);
                this.startActivity(intent);
                finish();
                return true;

            case R.id.action_view_all:
                Intent intent_view_all = new Intent(this, ViewAllActivity.class);
                this.startActivity(intent_view_all);
                finish();
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
        }

        return "light";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_palette, menu);
        return true;
    }

    protected void setColorFormat(){

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // SQL to get all palettes
        Cursor cursor = db.rawQuery(
                "SELECT showHex,showRGB FROM Preferences",
                null
        );

        try {
            while (cursor.moveToNext()) {
                String hex = cursor.getString(cursor.getColumnIndex("showHex"));
                String rgb = cursor.getString(cursor.getColumnIndex("showRGB"));
                if (hex.equals("true")) {
                    colorCodeFormat = "hex";
                }
                if (rgb.equals("true")){
                    colorCodeFormat = "rgb";
                }
                if(hex.equals("true") && rgb.equals("true")){
                    colorCodeFormat = "both";
                }
                if(!hex.equals("true") && !rgb.equals("true")) {
                    colorCodeFormat = "none";
                }
            }
        } finally {
            cursor.close();
        }
    }

}
