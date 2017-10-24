package com.palettepaintbox.palettepaintbox;

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
        this.paletteID = paletteID;
        this.name = name;
        this.colors = colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public void addColor(String color) {
        this.colors.add(color);
    }

    public void setColor(int position, String color) {
        this.colors.set(position, color);
    }

    public ArrayList<String> getColors() {
        //eventually pass in "format" string and add switch statement for hex, etc, if we need other formats
        return colors;
    }

    public String getName() {
        return name;
    }

    public int getPaletteID(){
        return paletteID;
    }
}
