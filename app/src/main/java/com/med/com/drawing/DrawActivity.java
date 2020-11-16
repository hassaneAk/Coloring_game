package com.med.com.drawing;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.med.com.drawing.Utils.PaintPotView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DrawActivity extends AppCompatActivity {

    private ViewGroup contentView;
    private Button dotSizePlus, dotSizeMinus, reset, squarebtn, trianglebtn, circlebtn, eraserbtn;
    private PaintPotView drawingPad;
    private Intent result, homePage;
    private AlertDialog.Builder alertDialog;
    private String message, yes, no;
    private int button; //0 for cancel, 1 for validate
    private Chronometer chronometer;
    private Long timeWhenStopped;

    private static final int DOT_SIZE_INCREMENT = 10;

    private static int time=0;
    private static int form=0;//1 for square, 2 for triangle, 3 for circle.
    private static String startTime;
    private static String endTime;

    public static int getTime(){return time;}
    public static String getStartTime() {
        return startTime;
    }
    public static String getEndTime() {
        return endTime;
    }
    public static int getForm(){return form;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        init();

        squarebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting the form
                form = 1;
                //start time :
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                startTime = sdf.format(new Date());
                //Starting the chronometer
                chronometer.setBase(SystemClock.elapsedRealtime() );
                chronometer.start();
                //Setting up the padding
                int padding = 100;
                //resetting the drawingpad
                drawingPad.reset();
                //Draw a rectangle
                drawingPad.drawRectangle(padding, padding, drawingPad.getWidth()-padding, drawingPad.getHeight()-padding);
            }
        });
        trianglebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting the form
                form = 2;
                //start time :
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                startTime = sdf.format(new Date());
                //Starting the chrono
                chronometer.setBase(SystemClock.elapsedRealtime() );
                chronometer.start();
                //Reseting the drawingpad
                drawingPad.reset();
                //Draw a triangle
                drawingPad.drawTriangle(drawingPad.getWidth()*50/100, drawingPad.getHeight()*50/100, drawingPad.getWidth()*90/100);
            }
        });
        circlebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Setting the form
                form = 3;
                //start time :
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                startTime = sdf.format(new Date());
                //Starting the chrono
                chronometer.setBase(SystemClock.elapsedRealtime() );
                chronometer.start();
                //resetting the drawingpad
                drawingPad.reset();
                //draw circle
                drawingPad.drawCircle(drawingPad.getWidth()*50/100, drawingPad.getHeight()*50/100, drawingPad.getWidth()*40/100);
            }
        });

        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToFullScreen();
            }
        });
        eraserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingPad.setPenColor(-1);
            }
        });
        /* Decrement dotSize  */
        dotSizeMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingPad.changeDotSize(-DOT_SIZE_INCREMENT);
            }
        });
        /* Increment dotSize */
        dotSizePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingPad.changeDotSize(+DOT_SIZE_INCREMENT);
            }
        });
        /* Reset DotSize */
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Resetting the drawingpad
                drawingPad.reset();
                //Resetting the chronometer
                chronometer.setBase(SystemClock.elapsedRealtime() );
                chronometer.start();
            }
        });
    }

    /* Function that initialize everything */
    private void init(){

        result = new Intent(this, ResultActivity.class);
        homePage = new Intent(this, GameMainActivity.class);

        contentView = findViewById(R.id.activity_draw);
        drawingPad = findViewById(R.id.drawingPad);
        reset = findViewById(R.id.reset);
        dotSizePlus = findViewById(R.id.plus);
        dotSizeMinus = findViewById(R.id.minus);
        squarebtn = findViewById(R.id.squarebtn);
        trianglebtn = findViewById(R.id.trianglebtn);
        circlebtn = findViewById(R.id.circlebtn);
        eraserbtn = findViewById(R.id.eraser);
        chronometer = findViewById(R.id.chronometer);

        form =0;


        //alerte
        alertDialog = new AlertDialog.Builder(this);
        message = getResources().getString(R.string.existMessage);
        yes = getResources().getString(R.string.yes);
        no = getResources().getString(R.string.no);
        alertDialog.setMessage(message)
                .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(button==1) {//si il clique sur validate
                            if (form == 0) {//Si aucune forme n'a été choisi, back to main menu
                                finish();
                                startActivity(homePage);
                            } else {
                                //end time :
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                endTime = sdf.format(new Date());
                                //Getting the drawn points
                                drawingPad.getDrawnPoints();
                                //and go to the result activity.
                                startActivity(result);
                            }
                        }else{//Si il clique sur Cancel
                            startActivity(homePage);
                        }
                    }
                })
                .setNegativeButton(no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Resuming the chronometer to the stopped value
                        chronometer.setBase(SystemClock.elapsedRealtime() + time);
                        chronometer.start();
                        //Dismissing the dialog interface
                        dialogInterface.dismiss();
                        //Setting the full screen mode
                        setToFullScreen();
                    }
                })
                .create();
    }

    /* Fonction that allows to choose the color to draw with */
    public void chooseColor(View view) {
        final Context context = view.getContext();
        ColorPickerDialogBuilder
                .with(context)
                .setTitle(R.string.app_name)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .setOnColorChangedListener(new OnColorChangedListener() {
                    @Override
                    public void onColorChanged(int selectedColor) {
                        // Handle on color change
                        Log.d("ColorPicker", "onColorChanged: 0x" + Integer.toHexString(selectedColor));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        drawingPad.setPenColor(selectedColor);
                        if (allColors != null) {
                            StringBuilder sb = null;

                            for (Integer color : allColors) {
                                if (color == null)
                                    continue;
                                if (sb == null)
                                    sb = new StringBuilder("Color List:");
                                sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                            }

                            if (sb != null)
                                Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                        }
                        setToFullScreen();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .showColorEdit(true)
                .setColorEditTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright))
                .build()
                .show();
    }


    private void setToFullScreen(){
        ViewGroup rootLayout = findViewById(R.id.activity_draw);
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

    public void validate(View view) {
        //Stoping the chronometer
        chronometer.stop();
        time = (int) (SystemClock.elapsedRealtime() - chronometer.getBase())/1000;
        //Setting up the value of the button pressed
        button = 1;
        //Showing the alert
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void cancel(View view) {
        //Stoping the chronometer
        chronometer.stop();
        time = (int) (SystemClock.elapsedRealtime() - chronometer.getBase())/1000;
        //Setting up the value of the button pressed
        button = 0;
        //Showing the alert
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}
