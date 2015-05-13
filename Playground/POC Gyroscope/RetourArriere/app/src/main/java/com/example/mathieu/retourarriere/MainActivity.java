package com.example.mathieu.retourarriere;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private SensorManager mManager = null;
    private Sensor mAccelerometre = null;
    private SensorEvent mSensor = null;

    final SensorEventListener mSensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Que faire en cas de changement de précision ?
        }

        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            System.out.println(x);
            System.out.println(y);
            System.out.println(z);
        }
    };

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        

        String txt = new String();
        TextView tv = new TextView(this);


        super.onCreate(savedInstanceState);
        setContentView(tv);

         mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
         mAccelerometre = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
         mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_FASTEST);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mManager.registerListener(mSensorEventListener, mAccelerometre, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mManager.unregisterListener(mSensorEventListener, mAccelerometre);

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
