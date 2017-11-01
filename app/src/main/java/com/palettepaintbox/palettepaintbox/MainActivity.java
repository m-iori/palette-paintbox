package com.palettepaintbox.palettepaintbox;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

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

    //Flag for delete mode
    private boolean IN_DELETE_MODE;

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

        //If starting with a deletion, delete that palette
        Intent i = getIntent();
        int pid = i.getIntExtra("deletion", -1);
        //Log.v("thepid", pid + "");
        if(pid > -1) {
            deletePalette(pid);
        }

        // Set up RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mPaletteList = new ArrayList<>();

        // Set up initial placeholder palette
        ArrayList<String> emptycolors = new ArrayList<>();
        Palette placeholderPalette = new Palette(-5, "Please tap the + button to create a palette.", emptycolors);

        //MADE UP PALETTES USED IN TESTING
        /*
        ArrayList<String> testing1 = new ArrayList<>();
        testing1.add("FF1155");
        testing1.add("05B291");
        testing1.add("F074AB");
        Palette p1 = new Palette(1, "Sample 1", testing1);

        ArrayList<String> testing2 = new ArrayList<>();
        testing2.add("FFFF00");
        testing2.add("FD00DD");
        testing2.add("F60403");
        Palette p2 = new Palette(2, "Sample 2", testing2);

        mPaletteList.add(p1);
        mPaletteList.add(p2);
        */
        /*TEST*/
        // Adds new palettes
        /*
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
        */
        /*TEST*/

        //END MAKE UP PALETTES

        // Get all the palettes from the data to the view
        viewAllPalettes();

        //If no palettes, show message prompting user to create some
        if(mPaletteList.isEmpty()){
            mPaletteList.add(placeholderPalette);
        }

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


    // Loads the palette editor - unused, changed to its own activity
    protected void editExistingPalette(){
        setContentView(R.layout.palette_creator_and_editor);
    }

    // Does a DB update to existing palette
    // TODO: Also save the color order, like color 1, color 2
    protected void saveExistingPalette(int paletteID){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put("paletteTitle", ""); //get the new title from the UI
        //values.put("", ""); //get the new colors

        // Where paletteID is the palette being updated, perform the update
        String selection = "paletteID" + " = ?";
        String[] selectionArgs = { "" + paletteID };

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

        // SQL to get all palettes
        Cursor cursor = db.rawQuery(
                "SELECT p.paletteID, p.paletteName, c.pColor FROM Palettes p, PalettesToColors c " +
                        "WHERE p.paletteID = c.pID ORDER BY p.paletteID DESC",
                null
        );

        // Match up colors with palettes
        for(int i = 0; i < cursor.getCount(); i++) {
            HashMap<Integer,String> paletteNames = new HashMap<Integer,String>();
            HashMap<Integer,ArrayList<String>> paletteColors = new HashMap<Integer,ArrayList<String>>();

            try {
                while (cursor.moveToNext()) {
                    Integer paletteID = cursor.getInt(cursor.getColumnIndex("paletteID"));
                    String paletteName = cursor.getString(cursor.getColumnIndex("paletteName"));
                    if(!(paletteNames.containsKey(paletteID))){
                        paletteNames.put(paletteID, paletteName);
                    }
                    if(!(paletteColors.containsKey(paletteID))){
                        ArrayList<String> pcolors = new ArrayList<String>();
                        paletteColors.put(paletteID, pcolors);
                    }
                    paletteColors.get(paletteID).add(cursor.getString(cursor.getColumnIndex("pColor")));
                }
            } finally {
                cursor.close();
            }

            // TODO: Put DB cursor in with the Adapter
            for(int id : paletteNames.keySet()){
                String pName = paletteNames.get(id);
                ArrayList<String> pColors = paletteColors.get(id);
                Palette nextPalette = new Palette(id, pName, pColors);
                mPaletteList.add(nextPalette);
            }
        }
    }

    // Opens the palette creator
    protected void createNewPalette(View v) {
        Intent intent = new Intent(MainActivity.this, ModifyPaletteActivity.class);
        startActivity(intent);
    }

    // Opens the settings
    protected void viewSettings(MenuItem item){
        Intent intent = new Intent(this, PreferencesActivity.class);
        this.startActivity(intent);
    }

    // Saves the new settings
    protected void saveSettings(){
        //save in database
    }

    // Deletes existing palettes
    protected void deletePalette(int paletteID){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String selection = "paletteID = ?";
        String[] selectionArgs = { "" + paletteID };
        db.delete("PalettesToColors", selection, selectionArgs);
        selection = "paletteID = ?";
        String[] selectionArgs2 = { "" + paletteID };
        db.delete("Palettes", selection, selectionArgs2);
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

    protected void startDeleteMode(){
        IN_DELETE_MODE = true;
        //View deleteButton = this.findViewById(R.id.palette_row_linear_layout_root).findViewById(R.id.deleteButton);
        //deleteButton.setVisibility(View.VISIBLE);
    }

    protected void endDeleteMode(){
        IN_DELETE_MODE = false;
        recreate();
    }

    // Sets up the menu bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                viewSettings(item);
                return true;

            case R.id.action_delete:
                if(IN_DELETE_MODE) {
                    endDeleteMode();
                    return true;
                }
                else{
                    startDeleteMode();
                    return true;
                }

            case R.id.action_export:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

}
