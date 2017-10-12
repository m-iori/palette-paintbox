package com.palettepaintbox.palettepaintbox;

import java.io.Serializable;
import java.util.ArrayList;

public class Palette implements Serializable{

    private String name;
    private ArrayList<String> colors;

    public Palette(String name, ArrayList<String> colors){
        this.name = name;
        this.colors = colors;
    }

    public ArrayList<String> getColors() { //eventually pass in "format" string and add switch statement for hex, etc, if we need other formats
        return colors;
    }

    public String getName() {
        return name;
    }
}
