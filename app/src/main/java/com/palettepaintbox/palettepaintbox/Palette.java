package com.palettepaintbox.palettepaintbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/*
 * A Palette has a name and a set of colors, up to 6.
 */
public class Palette implements Serializable{

    private int paletteID;
    private String name;
    private ArrayList<String> colors;

    public Palette(int paletteID, String name, ArrayList<String> colors){
        //add validation that colors are valid hex code
        this.paletteID = paletteID;
        this.name = name;
        this.colors = colors;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public String getName() {
        return name;
    }

    public int getPaletteID(){
        return paletteID;
    }

    //STATIC FUNCTIONS TO MODIFY PALETTES IN THE DB

    public static boolean deletePalette(Context context, int paletteID) {
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = feedReaderDbHelper.getWritableDatabase();
        String selection = "pid = ?";
        String[] selectionArgs = { "" + paletteID };
        db.delete("PalettesToColors", selection, selectionArgs);
        selection = "paletteID = ?";
        String[] selectionArgs2 = { "" + paletteID };
        db.delete("Palettes", selection, selectionArgs2);
        feedReaderDbHelper.close();
        return true;
    }

    public static int createPalette(Context context, Palette palette) {
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = feedReaderDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Add the name
        values.put("paletteName", palette.getName());
        long pid = db.insert("Palettes", null, values);


        for(String color : palette.getColors()) {
            // TODO: Check if palette already has 6 colors before inserting!
            values = new ContentValues();
            values.put("pID", pid+0);
            values.put("pColor", color);
            db.insert("PalettesToColors", null, values);
        }

        feedReaderDbHelper.close();
        return (int)(pid);
    }

    public static Palette getPalette(Context context, int paletteID) {
        FeedReaderDbHelper feedReaderDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = feedReaderDbHelper.getWritableDatabase();

        Cursor cursor = null;
        String paletteName = "Palette not found.";
        try {
            cursor = db.rawQuery("SELECT paletteName FROM Palettes WHERE paletteID=?", new String[]{paletteID + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                paletteName = cursor.getString(cursor.getColumnIndex("paletteName"));
            }
        } finally {
            cursor.close();
        }

        cursor = null;
        ArrayList<String> paletteColors = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT pColor FROM PalettesToColors WHERE pID=?", new String[]{paletteID + ""});
            while (cursor.moveToNext()) {
                paletteColors.add(cursor.getString(cursor.getColumnIndex("pColor")));
            }
        } finally {
            cursor.close();
        }
        feedReaderDbHelper.close();

        return new Palette(paletteID, paletteName, paletteColors);
    }
}