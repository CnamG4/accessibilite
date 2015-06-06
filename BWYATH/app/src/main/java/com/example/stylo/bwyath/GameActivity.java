package com.example.stylo.bwyath;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import AccessibilityService.AccessGestureRecognizer;
import AccessibilityService.GestureCallbackInterface;
import AccessibilityService.TTSService;
import AccessibilityService.VibratorService;



public class GameActivity extends ActionBarActivity implements View.OnTouchListener, View.OnClickListener, GestureCallbackInterface {

    private ImageButton btn_home, btn_back;

    private StoryGame myStory;

    private TextView txt_title, txt_content, txt_question;

    private RadioButton rb_choise1, rb_choise2, rb_choise3;

    private AccessGestureRecognizer recognizer;

    private TextToSpeech tts;

    private VibratorService vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        recognizer = new AccessGestureRecognizer((SensorManager) getSystemService(Context.SENSOR_SERVICE));
        recognizer.addGesture(AccessGestureRecognizer.Gesture.GESTURE_BACK, (long) 350, this);
        recognizer.addGesture(AccessGestureRecognizer.Gesture.GESTURE_SHAKE, (long) 350, this);
        tts = TTSService.getTTS(this);

        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_content = (TextView) findViewById(R.id.txt_content);
        txt_question = (TextView) findViewById(R.id.txt_question);
        rb_choise1 = (RadioButton) findViewById(R.id.rb_choise1);
        rb_choise1.setOnClickListener(this);
        rb_choise2 = (RadioButton) findViewById(R.id.rb_choise2);
        rb_choise2.setOnClickListener(this);
        rb_choise3 = (RadioButton) findViewById(R.id.rb_choise3);
        rb_choise3.setOnClickListener(this);

        myStory = new StoryGame();
        myStory.initHistory();

        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        this.updateViewWithCurrentPage();
    }

    public void updateViewWithCurrentPage(){
        txt_title.setText(myStory.getCurrent_page().getTitle());
        txt_content.setText(myStory.getCurrent_page().getContent());
        txt_question.setText(myStory.getCurrent_page().getQuestion());
        if(myStory.getCurrent_page().getType().equals("end")) {
            rb_choise1.setVisibility(View.INVISIBLE);
            rb_choise2.setVisibility(View.INVISIBLE);
            rb_choise3.setVisibility(View.INVISIBLE);
        }else{
            rb_choise1.setVisibility(View.VISIBLE);
            rb_choise2.setVisibility(View.VISIBLE);
            rb_choise3.setVisibility(View.VISIBLE);
            rb_choise1.setText(myStory.getCurrent_page().getChoise(0).getContent());
            rb_choise2.setText(myStory.getCurrent_page().getChoise(1).getContent());
            rb_choise3.setText(myStory.getCurrent_page().getChoise(2).getContent());
        }
        if(myStory.getCurrent_page().getType().equals("begin")){
            btn_back.setVisibility(View.INVISIBLE);
        }
        else{
            btn_back.setVisibility(View.VISIBLE);
        }
        this.speakPage();
    }

    public void choise1(){
        myStory.addLast_page(myStory.getCurrent_page().getNumber());
        myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(0).getTarget()-1));
        rb_choise1.setChecked(false);
        this.updateViewWithCurrentPage();
    }

    public void choise2(){
        myStory.addLast_page(myStory.getCurrent_page().getNumber());
        myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(1).getTarget()-1));
        rb_choise2.setChecked(false);
        this.updateViewWithCurrentPage();
    }

    public void choise3(){
        myStory.addLast_page(myStory.getCurrent_page().getNumber());
        myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(2).getTarget()-1));
        rb_choise3.setChecked(false);
        this.updateViewWithCurrentPage();
    }

    public void back(){
        TTSService.Speak("Retour à la page précédente.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                myStory.setCurrent_page(myStory.getStory().get(myStory.removeLast_page()-1));
                updateViewWithCurrentPage();
            }
        }, 2000);
    }

    public void home(){
        TTSService.Speak("Retour au menu.");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                GameActivity.this.finish();
            }
        }, 2000);
    }

    public void speakPage(){
        TTSService.Speak(myStory.getCurrent_page().getTitle()
                +myStory.getCurrent_page().getContent()
                +myStory.getCurrent_page().getQuestion()
        );
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_home) {
               this.home();
        }
        else if (id == R.id.btn_back) {
            this.back();
        }
        else if (id == R.id.rb_choise1) {
            this.choise1();
        }
        else if (id == R.id.rb_choise2) {
            this.choise2();
        }
        else if (id == R.id.rb_choise3) {
            this.choise3();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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
    public void didReceiveNotificationForGesture(AccessGestureRecognizer.Gesture g) {

    }

    @Override
    public void didReceiveYesNoChange(int status) {

    }

    @Override
    public void didReceiveBackChange(int status) {
        if(status > 0 && myStory.getCurrent_page().getNumber()>1) {
           this. back();
        }
    }

    @Override
    public void didReceiveShakeChange(int status) {
        if(status == 1) {
            this.home();
        }
    }

    @Override
    public void didReceiveValidation(int status) {

    }
}
