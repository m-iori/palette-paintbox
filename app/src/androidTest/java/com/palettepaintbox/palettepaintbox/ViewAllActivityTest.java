package com.palettepaintbox.palettepaintbox;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

import android.support.v7.widget.Toolbar;


@RunWith(AndroidJUnit4.class)
public class ViewAllActivityTest {
    @Rule
    public ActivityTestRule<ViewAllActivity> mActivityRule = new ActivityTestRule(ViewAllActivity.class);

    /**
     * Test that the title is correct.
     */
    @Test
    public void titleIsCorrect() {
        Toolbar mToolbar = mActivityRule.getActivity().findViewById(R.id.toolbar);
        assertEquals("Palette Paintbox", mToolbar.getTitle());
    }


}
