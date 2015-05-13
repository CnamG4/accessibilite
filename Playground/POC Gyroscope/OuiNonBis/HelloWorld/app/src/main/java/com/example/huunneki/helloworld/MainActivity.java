package com.example.huunneki.helloworld;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.huunneki.helloworld.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements GestureCallbackInterface {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor magnetometer;

    private long timestamp;
    private long timestampValidate;
    private long currTime;

    private float oldYaw;
    private float oldRoll;
    private float oldPitch;

    private AccessGestureRecognizer recognizer;

    private boolean canGo;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        recognizer = new AccessGestureRecognizer((SensorManager) getSystemService(Context.SENSOR_SERVICE));
      //  recognizer.addGesture(AccessGestureRecognizer.Gesture.GESTURE_YES_NO, (long) 350, this);
        recognizer.addGesture(AccessGestureRecognizer.Gesture.GESTURE_BACK, (long) 350, this);
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    protected void onPause() {
        super.onPause();
        //senSensorManager.unregisterListener(this);
        try {
            recognizer.stopGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    protected void onResume() {
        super.onResume();
        try {
            recognizer.startGestureRecognizer();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //senSensorManager.registerListener(this, this.senAccelerometer, SensorManager.SENSOR_DELAY_UI);
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

    /*float[] mGravity;
    float[] mGeomagnetic;

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {

            // get the text fields
            TextView text = (TextView) findViewById(R.id.X);
            TextView text2 = (TextView) findViewById(R.id.Y);
            TextView text3 = (TextView) findViewById(R.id.Z);
            float Rotation[] = new float[9];
            float temp[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(temp, null, mGravity, mGeomagnetic);

            currTime = System.currentTimeMillis();
            if (success) {

                //Remap to camera's point-of-view
                SensorManager.remapCoordinateSystem(temp,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z, Rotation);


                float orientation[] = new float[3];
                SensorManager.getOrientation(Rotation, orientation);

                float yaw = Math.round(Math.toDegrees(orientation[0]));
                float pitch = Math.round(Math.toDegrees(orientation[1]));
                float roll = Math.round(Math.toDegrees(orientation[2]));

                // set text color
                text.setTextColor(Color.BLACK);
                text2.setTextColor(Color.BLACK);
                text3.setTextColor(Color.BLACK);

                if (currTime - timestamp >= 300) {
                    timestamp = currTime;
                    if (oldRoll == 0 && oldPitch == 0 && oldYaw == 0) {
                        oldPitch = pitch;
                        oldRoll = roll;
                        oldYaw = yaw;
                    } else {
                        if (roll - oldRoll >= 30 && canGo) {
                            text.setText("NO");
                            // execute code here for NO
                            timestampValidate = currTime;
                            canGo = false;
                        } else if (roll - oldRoll <= -30 && canGo) {
                            text.setText("YES");
                            // execute code here for YES
                            timestampValidate = currTime;
                            canGo = false;
                        } else if (!canGo) {

                        }
                    }
                    oldPitch = pitch;
                    oldRoll = roll;
                    oldYaw = yaw;
                }
                if (currTime - timestampValidate >= 1500 && !canGo) {
                    text.setText("You can re test...");
                    canGo = true;
                    timestampValidate = currTime;
                }
            }
        }
    }*/

    /*@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/

    @Override
    public void didReceiveNotificationForGesture(AccessGestureRecognizer.Gesture g) {
        switch(g) {
            case GESTURE_YES_NO:
                // implement the question "Are you sure..."
                TextView text = (TextView) findViewById(R.id.X);
                text.setTextColor(Color.BLACK);
                text.setText("Are you sure you want to do YES or NO ?");
                break;
            case GESTURE_BACK:
                // implement the question "Are you sure..."
                TextView text2 = (TextView) findViewById(R.id.Y);
                text2.setTextColor(Color.BLACK);
                text2.setText("Are you sure you want to do BACK ?");
                break;
            case GESTURE_FORWARD:
                // implement the question "Are you sure..."
                break;
        }
    }

    @Override
    public void didReceiveYesNoChange(int status) {
        if (status == 0) return;
        TextView text = (TextView) findViewById(R.id.X);
        text.setTextColor(Color.BLACK);
        if (status > 0) {
            text.setText("Nope");
            // Case of NO
        } else if (status < 0) {
            text.setText("Yes");

            System.out.println(text.getText());
            // Case of YES
        }
    }

    @Override
    public void didReceiveBackChange(int status) {

        if (status == 0) return;
        TextView text = (TextView) findViewById(R.id.Y);
        text.setTextColor(Color.BLACK);
        if (status > 0) {
            text.setText("BACK");
            // Case of BACK
        } else if (status < 0) {
            text.setText("NO BACK");
            // Case of NO BACK
        }
    }

    @Override
    public void didReceiveForwardChange(int status) {
        if (status == 0) return;
    }

    @Override
    public void didReceiveValidation(int status) {

    }
}