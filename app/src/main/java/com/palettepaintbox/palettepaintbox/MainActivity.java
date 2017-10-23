package com.palettepaintbox.palettepaintbox;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import java.util.ArrayList;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    // This is the database reader.
    private FeedReaderDbHelper mDbHelper;

    // PaletteList contains the palette data.
    // The adapter feeds the data into the RecyclerView.
    // The RecyclerView displays the data.
    private ArrayList<Palette> mPaletteList;
    private PaletteAdapter mAdapter;
    private RecyclerView mRecyclerView;

    // Layout manager and item divider
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    // This is called when the app first opens.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up database helper
        mDbHelper = new FeedReaderDbHelper(this);

        // Set the main view
        setContentView(R.layout.activity_main);

        // Start the toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Set up RecyclerView
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
        Palette p1 = new Palette(1, "palette 1", testing1);

        ArrayList<String> testing2 = new ArrayList<>();
        testing2.add("FFFF00");
        Palette p2 = new Palette(2, "palette 2", testing2);

        mPaletteList.add(p1);
        mPaletteList.add(p2);

        /*TEST*/
        // Adds new palettes
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("paletteName", "TestPalette"); // get palette title from UI
        long newRowId = db.insert("Palettes", null, values);

        // TODO: Check if palette already has 6 colors before inserting!
        values = new ContentValues();
        values.put("pID", 3);
        values.put("pColor", "FF0000");
        newRowId = db.insert("PalettesToColors", null, values);

        values = new ContentValues();
        values.put("pID", 3);
        values.put("pColor", "00FF00");
        newRowId = db.insert("PalettesToColors", null, values);

        values = new ContentValues();
        values.put("pID", 3);
        values.put("pColor", "0000FF");
        newRowId = db.insert("PalettesToColors", null, values);

        // Get all the palettes from the data to the view
        viewAllPalettes();

        /*TEST*/

        //END MAKE UP PALETTES

        // Connect adapter to view
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

    // Saves a new palette
    protected void saveNewPalette(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("paletteTitle", "test"); // get palette title from UI

        long newRowId = db.insert("Palettes", null, values);

        onDestroy();
    }

    // Loads the palette editor
    protected void editExistingPalette(){
        setContentView(R.layout.palette_creator_and_editor);
    }

    // Does a DB update
    // TODO: Also save the color order, like color 1, color 2
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

    // Shows all palettes
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
                test3.add(cursor.getString(cursor.getColumnIndex("pColor")));
            }
        } finally {
            cursor.close();
        }

        // TODO: Put DB cursor in with the Adapter
        Palette p3 = new Palette(3, "palette 3", test3);

        mPaletteList.add(p3);

        //setContentView(R.layout.activity_main);
    }

    // Opens the palette creator
    protected void createNewPalette(View v) {
        setContentView(R.layout.palette_creator_and_editor);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.creator_and_editor);
        ColorPickerView colorPickerView= new ColorPickerView(coordinatorLayout.getContext());
        coordinatorLayout.addView(colorPickerView);
    }

    // Opens the settings
    protected void viewSettings(){
        setContentView(R.layout.activity_settings);
    }

    // Opens the credits
    protected void viewCredits(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Credits");
        alertDialog.setMessage("\u00A9 Rachel Hogue & Melissa Iori, 2017");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    // Saves the new settings
    protected void saveSettings(){
        //save in database
    }

    // Deletes existing palettes
    protected void deletePalettes(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = "paletteID = ?";
        String[] selectionArgs = { "id" };
        db.delete("Palettes", selection, selectionArgs);
        onDestroy();
    }

    // Later features
    /*
    protected void exportPalettes(){

    }

    protected void recommendColor(){

    }

    protected void getColorsFromImage(){

    }
    */

    // Closes the database connection when activity is destroyed
    protected void onDestroy() {
        mDbHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                viewSettings();
                return true;

            case R.id.action_export:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

}
