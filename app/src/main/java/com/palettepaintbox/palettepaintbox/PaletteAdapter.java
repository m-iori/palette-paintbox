package com.palettepaintbox.palettepaintbox;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.PaletteHolder> {

    private ArrayList<Palette> mPalettes;

    public static class PaletteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Palette mPalette;
        private LinearLayout mLinearLayout;

        public PaletteHolder(View v) {
            super(v);
            mLinearLayout = v.findViewById(R.id.palette_row_linear_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }

        public void bindPalette(Palette palette) {
            mPalette = palette;
            for(String color : palette.getColors()){
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
                String hexColor = "#" + color;
                backgroundShape.mutate().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.MULTIPLY);
                Button colorButton = new Button(mLinearLayout.getContext());
                colorButton.setBackground(backgroundShape);
                colorButton.setLayoutParams(new LayoutParams(120,120));
                mLinearLayout.addView(colorButton);
            }
        }
    }

    public PaletteAdapter(ArrayList<Palette> palettes) {
        mPalettes = palettes;
    }

    @Override
    public PaletteAdapter.PaletteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.palette_row, parent, false);
        return new PaletteHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PaletteAdapter.PaletteHolder holder, int position) {
        Palette itemPalette = mPalettes.get(position);
        holder.bindPalette(itemPalette);
    }

    @Override
    public int getItemCount() {
        return mPalettes.size();
    }
}
