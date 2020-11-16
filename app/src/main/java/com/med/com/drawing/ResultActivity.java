package com.med.com.drawing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.med.com.drawing.BddUtils.Game;
import com.med.com.drawing.BddUtils.GamesBDD;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.med.com.drawing.DrawActivity.getForm;
import static com.med.com.drawing.GameMainActivity.getInsidePercent;
import static com.med.com.drawing.GameMainActivity.getOutSidePercent;
import static com.med.com.drawing.Utils.PaintPotView.getColoredPoints;
import static com.med.com.drawing.Utils.PaintPotView.getNbrPoints;
import static com.med.com.drawing.Utils.PaintPotView.getNbrPointsOutside;
import static com.med.com.drawing.Utils.PaintPotView.getNbrTentative;

public class ResultActivity extends AppCompatActivity {

    ViewGroup viewGroup;
    Intent homepage;
    TextView textView;
    int form;
    Game game;
    String id_application = "2018_1_5_2",//check
            id_exercice = "T_5_9",//check
            id_niveau,//check
            date_actuelle,//check
            heure_debut,//check
            heure_fin,//check
            nbr_op_reussi,//check
            nbr_op_echoue,//check
            min_temps_op_sec,//check
            moy_temps_op_sec,//check
            id_apprenant,
            id_accompagnant,
            longitude,
            latitude,
            device,
            flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        init();
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToFullScreen();
            }
        });
        showResult();
        if(form!=0){
            // Création d'une variable Game
            game = new Game(id_application, id_exercice, id_niveau, date_actuelle, heure_debut, heure_fin, nbr_op_reussi, nbr_op_echoue, min_temps_op_sec, moy_temps_op_sec);
            //Insetion de la variable game
            insertGame(game);
        }
    }

    private void insertGame(Game game) {
        //Insertion dans la base de donnée de la variable Game.
        GamesBDD gamesBDD = new GamesBDD(this);

        //On ouvre la base de données pour écrire dedans
        gamesBDD.open();

        //On insère la variable Game que l'on a crée
        Long id = gamesBDD.insertGameInfo(game);
        if( id ==-1){
            Toast.makeText(this,"Insertion échoué.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Insertion réussi, id = "+id, Toast.LENGTH_SHORT).show();
        }

        //On ferme notre bdd
        gamesBDD.close();
    }

    private void init(){
        viewGroup = findViewById(R.id.activity_result);
        homepage = new Intent(this, GameMainActivity.class);
        textView = findViewById(R.id.textView_result);

        form = getForm();
        if(form != 0) {
            id_niveau = String.valueOf(form);
            DateFormat df = new SimpleDateFormat("MM/dd/yy");
            date_actuelle = df.format(new Date());
            heure_debut = DrawActivity.getStartTime();
            heure_fin = DrawActivity.getEndTime();
            min_temps_op_sec = String.valueOf(DrawActivity.getTime());
            moy_temps_op_sec = String.valueOf(DrawActivity.getTime());
        }
    }

    private int randInt(int min, int max){
        int range = (max-min)+1;
        return (int)(Math.random() * range) + min;
    }

    private void setMessage(int result){//1 for win & 0 for lose

        int i;
        MediaPlayer winAudio[] = new MediaPlayer[3];
        MediaPlayer loseAudio[] = new MediaPlayer[3];

        String lang = Locale.getDefault().getDisplayLanguage();
        if(lang.equals("français")) {
            winAudio[0] = MediaPlayer.create(this, R.raw.incroyable_fr);
            winAudio[1] = MediaPlayer.create(this, R.raw.brillant_fr);
            winAudio[2] = MediaPlayer.create(this, R.raw.excellent_fr);

            loseAudio[0] = MediaPlayer.create(this, R.raw.resources_fr);
            loseAudio[1] = MediaPlayer.create(this, R.raw.time_fr);
            loseAudio[2] = MediaPlayer.create(this, R.raw.timepractice_fr);

        }else if(lang.equals("English")){
            winAudio[0] = MediaPlayer.create(this, R.raw.amazing_en);
            winAudio[1] = MediaPlayer.create(this, R.raw.brillant_en);
            winAudio[2] = MediaPlayer.create(this, R.raw.excellent_en);

            loseAudio[0] = MediaPlayer.create(this, R.raw.resources_en);
            loseAudio[1] = MediaPlayer.create(this, R.raw.time_en);
            loseAudio[2] = MediaPlayer.create(this, R.raw.timepractice_en);
        }

        String winExp[] = {getResources().getString(R.string.winExp1),
                getResources().getString(R.string.winExp2),
                getResources().getString(R.string.winExp3)};

        String loseExp[] = {getResources().getString(R.string.loseExp1),
                getResources().getString(R.string.loseExp2),
                getResources().getString(R.string.loseExp3)};

        switch (result){
            case 0:
                i = randInt(0, loseExp.length-1);
                textView.setText(loseExp[i]);
                loseAudio[i].start();
                break;
            case 1:
                i = randInt(0, winExp.length-1);
                textView.setText(winExp[i]);
                winAudio[i].start();
                break;
        }

    }

    private void showResult(){

        //Getting the percents
        int outside_percent = getOutSidePercent();
        int inside_percent = getInsidePercent();

        //Getting the nbr of points colored & the nbr of points inside & Outside
        int nbrPoints = getNbrPoints();
        int coloredPoints = getColoredPoints();

        //switch form
        switch (form){
            case 1://rectangle
                //calculating the percent of nbrPoints inside.
                int maxRectangle = nbrPoints*inside_percent/100;

                // <=80 : not enough, >=80 et <=90 : good, >=90 : awsome. nbrTentative != 0 : not enough, nbrTentative=0; ok
                if(getNbrTentative()<=outside_percent) {
                    if (coloredPoints >= maxRectangle) {
                        setMessage(1);
                        nbr_op_reussi = "1";
                        nbr_op_echoue = "0";
                    }else{
                        setMessage(0);
                        nbr_op_reussi = "0";
                        nbr_op_echoue = "1";
                    }
                }else{
                    Toast.makeText(this, "Tu a dépassé les limites. Try again.", Toast.LENGTH_SHORT).show();
                    setMessage(0);
                    nbr_op_reussi = "0";
                    nbr_op_echoue = "1";
                }
                break;

            case 2 :// we have 25 points inside triangle

                //calculating the percent of nbrPoints inside&outside.
                int maxTriangle = nbrPoints*inside_percent/100;
                int maxOutsideTriangle = getNbrPointsOutside()*outside_percent/100;

                //test
                if(getNbrTentative() <= maxOutsideTriangle) {
                    if (coloredPoints >= maxTriangle) {
                        setMessage(1);
                        nbr_op_reussi = "1";
                        nbr_op_echoue = "0";
                    }else{
                        setMessage(0);
                        nbr_op_reussi = "0";
                        nbr_op_echoue = "1";
                    }
                }else{
                    setMessage(0);
                    nbr_op_reussi = "0";
                    nbr_op_echoue = "1";
                }
                break;

            case 3 :

                //calculating the percent of nbrPoints inside&outside
                int maxCircle = nbrPoints*inside_percent/100;
                int maxOutsideCircle = getNbrPointsOutside()*outside_percent/100;

                if(getNbrTentative() <= outside_percent) {
                    if (coloredPoints >= maxCircle) {
                        setMessage(1);
                        nbr_op_reussi = "1";
                        nbr_op_echoue = "0";
                    }else{
                        setMessage(0);
                        nbr_op_reussi = "0";
                        nbr_op_echoue = "1";
                    }
                }else{
                    setMessage(0);
                    nbr_op_reussi = "0";
                    nbr_op_echoue = "1";
                }
                break;
        }
    }

    private void setToFullScreen(){
        ViewGroup rootLayout = findViewById(R.id.activity_result);
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

    public void homePage(View view) {
        finish();
        startActivity(homepage);
    }
}
