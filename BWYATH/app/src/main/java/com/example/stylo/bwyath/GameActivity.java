package com.example.stylo.bwyath;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity implements View.OnTouchListener, View.OnClickListener{

    private ImageButton btn_home, btn_back;

    private StoryGame myStory;

    private TextView txt_title, txt_content, txt_question;

    private RadioButton rb_choise1, rb_choise2, rb_choise3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        updateViewWithCurrentPage();
    }

    public void updateViewWithCurrentPage(){
        txt_title.setText(myStory.getCurrent_page().getTitle());
        txt_content.setText(myStory.getCurrent_page().getContent());
        txt_question.setText(myStory.getCurrent_page().getQuestion());
        if(myStory.getCurrent_page().getNumber() == 6) {
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
        if(myStory.getCurrent_page().getNumber() == 1){
            btn_back.setVisibility(View.INVISIBLE);
        }
        else{
            btn_back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_home) {
            GameActivity.this.finish();
        }
        else if (id == R.id.btn_back) {
            myStory.setCurrent_page(myStory.getStory().get(myStory.removeLast_page()-1));
            updateViewWithCurrentPage();
        }
        else if (id == R.id.rb_choise1) {
            myStory.addLast_page(myStory.getCurrent_page().getNumber());
            myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(0).getTarget()-1));
            rb_choise1.setChecked(false);
            updateViewWithCurrentPage();
        }
        else if (id == R.id.rb_choise2) {
            myStory.addLast_page(myStory.getCurrent_page().getNumber());
            myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(1).getTarget()-1));
            rb_choise2.setChecked(false);
            updateViewWithCurrentPage();
        }
        else if (id == R.id.rb_choise3) {
            myStory.addLast_page(myStory.getCurrent_page().getNumber());
            myStory.setCurrent_page(myStory.getStory().get(myStory.getCurrent_page().getChoise(2).getTarget()-1));
            rb_choise3.setChecked(false);
            updateViewWithCurrentPage();
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
}
