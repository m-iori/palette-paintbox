package com.palettepaintbox.palettepaintbox;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //https://developer.android.com/training/basics/data-storage/databases.html
    //https://developer.android.com/reference/android/graphics/drawable/shapes/Shape.html
    //https://developer.android.com/reference/android/graphics/Canvas.html
    //https://developer.android.com/reference/android/graphics/Paint.html

    // This is the database reader.
    private FeedReaderDbHelper mDbHelper;

    private ArrayList<Palette> mPaletteList;
    private PaletteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    // This is called when the app first opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new FeedReaderDbHelper(this);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);

        mPaletteList = new ArrayList<>();

        //MAKE UP PALETTES FOR TESTING - GET FROM DB LATER
        ArrayList<String> testing1 = new ArrayList<>();
        testing1.add("800000");
        testing1.add("000000");
        Palette p1 = new Palette("palette 1", testing1);

        ArrayList<String> testing2 = new ArrayList<>();
        testing2.add("FFFF00");
        Palette p2 = new Palette("palette 2", testing2);

        mPaletteList.add(p1);
        mPaletteList.add(p2);

        /*TEST*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("paletteName", "thefirstone"); // get palette title from UI
        long newRowId = db.insert("Palettes", null, values);

        // TODO: Check if palette already has 6 colors before inserting!
        values = new ContentValues();
        values.put("pID", 1);
        values.put("pColor", "FF0000");
        newRowId = db.insert("PalettesToColors", null, values);

        values = new ContentValues();
        values.put("pID", 1);
        values.put("pColor", "00FF00");
        newRowId = db.insert("PalettesToColors", null, values);

        values = new ContentValues();
        values.put("pID", 1);
        values.put("pColor", "0000FF");
        newRowId = db.insert("PalettesToColors", null, values);

        viewAllPalettes();
        /*TEST*/

        //END MAKE UP PALETTES

        mAdapter = new PaletteAdapter(mPaletteList);
        mRecyclerView.setAdapter(mAdapter);

        // A PaletteView is a custom view which we create.
        // We can get palettes from the database.
        // Then, we load the colors from that palette data into a PaletteView.
        // The PaletteView is several circle Shapes drawn onto a Canvas with the defined color of Paint.

        // https://stackoverflow.com/questions/6216547/android-dynamically-add-views-into-view
        /*LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.your_layout, null);

        PaletteView paletteView = (PaletteView) new PaletteView(this, new AttributeSet());
        textView.setText("your text");

        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.insert_point);
        insertPoint.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));*/
    }

    protected void createNewPalette(){
        setContentView(R.layout.activity_palette_creator_and_editor);
    }

    protected void saveNewPalette(){
        //https://developer.android.com/training/basics/data-storage/databases.html
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("paletteTitle", "test"); // get palette title from UI

        long newRowId = db.insert("Palettes", null, values);

        onDestroy();
    }

    protected void editExistingPalette(){
        setContentView(R.layout.activity_palette_creator_and_editor);
    }

    protected void saveExistingPalette(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("paletteTitle", "newtitle"); //get the new title from the UI

        String selection = "paletteID" + " = ?";
        String[] selectionArgs = { "id" };

        int count = db.update(
                "Palettes",
                values,
                selection,
                selectionArgs);

        onDestroy();
    }

    protected void viewAllPalettes(){
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
                        "WHERE p.paletteID = c.pID ORDER BY p.paletteID DESC",
                null
        );

        //onDestroy();

        ArrayList<String> test3 = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                //Log.v("datadata", cursor.getString(cursor.getColumnIndex("c.pColor")));
                test3.add(cursor.getString(cursor.getColumnIndex("c.pColor")));
            }
        } finally {
            cursor.close();
        }

        // TODO: Put DB cursor in with the Adapter
        Palette p3 = new Palette("palette 3", test3);

        mPaletteList.add(p3);

        //setContentView(R.layout.activity_main);
    }

    protected void viewSinglePalette(){
        setContentView(R.layout.activity_palette_single_viewer);
    }

    protected void viewSettings(){
        setContentView(R.layout.activity_settings);
    }

    protected void saveSettings(){
        //save in database
    }

    protected void deletePalettes(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = "paletteID = ?";
        String[] selectionArgs = { "id" };
        db.delete("Palettes", selection, selectionArgs);
        onDestroy();
    }

    protected void exportPalettes(){

    }

    protected void recommendColor(){

    }

    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

}
