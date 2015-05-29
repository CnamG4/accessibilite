package com.example.stylo.bwyath;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.app.Activity;
import android.view.View;
import android.view.MotionEvent;

public class MainActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    // Bouton "Nouvelle partie"
    private ImageButton btn_new_game;
    // Bouton "Options"
    private ImageButton btn_options;
    // Bouton "Quitter"
    private ImageButton btn_leave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_new_game = (ImageButton) findViewById(R.id.btn_new_game);
        btn_new_game.setOnClickListener(this);

        btn_options = (ImageButton) findViewById(R.id.btn_options);
        btn_options.setOnClickListener(this);

        btn_leave = (ImageButton) findViewById(R.id.btn_leave);
        btn_leave.setOnClickListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_new_game){
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.btn_options){
            Intent intent = new Intent(MainActivity.this, OptionActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.btn_leave) {
            MainActivity.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
