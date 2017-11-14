package com.palettepaintbox.palettepaintbox;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PaletteInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.palettepaintbox.palettepaintbox", appContext.getPackageName());
    }

    @Test
    public void paletteDoesNotExist() throws Exception {
        Palette getPalette = Palette.getPalette(InstrumentationRegistry.getTargetContext(), 24);
        Assert.assertEquals("Palette not found.", getPalette.getName());
    }

    @Test
    public void insertPalette() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ArrayList<String> colors = new ArrayList<>();
        colors.add("FFFFFF");
        int newPaletteId = Palette.createPalette(appContext, new Palette(-1,"Test Palette", colors));
        Palette getPalette = Palette.getPalette(appContext, newPaletteId);
        assertEquals(newPaletteId, getPalette.getPaletteID());
        assertEquals("Test Palette", getPalette.getName());
        assertEquals("FFFFFF", getPalette.getColors().get(0));
    }

    @Test
    public void deletePalette() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ArrayList<String> colors = new ArrayList<>();
        colors.add("FFFFFF");
        int newPaletteId = Palette.createPalette(appContext, new Palette(-1,"Test Palette", colors));
        Palette.deletePalette(appContext, newPaletteId);
        Palette getPalette = Palette.getPalette(appContext, newPaletteId);
        Assert.assertEquals("Palette not found.", getPalette.getName());
    }

    @Test
    public void updatePalette() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        ArrayList<String> colors = new ArrayList<>();
        colors.add("FFFFFF");
        int newPaletteId = Palette.createPalette(appContext, new Palette(-1,"Test Palette", colors));
        ArrayList<String> updateColors = new ArrayList<>();
        updateColors.add("AAAAAA");
        Palette.updatePalette(appContext, new Palette(newPaletteId, "Update Palette Name", updateColors));
        Palette getPalette = Palette.getPalette(appContext, newPaletteId);
        assertEquals(newPaletteId, getPalette.getPaletteID());
        assertEquals("Update Palette Name", getPalette.getName());
        assertEquals("AAAAAA", getPalette.getColors().get(0));
    }
}
