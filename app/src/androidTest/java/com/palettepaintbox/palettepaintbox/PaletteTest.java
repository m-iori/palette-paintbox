package com.palettepaintbox.palettepaintbox;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PaletteTest {
    @Test
    public void createPalette() throws Exception {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("FFFFFF");
        Palette testPalette = new Palette(24, "Test Palette", colors);
        assertEquals("Test Palette", testPalette.getName());
        assertEquals(colors, testPalette.getColors());
        assertEquals(24, testPalette.getPaletteID());
    }
}
