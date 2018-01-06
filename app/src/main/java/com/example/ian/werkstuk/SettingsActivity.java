package com.example.ian.werkstuk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


//https://www.logicchip.com/dynamic-action-bar-background-color/
public class SettingsActivity extends AppCompatActivity {

    SeekBar seekBarRed,seekBarGreen,seekBarBlue;
    RadioGroup idForRadioGroup;
    TextView textR,textG,textB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    int r;
    int g;
    int b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("key_clr", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        textR=(TextView)findViewById(R.id.textR);
        textG=(TextView)findViewById(R.id.textG);
        textB=(TextView)findViewById(R.id.textB);


        seekBarRed=(SeekBar)findViewById(R.id.seekBarRed);
        seekBarGreen=(SeekBar)findViewById(R.id.seekBarGreen);
        seekBarBlue=(SeekBar)findViewById(R.id.seekBarBlue);
        getClr();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textR.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarGreen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textG.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarBlue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textB.setText("= "+String.valueOf(progress));
                putClr();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    public void ActionBarClr(){
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.rgb(seekBarRed.getProgress()
                        ,seekBarGreen.getProgress(),seekBarBlue.getProgress())));
    }



    public void getClr(){

            r=sharedPreferences.getInt("a_r",0);
            g=sharedPreferences.getInt("a_g",0);
            b=sharedPreferences.getInt("a_b",0);

            seekBarRed.setProgress(r);
            seekBarGreen.setProgress(g);
            seekBarBlue.setProgress(b);

            textR.setText("= "+String.valueOf(r));
            textG.setText("= "+String.valueOf(g));
            textB.setText("= "+String.valueOf(b));
            ActionBarClr();

    }

    public void putClr(){

            editor.putInt("a_r",seekBarRed.getProgress());
            editor.putInt("a_g",seekBarGreen.getProgress());
            editor.putInt("a_b",seekBarBlue.getProgress());
            editor.commit();
            ActionBarClr();

    }
}
