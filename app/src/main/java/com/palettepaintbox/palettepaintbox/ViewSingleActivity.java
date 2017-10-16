package com.palettepaintbox.palettepaintbox;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.util.Log;

import java.util.ArrayList;

public class ViewSingleActivity extends AppCompatActivity {

    private ArrayList<Palette> mPaletteList;
    private PaletteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;
    private FeedReaderDbHelper mDbHelper;

    // This is called when the app first opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new FeedReaderDbHelper(this);
        setContentView(R.layout.activity_palette_single_viewer);

        ArrayList<Palette> mOnePalette = new ArrayList<>();
        Intent i = getIntent();
        int pid = i.getIntExtra("paletteID", 1);
        mOnePalette = viewSinglePalette(mOnePalette,pid);

        mAdapter = new PaletteAdapter(mOnePalette);
        RecyclerView mRecyclerSingleView = (RecyclerView) findViewById(R.id.singleview);
        mRecyclerSingleView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerSingleView.setAdapter(mAdapter);

        PaletteLargeAdapter mLarge = new PaletteLargeAdapter(mOnePalette);
        RecyclerView mRecyclerLargeView = (RecyclerView) findViewById(R.id.largeview);
        mRecyclerLargeView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerLargeView.setAdapter(mLarge);

    }

    protected ArrayList<Palette> viewSinglePalette(ArrayList<com.palettepaintbox.palettepaintbox.Palette> plist,int paletteID){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                "p.paletteName",
                "c.pColor"
        };

        String selection = "p.paletteID" + " = ?";
        //String selection = "";
        String[] selectionArgs = { "c.pID" };
        //String[] selectionArgs = {};

        String sortOrder =
                "p.paletteID DESC";

        Cursor cursor = db.rawQuery(
                "SELECT p.paletteName, c.pColor FROM Palettes p, PalettesToColors c " +
                        "WHERE p.paletteID = c.pID AND p.paletteID = '" + paletteID + "' ORDER BY p.paletteID DESC",
                null
        );

        //onDestroy();

        ArrayList<String> test3 = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                //Log.v("datadata", cursor.getString(cursor.getColumnIndex("pColor")));
                test3.add(cursor.getString(cursor.getColumnIndex("pColor")));
            }
        } finally {
            cursor.close();
        }

        // TODO: Put DB cursor in with the Adapter
        com.palettepaintbox.palettepaintbox.Palette p3 = new com.palettepaintbox.palettepaintbox.Palette("palette 3", test3);

        plist.add(p3);
        return plist;
    }

}
