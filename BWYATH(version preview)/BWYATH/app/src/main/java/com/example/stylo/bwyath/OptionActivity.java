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

    private GestureService recognizer;

    private TextToSpeech tts;

    private VibratorService vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);

        recognizer = new GestureService((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        recognizer.addGesture(GestureService.Gesture.GESTURE_BACK, (long) 350, this);
        recognizer.addGesture(GestureService.Gesture.GESTURE_SHAKE, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        tts = TTSService.getTTS(this);
        vibrator = new VibratorService(this);
    }

    public void home(){
        vibrator.validate();
        TTSService.Speak("Retour au menu.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                OptionActivity.this.finish();
            }
        }, 1500);
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

    }
}
