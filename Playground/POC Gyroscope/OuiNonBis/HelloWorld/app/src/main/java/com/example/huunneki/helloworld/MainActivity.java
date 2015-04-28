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


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Sensor magnetometer;

    private long timestamp;
    private long timestampValidate;
    private long currTime;

    private float oldYaw;
    private float oldRoll;
    private float oldPitch;

    private boolean canGo;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timestamp = System.currentTimeMillis();
        timestampValidate = System.currentTimeMillis();
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        magnetometer = senSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_UI);
        senSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

        oldYaw = 0;
        oldPitch = 0;
        oldRoll = 0;
        canGo = true;
    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, this.senAccelerometer, SensorManager.SENSOR_DELAY_UI);
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

    /*public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.
        if (timestamp != 0) {
            currTime = System.currentTimeMillis();
            long dT = currTime - timestamp;
            if(dT >= 400) {
                timestamp = currTime;
                float test[];
                float mdr[];
                senSensorManager.getOrientation(mdr, test);
                System.out.println(test);
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];
                TextView text = (TextView) findViewById(R.id.X);
                TextView text2 = (TextView) findViewById(R.id.Y);
                TextView text3 = (TextView) findViewById(R.id.Z);

                float myAngleXY = (float) (Math.atan2(axisX, axisY)/(Math.PI/180));
                float myAngleXZ = (float) (Math.atan2(axisX, axisZ)/(Math.PI/180));
                float myAngleYZ = (float) (Math.atan2(axisY, axisZ)/(Math.PI/180));

                text.setTextColor(Color.RED);
                text2.setTextColor(Color.RED);
                text3.setTextColor(Color.RED);
                text.setText("X : " + axisX +"\nY : " + axisY + "\nZ : " + axisZ + "\nAngleXY : " + myAngleXY);
                text2.setText("AngleXZ : " + myAngleXZ);
                text3.setText("AngleYZ : " + myAngleYZ);
                if(Math.abs(myAngleXY - angleXY) >= 30) {
                    text.setText("NICE");
                    angleXY = myAngleXY;
                } else {
                    angleXY = myAngleXY;
                }
            }
        }
        else {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            angleXY = (float) (Math.atan2(axisX, axisY)/(Math.PI/180));
            angleXZ = (float) (Math.atan2(axisX, axisZ)/(Math.PI/180));
            angleYZ = (float) (Math.atan2(axisY, axisZ)/(Math.PI/180));
            timestamp = System.currentTimeMillis();
        }
    }*/

    float[] mGravity;
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

                /*text.setText("Yaw : " +yaw);
                text2.setText("Pitch : " +pitch);
                text3.setText("Roll : " +roll);*/

                if(currTime - timestamp >= 300) {
                    timestamp = currTime;
                    if(oldRoll == 0 && oldPitch == 0 && oldYaw == 0) {
                        oldPitch = pitch;
                        oldRoll = roll;
                        oldYaw = yaw;
                    } else {
                        if(roll - oldRoll >= 30 && canGo) {
                            text.setText("NO");
                            // execute code here for NO
                            timestampValidate = currTime;
                            canGo = false;
                        } else if(roll - oldRoll <= -30 && canGo) {
                            text.setText("YES");
                            // execute code here for YES
                            timestampValidate = currTime;
                            canGo = false;
                        } else if(!canGo) {

                        }
                    }
                    oldPitch = pitch;
                    oldRoll = roll;
                    oldYaw = yaw;
                }
                if(currTime - timestampValidate >= 1500 && !canGo) {
                    text.setText("You can re test...");
                    canGo = true;
                    timestampValidate = currTime;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
