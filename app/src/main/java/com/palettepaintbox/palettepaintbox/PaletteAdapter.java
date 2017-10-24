package com.palettepaintbox.palettepaintbox;

import android.content.Intent;
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
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.PaletteHolder> {

    private ArrayList<Palette> mPalettes;

    public static class PaletteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Palette mPalette;
        private LinearLayout mLinearLayout;
        private TextView mName;

        public PaletteHolder(View v) {
            super(v);
            mLinearLayout = v.findViewById(R.id.palette_row_linear_layout);
            mLinearLayout.setOnClickListener(goToViewer);
            mName = v.findViewById(R.id.palette_row_name_text_view);
            //v.setOnClickListener(goToViewer); //this
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }

        public void bindPalette(Palette palette) {
            mPalette = palette;
            mName.setText(palette.getName());
            for(String color : palette.getColors()){
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(),R.drawable.color_circle);
                String hexColor = "#" + color;
                backgroundShape.mutate().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.MULTIPLY);
                Button colorButton = new Button(mLinearLayout.getContext());
                colorButton.setBackground(backgroundShape);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120,120);
                layoutParams.setMargins(5,5,5,5);
                colorButton.setLayoutParams(layoutParams);
                colorButton.setOnClickListener(goToViewer);
                mLinearLayout.addView(colorButton);
            }
            mLinearLayout.setId(palette.getPaletteID());
        }
    }

    // Palette onclick
    // Goes to the Single View for a palette
    public static OnClickListener goToViewer = new OnClickListener(){
        public void onClick(View v){
            Intent intent = new Intent(v.getContext(), ViewSingleActivity.class);
            intent.putExtra("paletteID", v.getId());
            v.getContext().startActivity(intent);
        }
    };


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
