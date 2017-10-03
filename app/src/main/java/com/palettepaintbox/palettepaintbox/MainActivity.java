package com.palettepaintbox.palettepaintbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Palette> mPaletteList;
    private PaletteAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mPaletteList = new ArrayList<>();

        //MAKE UP PALETTES FOR TESTING - GET FROM DB LATER
        ArrayList<String> testing1 = new ArrayList<>();
        testing1.add("800000");
        testing1.add("000000");
        Palette p1 = new Palette("palette 1", testing1);

        ArrayList<String> testing2 = new ArrayList<>();
        testing2.add("FFFF00");
        Palette p2 = new Palette("palette 2", testing2);

        mPaletteList.add(p1);
        mPaletteList.add(p2);
        //END MAKE UP PALETTES

        mAdapter = new PaletteAdapter(mPaletteList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
