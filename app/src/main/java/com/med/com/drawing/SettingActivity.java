package com.med.com.drawing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import static com.med.com.drawing.GameMainActivity.setInsidePercent;
import static com.med.com.drawing.GameMainActivity.setOutsidePercent;

public class SettingActivity extends AppCompatActivity {

    ViewGroup viewGroup;
    Spinner perspinner, tenspinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });
    }

    private void init(){
        viewGroup = findViewById(R.id.activity_setting);
        perspinner = findViewById(R.id.percentSpinner);
        tenspinner = findViewById(R.id.tentativeSpinner);

        String[] items = {"100%", "90%", "80%", "70%", "60%", "50%", "40%", "30%", "20%", "10%", "5%", "0%"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);

        tenspinner.setAdapter(adapter);
        perspinner.setAdapter(adapter);

        perspinner.setSelection(0);
        tenspinner.setSelection(items.length-1);
    }

    public void save(View view) {

        switch (perspinner.getSelectedItemPosition()){
            case 0:
                setInsidePercent(100);
                break;
            case 1:
                setInsidePercent(90);
                break;
            case 2:
                setInsidePercent(80);
                break;
            case 3:
                setInsidePercent(70);
                break;
            case 4:
                setInsidePercent(60);
                break;
            case 5:
                setInsidePercent(50);
                break;
            case 6:
                setInsidePercent(40);
                break;
            case 7:
                setInsidePercent(30);
                break;
            case 8:
                setInsidePercent(20);
                break;
            case 9:
                setInsidePercent(10);
                break;
            case 10:
                setInsidePercent(5);
                break;
            case 11:
                setInsidePercent(0);
                break;
        }

        switch (tenspinner.getSelectedItemPosition()){
            case 0:
                setOutsidePercent(100);
                break;
            case 1:
                setOutsidePercent(90);
                break;
            case 2:
                setOutsidePercent(80);
                break;
            case 3:
                setOutsidePercent(70);
                break;
            case 4:
                setOutsidePercent(60);
                break;
            case 5:
                setOutsidePercent(50);
                break;
            case 6:
                setOutsidePercent(40);
                break;
            case 7:
                setOutsidePercent(30);
                break;
            case 8:
                setOutsidePercent(20);
                break;
            case 9:
                setOutsidePercent(10);
                break;
            case 10:
                setOutsidePercent(5);
                break;
            case 11:
                setOutsidePercent(0);
                break;
        }

        cancel(view);
    }

    public void cancel(View view) {
        Intent setting = new Intent(this, GameMainActivity.class);
        finish();
        startActivity(setting);
    }
    /* Fonctions for fullscreen mode. */
    private void setToFullScreen(){
        ViewGroup rootLayout = findViewById(R.id.activity_setting);
        rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }
}
