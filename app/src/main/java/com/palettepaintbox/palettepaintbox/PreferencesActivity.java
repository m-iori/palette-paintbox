package com.palettepaintbox.palettepaintbox;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class PreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.settingstoolbar);
        setSupportActionBar(toolbar);
    }

    protected void applySettings(View v){
        //RadioButton light = (RadioButton) (v.findViewById(R.id.lightTh));
        RadioButton dark = v.findViewById(R.id.darkTh);
        if(dark.isSelected()){
            darkTheme();
        }
        else{
            lightTheme();
        }
    }

    protected void lightTheme(){
        View mainView = this.findViewById(R.id.settingsLayout);
        mainView.setBackgroundColor(getColor(R.color.white));
    }

    protected void darkTheme(){
        View mainView = this.findViewById(R.id.settingsLayout
        );
        mainView.setBackgroundColor(getColor(R.color.black));
    }

    // Opens the credits
    public void viewCredits(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(PreferencesActivity.this).create();
        alertDialog.setTitle("Credits");
        alertDialog.setMessage("\u00A9 Rachel Hogue & Melissa Iori, 2017");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void enableColorCodeSettings(View v){
        View hex = findViewById(R.id.showHexcode);
        View rgb = findViewById(R.id.showRGB);
        hex.setEnabled(true);
        rgb.setEnabled(true);
    }

    public void disableColorCodeSettings(View v){
        CheckBox hex = (CheckBox) findViewById(R.id.showHexcode);
        CheckBox rgb = (CheckBox) findViewById(R.id.showRGB);
        hex.setChecked(false);
        rgb.setChecked(false);
        hex.setEnabled(false);
        rgb.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_export:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
}
