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
    private FeedReaderDbHelper mDbHelper;
    int currentPID;

    // This is called when the app first opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Database helper
        mDbHelper = new FeedReaderDbHelper(this);

        // Set view and toolbar
        setContentView(R.layout.activity_palette_single_viewer);
        Toolbar tb = (Toolbar) findViewById(R.id.paletteToolbar);
        setSupportActionBar(tb);

        // Load the palette
        ArrayList<Palette> mOnePalette = new ArrayList<>();
        Intent i = getIntent();
        int pid = i.getIntExtra("paletteID", 1);
        //Log.v("paletteID", "pid: " + pid);
        currentPID = pid;
        mOnePalette = viewSinglePalette(mOnePalette,pid);

        // Set up dot view
        mAdapter = new PaletteAdapter(mOnePalette);
        RecyclerView mRecyclerSingleView = (RecyclerView) findViewById(R.id.singleview);
        mRecyclerSingleView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerSingleView.setAdapter(mAdapter);

        // Set up large view
        PaletteLargeAdapter mLarge = new PaletteLargeAdapter(mOnePalette);
        RecyclerView mRecyclerLargeView = (RecyclerView) findViewById(R.id.largeview);
        mRecyclerLargeView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerLargeView.setAdapter(mLarge);

    }

    protected ArrayList<Palette> viewSinglePalette(ArrayList<com.palettepaintbox.palettepaintbox.Palette> plist,int paletteID){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sortOrder =
                "p.paletteID DESC";

        Cursor cursor = db.rawQuery(
                "SELECT p.paletteName, c.pColor FROM Palettes p, PalettesToColors c " +
                        "WHERE p.paletteID = c.pID AND p.paletteID = '" + paletteID + "' ORDER BY p.paletteID DESC",
                null
        );

        ArrayList<String> pColors = new ArrayList<>();
        String pname = "Palette not found.";
        Log.v("paletteID", "" + paletteID);

        try {
            while (cursor.moveToNext()) {
                Log.v("datadata", cursor.getString(cursor.getColumnIndex("pColor")));
                pColors.add(cursor.getString(cursor.getColumnIndex("pColor")));
                pname = cursor.getString(cursor.getColumnIndex("paletteName"));
            }
        } finally {
            cursor.close();
        }

        // TODO: Put DB cursor in with the Adapter
        com.palettepaintbox.palettepaintbox.Palette palette = new com.palettepaintbox.palettepaintbox.Palette(paletteID, pname, pColors);
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
                return true;

            case R.id.action_view_all:
                Intent intent_view_all = new Intent(this, MainActivity.class);
                this.startActivity(intent_view_all);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_palette, menu);
        return true;
    }

}
