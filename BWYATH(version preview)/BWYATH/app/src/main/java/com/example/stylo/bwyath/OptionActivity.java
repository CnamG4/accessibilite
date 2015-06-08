package com.example.stylo.bwyath;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import AccessibilityService.GestureListener;
import AccessibilityService.GestureService;
import AccessibilityService.TTSService;
import AccessibilityService.VibratorService;


public class OptionActivity extends ActionBarActivity implements View.OnTouchListener, View.OnClickListener, GestureListener {

    // Bouton de retour à la page d'accueil
    private ImageButton btn_home;

    private static View theView;

    private GestureService recognizer;

    private TextToSpeech tts;

    private VibratorService vibrator;

    private enum Method {
        HOME,
        UPDATE
    }

    private Method method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);
        theView = (View) btn_home.getParent();

        recognizer = new GestureService((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        recognizer.addGesture(GestureService.Gesture.GESTURE_SHAKE, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        tts = TTSService.getTTS(this);
        vibrator = new VibratorService(this);

    }

    public void goHome() {
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        OptionActivity.this.finish();
    }

    public void home(){
        TTSService.Stop();
        this.announceText("Êtes vous sûr de vouloir retourner au menu principal ?", true);
        this.method = Method.HOME;
        this.addValidation();
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
        recognizer.addGesture(GestureService.Gesture.GESTURE_SHAKE, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_home){
            this.home();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void didReceiveNotificationForGesture(GestureService.Gesture g) {
        switch(g) {
            case GESTURE_SHAKE:
                //demander si l'utilisateur est sûr de vouloir faire une secousse
                this.announceText("Êtes-vous sûr de vouloir revenir au menu principal?", true);
                break;
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
        View view = (View)this.btn_home.getParent();
        view.setBackgroundResource(R.drawable.background);
        if(status > 0) {
            this.home();
        }
        else {
            this.announceText("Retour au menu principal annulé", false);
        }
    }

    @Override
    public void didReceiveValidation(int status) {
        View view = (View)this.btn_home.getParent();
        view.setBackgroundResource(R.drawable.background);
        if(status < 0) {
            // annulation
            this.announceText("Navigation annulée", false);
        } else {
            vibrator.validate();
            switch(this.method) {
                case HOME:
                    this.goHome();
                    break;
                case UPDATE:
                    break;
            }
            this.removeValidation();
        }
    }

    public void addValidation() {
        recognizer.removeGesture(GestureService.Gesture.GESTURE_SHAKE);
        recognizer.addGesture(GestureService.Gesture.GESTURE_VALIDATION, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeValidation() {
        recognizer.addGesture(GestureService.Gesture.GESTURE_SHAKE, (long) 350, this);
        recognizer.removeGesture(GestureService.Gesture.GESTURE_VALIDATION);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void announceText(final String text, final boolean black) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(black) {
                    theView.setBackgroundResource(R.drawable.validation_droitier);
                }
                TTSService.Stop();
                TTSService.Speak(text);
            }
        });
    }
}
