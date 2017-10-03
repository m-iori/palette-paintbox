package com.palettepaintbox.palettepaintbox;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.PaletteHolder> {

    private ArrayList<Palette> mPalettes;

    public static class PaletteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Button mColor1;
        private Button mColor2;
        private Button mColor3;
        private Button mColor4;
        private Palette mPalette;

        private static final String COLOR_KEY = "COLOR";

        public PaletteHolder(View v, ArrayList<String> colors) {
            super(v);
            mColor1 = v.findViewById(R.id.color_1);
            Drawable bgShape = mColor1.getBackground();
            String color = "#" + colors.get(0);
            bgShape.mutate().setColorFilter(Color.parseColor(color), PorterDuff.Mode.MULTIPLY);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }
    }

    public PaletteAdapter(ArrayList<Palette> palettes) {
        mPalettes = palettes;
    }

    @Override
    public PaletteAdapter.PaletteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.palette_row, parent, false);
        ArrayList<String> testing1 = new ArrayList<>();
        testing1.add("800000");
        return new PaletteHolder(inflatedView, testing1);
    }

    @Override
    public void onBindViewHolder(PaletteAdapter.PaletteHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mPalettes.size();
    }
}
