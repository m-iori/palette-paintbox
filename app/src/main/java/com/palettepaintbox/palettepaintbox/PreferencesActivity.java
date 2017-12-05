package com.palettepaintbox.palettepaintbox;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PreferencesActivity extends AppCompatActivity {

    private FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new FeedReaderDbHelper(this);

        String currentTheme = getCurrentTheme();
        if(currentTheme.equals("dark")){
            setTheme(R.style.darkTheme);
        }
        else{
            setTheme(R.style.lightTheme);
        }

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingstoolbar);
        setSupportActionBar(toolbar);
    }

    public void applySettings(View v){
        //RadioButton light = (RadioButton) (v.findViewById(R.id.lightTh));
        View main = (View)(v.getParent());
        RadioGroup theme = (RadioGroup)main.findViewById(R.id.themeChoice);
        String choice = ((RadioButton)findViewById(theme.getCheckedRadioButtonId())).getText().toString();
        if(choice.equals("Dark")){
            darkTheme();
        }
        else{
            lightTheme();
        }
        RadioGroup datas = main.findViewById(R.id.dataChoice);
        String datachoice = ((RadioButton)findViewById(datas.getCheckedRadioButtonId())).getText().toString();
        if(datachoice.equals("Show")){
            CheckBox hex = datas.findViewById(R.id.showHexcode);
            CheckBox rgb = datas.findViewById(R.id.showRGB);
            if(hex.isChecked()){
                setHex();
            }
            if(rgb.isChecked()){
                setRGB();
            }
        }
        else{
            setNoData();
        }
        Intent intent_view_all = new Intent(this, ViewAllActivity.class);
        this.startActivity(intent_view_all);
        finish();
    }

    protected void lightTheme(){
        makeUpdate("theme", "light", this);
        setTheme(R.style.lightTheme);
    }

    protected void darkTheme(){
        makeUpdate("theme", "dark", this);
        setTheme(R.style.darkTheme);
    }

    protected void setNoData(){
        makeUpdate("showHex", "false", this);
        makeUpdate("showRGB", "false", this);
    }

    protected void setHex(){
        makeUpdate("showHex", "true", this);
    }

    protected void setRGB(){
        makeUpdate("showRGB", "true", this);
    }

    protected void makeUpdate(String field, String val, Context context){
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = feedReaderDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(field, val);
        db.update("Preferences", values, null, null);
    }

    // Opens the credits
    public void viewCredits(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(PreferencesActivity.this).create();
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

    public void enableColorCodeSettings(View v){
        View hex = findViewById(R.id.showHexcode);
        View rgb = findViewById(R.id.showRGB);
        hex.setEnabled(true);
        rgb.setEnabled(true);
    }

    public void disableColorCodeSettings(View v){
        CheckBox hex = (CheckBox) findViewById(R.id.showHexcode);
        CheckBox rgb = (CheckBox) findViewById(R.id.showRGB);
        hex.setChecked(false);
        rgb.setChecked(false);
        hex.setEnabled(false);
        rgb.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_export:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected String getCurrentTheme(){
        // Set up database helper
        mDbHelper = new FeedReaderDbHelper(this);
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

    protected String getCurrentSettings(){
        // Set up database helper
        mDbHelper = new FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // SQL to get all palettes
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Preferences",
                null
        );

        try {
            while (cursor.moveToNext()) {
                String currentTheme = cursor.getString(cursor.getColumnIndex("theme"));
                String hex = cursor.getString(cursor.getColumnIndex("showHex"));
                String rgb = cursor.getString(cursor.getColumnIndex("showRGB"));
                if (currentTheme.equals("dark")) {
                    //RadioGroup theme = (RadioGroup)main.findViewById(R.id.themeChoice);
                    //theme.check(R.id.darkTh);
                }
                if(hex == "true"){
                    //RadioGroup datas = main.findViewById(R.id.dataChoice);
                    //datas.findViewById(R.id.showHexCode);
                }
            }
        } finally {
            cursor.close();
        }

        return "light";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar_preferences, menu);
        return true;
    }
}
