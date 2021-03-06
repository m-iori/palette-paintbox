package com.palettepaintbox.palettepaintbox;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

// The Palette Large view is a more descriptive view of rectangles which features the color data.
public class PaletteLargeAdapter extends RecyclerView.Adapter<PaletteLargeAdapter.PaletteHolder> {

    private ArrayList<Palette> mPalettes;

    public static class PaletteHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private Palette mPalette;
        private LinearLayout mLinearLayout;

        public PaletteHolder(View v) {
            super(v);
            mLinearLayout = v.findViewById(R.id.palette_large_list_layout);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d("RecyclerView", "CLICK!");
        }

        public void bindPalette(Palette palette) {
            mPalette = palette;
            for (String color : palette.getColors()) {
                Drawable backgroundShape = ContextCompat.getDrawable(mLinearLayout.getContext(), R.drawable.color_rectangle);
                String hexColor = "#" + color;
                backgroundShape.mutate().setColorFilter(Color.parseColor(hexColor), PorterDuff.Mode.MULTIPLY);
                Button colorButton = new Button(mLinearLayout.getContext());
                colorButton.setBackground(backgroundShape);
                colorButton.setText(getColorCodes(hexColor));

                // TODO: Find the correct bounds
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 130);
                layoutParams.setMargins(5, 5, 5, 5);
                colorButton.setLayoutParams(layoutParams);
                mLinearLayout.addView(colorButton);
            }
        }
    }

    public PaletteLargeAdapter(ArrayList<Palette> palettes) {
        mPalettes = palettes;
    }

    @Override
    public PaletteLargeAdapter.PaletteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.palette_large_list, parent, false);
        return new PaletteHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(PaletteLargeAdapter.PaletteHolder holder, int position) {
        Palette itemPalette = mPalettes.get(position);
        holder.bindPalette(itemPalette);
    }

    @Override
    public int getItemCount() {
        return mPalettes.size();
    }

    private static String getColorCodes(String hexCode) {
        String finalCode = hexCode;
        int red = Integer.valueOf(hexCode.substring(1, 3), 16);
        int green = Integer.valueOf(hexCode.substring(3, 5), 16);
        int blue = Integer.valueOf(hexCode.substring(5, 7), 16);
        String rgbCode = "rgb(" + red + "," + green + "," + blue + ")";
        if (ViewSingleActivity.colorCodeFormat.equals("hex")) {
            finalCode = hexCode;
        }
        if (ViewSingleActivity.colorCodeFormat.equals("rgb")) {
            finalCode = rgbCode;
        }
        if (ViewSingleActivity.colorCodeFormat.equals("both")) {
            finalCode = hexCode + "\n" + rgbCode;
        }
        if(ViewSingleActivity.colorCodeFormat.equals("none")) {
            finalCode = "            ";
        }
        return finalCode;
    }

}
