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

    private ColorDrawable color;
    SeekBar seekBarRed,seekBarGreen,seekBarBlue;
    RadioGroup idForRadioGroup;
    TextView textR,textG,textB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean bar=true;

    int r;
    int g;
    int b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        idForRadioGroup=(RadioGroup)findViewById(R.id.idForRadioGroup);
        RadioButton rButton = (RadioButton) idForRadioGroup.getChildAt(0);
        rButton.setChecked(true);

        RadioButton rButton2 = (RadioButton) idForRadioGroup.getChildAt(1);
        rButton2.setVisibility(View.GONE);

        sharedPreferences = getSharedPreferences("key_clr", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();

        textR=(TextView)findViewById(R.id.textR);
        textG=(TextView)findViewById(R.id.textG);
        textB=(TextView)findViewById(R.id.textB);


        seekBarRed=(SeekBar)findViewById(R.id.seekBarRed);
        seekBarGreen=(SeekBar)findViewById(R.id.seekBarGreen);
        seekBarBlue=(SeekBar)findViewById(R.id.seekBarBlue);
        getClr();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rButton2.setVisibility(View.VISIBLE);
            initStatusBarClr();
        }

        idForRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radio0:
                        // do operations specific to this selection
                        bar=true;
                        getClr();
                        break;
                    case R.id.radio1:
                        // do operations specific to this selection
                        bar=false;
                        getClr();
                        break;
                }
            }
        });


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
        color = new ColorDrawable(Color.rgb(seekBarRed.getProgress()
                ,seekBarGreen.getProgress(),seekBarBlue.getProgress()));
    }

    public void StatusBarClr(){
        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //    window.setStatusBarColor(Color.BLUE);

            window.setStatusBarColor(Color.rgb(seekBarRed.getProgress()
                    ,seekBarGreen.getProgress(),seekBarBlue.getProgress()));
        }
    }


    public void initStatusBarClr(){

        r=sharedPreferences.getInt("s_r",0);
        g=sharedPreferences.getInt("s_g",0);
        b=sharedPreferences.getInt("s_b",0);

        Window window = this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//    window.setStatusBarColor(Color.BLUE);
            window.setStatusBarColor(Color.rgb(r,g,b));
        }


    }

    public void getClr(){
        if(bar) {
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
        }else{
            r=sharedPreferences.getInt("s_r",0);
            g=sharedPreferences.getInt("s_g",0);
            b=sharedPreferences.getInt("s_b",0);

            seekBarRed.setProgress(r);
            seekBarGreen.setProgress(g);
            seekBarBlue.setProgress(b);

            textR.setText("= "+String.valueOf(r));
            textG.setText("= "+String.valueOf(g));
            textB.setText("= "+String.valueOf(b));
            StatusBarClr();
        }
    }

    public void putClr(){
        if (bar){
            editor.putInt("a_r",seekBarRed.getProgress());
            editor.putInt("a_g",seekBarGreen.getProgress());
            editor.putInt("a_b",seekBarBlue.getProgress());
            editor.commit();
            ActionBarClr();
        }else{
            editor.putInt("s_r",seekBarRed.getProgress());
            editor.putInt("s_g",seekBarGreen.getProgress());
            editor.putInt("s_b",seekBarBlue.getProgress());
            editor.commit();
            StatusBarClr();
        }
    }
    /*@SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void btnSave(View v){

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.rgb(seekBarRed.getProgress()
                        ,seekBarGreen.getProgress(),seekBarBlue.getProgress())));
        //Window window = getApplicationContext().get
    }*/

    public ColorDrawable getColor() {
        return color;
    }

    public void setColor(ColorDrawable color) {
        this.color = color;
    }
    public void btnSave(View v){
        /*Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName() );

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);*/

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        String results = "" + r + g + b;
        intent.putExtra("colors",results);

        intent.putExtra("red",sharedPreferences.getInt("a_r",0));
        intent.putExtra("green",sharedPreferences.getInt("a_g",0));
        intent.putExtra("blue",sharedPreferences.getInt("a_b",0));
        startActivity(intent);
    }
}
