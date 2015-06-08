package com.example.stylo.bwyath;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.app.Activity;
import android.view.View;
import android.view.MotionEvent;

import AccessibilityService.GestureListener;
import AccessibilityService.GestureService;
import AccessibilityService.TTSService;
import AccessibilityService.VibratorService;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener, GestureListener {

    // Bouton "Nouvelle partie"
    private ImageButton btn_new_game;
    // Bouton "Options"
    private ImageButton btn_options;
    // Bouton "Quitter"
    private ImageButton btn_leave;

    private static View theView;

    public static GestureService recognizer;

    private TextToSpeech tts;

    private VibratorService vibrator;

    public static String lateralite;

    public static String language;

    public static MainActivity self;

    private enum Method {
        NEWGAME,
        OPTIONS,
        LEAVE
    }

    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lateralite = "droitier";
        language = "fr";

        recognizer = new GestureService((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        if(lateralite.equals("droitier")){
            recognizer.setDroitier(true);
        }
        else{
            recognizer.setDroitier(false);
        }
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        tts = TTSService.getTTS(this);
        vibrator = new VibratorService(this);

        btn_new_game = (ImageButton) findViewById(R.id.btn_new_game);
        btn_new_game.setOnClickListener(this);
        btn_options = (ImageButton) findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
        btn_leave = (ImageButton) findViewById(R.id.btn_leave);
        btn_leave.setOnClickListener(this);
        theView = (View) btn_new_game.getParent();
        vibrator = new VibratorService(this);

        self = this;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    public void newGame(){
        TTSService.Stop();
        this.announceText("Êtes vous sûr de vouloir commencer une nouvelle partie ?", true);
        this.method = Method.NEWGAME;
        this.addValidation();
    }

    public void options(){
        TTSService.Stop();
        this.announceText("Êtes vous sûr de vouloir rentrer dans les options ?", true);
        this.method = Method.OPTIONS;
        this.addValidation();
    }

    public void leave(){
        TTSService.Stop();
        this.announceText("Êtes vous sûr de vouloir quitter l'application ?", true);
        this.method = Method.LEAVE;
        this.addValidation();
    }

    public void goNewGame(){
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    public void goOptions(){
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        Intent intent = new Intent(MainActivity.this, OptionActivity.class);
        startActivity(intent);
    }

    public void goLeave(){
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        finish();
    }

    public void addValidation() {
        recognizer.addGesture(GestureService.Gesture.GESTURE_VALIDATION, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeValidation() {
        recognizer.removeGesture(GestureService.Gesture.GESTURE_VALIDATION);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_new_game){
            TTSService.Speak("Nouvelle partie.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    newGame();
                }
            }, 1500);
        }
        else if (id == R.id.btn_options){
            TTSService.Speak("Menu des options.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    options();
                }
            }, 1500);
        }
        else if (id == R.id.btn_leave) {
            TTSService.Speak("Quitter l'application.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    leave();
                }
            }, 1500);
            this.options();
        }
    }

    public void onPause() {
        super.onPause();

        // tout arreter

        //
        // On arrête d'écouter les mouvements de l'utilisateur
        //
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // On demande a la dame de ne plus parler
        //
        TTSService.Stop();
    }

    public void onResume() {
        super.onResume();
        // tout remettre en marche

        //
        // Remise en place du gesture recognizer
        //
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void didReceiveNotificationForGesture(GestureService.Gesture g) {
        switch(g) {
            case GESTURE_YES_NO:
                //demander si l'utilisateur est sûr de vouloir faire un OUI / NON
                this.announceText("Oui Non", true);
                break;
        }
    }

    @Override
    public void didReceiveYesNoChange(int status) {

    }

    @Override
    public void didReceiveBackChange(int status) {

    }

    @Override
    public void didReceiveShakeChange(int status) {

    }

    @Override
    public void didReceiveValidation(int status) {
        View view = (View)this.btn_new_game.getParent();
        view.setBackgroundResource(R.drawable.background);
        if(status < 0) {
            // annulation
            this.announceText("Navigation annulée", false);
        } else {
            vibrator.validate();
            switch(this.method) {
                case NEWGAME:
                    this.goNewGame();
                    break;
                case OPTIONS:
                    this.goOptions();
                    break;
                case LEAVE:
                    this.goLeave();
                    break;
            }
            this.removeValidation();
        }
    }

    public void announceText(final String text, final boolean black) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(black) {
                    if(MainActivity.lateralite.equals("droitier")){
                        theView.setBackgroundResource(R.drawable.validation_droitier);
                    } else {
                        theView.setBackgroundResource(R.drawable.validation_gaucher);
                    }
                }
                TTSService.Stop();
                TTSService.Speak(text);
            }
        });
    }

    public void changeDroitier(boolean etat) {
        recognizer.setDroitier(etat);
    }

}
