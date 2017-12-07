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
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;

public class ViewAllActivity extends AppCompatActivity {

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

        // Set up database helper
        mDbHelper = new FeedReaderDbHelper(this);

        Cursor c = mDbHelper.getReadableDatabase().rawQuery("SELECT * FROM Preferences", null);
        if(c.getCount() == 0){
            ContentValues defaultPrefs = new ContentValues();
            defaultPrefs.put("theme", "light");
            defaultPrefs.put("showHex", "true");
            defaultPrefs.put("showRGB", "false");
            mDbHelper.getWritableDatabase().insert("Preferences", null, defaultPrefs);
        }

        super.onCreate(savedInstanceState);

        String currentTheme = getCurrentTheme();
        if(currentTheme.equals("dark")){
            setTheme(R.style.darkTheme);
        }
        else{
            setTheme(R.style.lightTheme);
        }

        Fabric.with(this, new Crashlytics());

        // Set the main view
        setContentView(R.layout.activity_main);

        // Start the toolbar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //If starting with a deletion, delete that palette
        Intent i = getIntent();
        final int pid = i.getIntExtra("deletion", -1);
        //Log.v("thepid", pid + "");
        if(pid > -1) {
            AlertDialog alertDialog = new AlertDialog.Builder(ViewAllActivity.this).create();
            alertDialog.setTitle("Palette Deletion");
            alertDialog.setMessage("Are you sure you want to delete this palette?");
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Palette.deletePalette(getApplicationContext(), pid);
                            Intent intent = new Intent(ViewAllActivity.this, ViewAllActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
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

        // Get all the palettes from the data to the view
        viewAllPalettes();

        //If no palettes, show message prompting user to create some
        if(mPaletteList.isEmpty()){
            mPaletteList.add(placeholderPalette);
        }

        // Connect adapter to view
        mAdapter = new PaletteAdapter(mPaletteList);
        mRecyclerView.setAdapter(mAdapter);
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
            SparseArray<String> paletteNames = new SparseArray<>();
            SparseArray<ArrayList<String>> paletteColors = new SparseArray<>();

            try {
                while (cursor.moveToNext()) {
                    Integer paletteID = cursor.getInt(cursor.getColumnIndex("paletteID"));
                    String paletteName = cursor.getString(cursor.getColumnIndex("paletteName"));
                    if(paletteNames.indexOfKey(paletteID) < 0){
                        paletteNames.put(paletteID, paletteName);
                    }
                    if(paletteColors.indexOfKey(paletteID) < 0){
                        ArrayList<String> pcolors = new ArrayList<>();
                        paletteColors.put(paletteID, pcolors);
                    }
                    paletteColors.get(paletteID).add(cursor.getString(cursor.getColumnIndex("pColor")));
                }
            } finally {
                cursor.close();
            }

            // TODO: Put DB cursor in with the Adapter
            for(int j = 0; j < paletteNames.size(); j++) {
                int id = paletteNames.keyAt(j);
                String pName = paletteNames.get(id);
                ArrayList<String> pColors = paletteColors.get(id);
                Palette nextPalette = new Palette(id, pName, pColors);
                mPaletteList.add(nextPalette);
            }
        }
    }

    // Opens the palette creator
    public void createNewPalette(View v) {
        Intent intent = new Intent(ViewAllActivity.this, ModifyPaletteActivity.class);
        startActivity(intent);
    }

    // Opens the settings
    protected void viewSettings(MenuItem item){
        Intent intent = new Intent(this, PreferencesActivity.class);
        this.startActivity(intent);
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
        RecyclerView recycler = this.findViewById(R.id.recyclerView);
        int palettes = recycler.getChildCount();
        for(int i = 0; i < palettes; i++){
            LinearLayout ll = (LinearLayout)(recycler.getChildAt(i));
            int elements = ll.getChildCount();
            for(int j = 0; j < elements; j++){
                View v = ll.getChildAt(j);
                if(v instanceof Button){
                    Button deleteButton = (Button)(v);
                    if(deleteButton.getVisibility() == View.GONE){
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    protected void endDeleteMode(){
        IN_DELETE_MODE = false;
        recreate();
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

    protected void export() {
        AlertDialog alertDialog = new AlertDialog.Builder(ViewAllActivity.this).create();
        alertDialog.setTitle("Coming Soon");
        alertDialog.setMessage("Exporting palettes coming soon!");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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
                export();
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
