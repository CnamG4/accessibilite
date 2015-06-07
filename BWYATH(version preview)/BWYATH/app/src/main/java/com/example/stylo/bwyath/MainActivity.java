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

    private GestureService recognizer;

    private TextToSpeech tts;

    private VibratorService vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        btn_new_game = (ImageButton) findViewById(R.id.btn_new_game);
        btn_new_game.setOnClickListener(this);
        btn_options = (ImageButton) findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);
        btn_leave = (ImageButton) findViewById(R.id.btn_leave);
        btn_leave.setOnClickListener(this);
        vibrator = new VibratorService(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_new_game){
            vibrator.validate();
            TTSService.Speak("Nouvelle aventure.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                }
            }, 1500);
        }
        else if (id == R.id.btn_options){
            vibrator.validate();
            TTSService.Speak("Menu des options.");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, OptionActivity.class);
                    startActivity(intent);
                }
            }, 1500);
        }
        else if (id == R.id.btn_leave) {
            vibrator.validate();
            this.finish();
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
