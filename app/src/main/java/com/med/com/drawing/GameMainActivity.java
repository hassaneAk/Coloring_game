package com.med.com.drawing;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class GameMainActivity extends AppCompatActivity {

    ViewGroup viewGroup;
    TextView textView;
    Intent playGame, setting;
    Button musicBtn;
    static MediaPlayer music;
    static boolean isMuted = false;
    static int insidePercent=100, outsidePercent=0;
    String lang;
    private static String id_kid;
    private static String id_accomp;

    public String getLang(){return lang; }
    public void setLang(String language){ lang = language; }

    public static int getInsidePercent(){return insidePercent;}
    public static void setInsidePercent(int percent){insidePercent = percent;}

    public static int getOutSidePercent(){return outsidePercent;}
    public static void setOutsidePercent(int percent){outsidePercent = percent;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        Intent a = getIntent();
        Toast.makeText(GameMainActivity.this , "kid_id :"+a.getStringExtra("kid_id"),Toast.LENGTH_SHORT).show();
        Toast.makeText(GameMainActivity.this , "id_accomp :"+a.getStringExtra("id_accomp"),Toast.LENGTH_SHORT).show();
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });

        if(!music.isPlaying() && (isMuted == false)) {
            music.start();
        }else if(isMuted == true){
            musicBtn.setBackgroundResource(R.drawable.music_off);
        }

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music.isPlaying()) {
                    musicBtn.setBackgroundResource(R.drawable.music_off);
                    music.pause();
                    isMuted = true;
                }else {
                    musicBtn.setBackgroundResource(R.drawable.music_on);
                    music.start();
                    isMuted = false;
                }
            }
        });
        music.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                music.start();
            }
        });
    }

    private void init() {
        textView = findViewById(R.id.textView);
        viewGroup = findViewById(R.id.activity_main);
        playGame = new Intent(this, DrawActivity.class);
        setting = new Intent(this, SettingActivity.class);
        musicBtn = findViewById(R.id.musicBtn);
        if(music==null){
            music = MediaPlayer.create(this, R.raw.music2);
        }
    }

    public void play(View view) {
        startActivity(playGame);
    }

    public void settings(View view) {
        startActivity(setting);
    }

    /* Fonctions for fullscreen mode. */
    private void setToFullScreen(){
        ViewGroup rootLayout = findViewById(R.id.activity_main);
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
