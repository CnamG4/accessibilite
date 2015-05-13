package com.example.huunneki.helloworld;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Huunneki on 05/05/2015.
 */
public class AccessGestureRecognizer implements SensorEventListener, GestureCallbackInterface {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    // for each list for 1 index we have a gesture, the timestamp between the measures, and the function to be called
    private List<Gesture> gestureList;
    private List<Long> timestampList;
    private List<Long> lastMeasureTimeList;
    private List<GestureCallbackInterface> objectHandlerList;
    private List<Float[]> gravityList;
    private List<Float[]> geomagneticList;
    private List<Float> oldYawList;
    private List<Float> oldPitchList;
    private List<Float> oldRollList;
    private List<Boolean> firstMeasureList;

    // the object that is waiting for a validation
    private GestureCallbackInterface waitingObject = null;
    // the method of the object that needs to be called
    private GestureMethod waitingMethod = null;

    private boolean didValidate = false;
    private boolean isValidating = false;

    private long prevalidationTime = 1000;
    private int waitingStatus = 0;

    private AccessGestureRecognizer validator;

    @Override
    public void didReceiveNotificationForGesture(Gesture g) {

    }

    @Override
    public void didReceiveYesNoChange(int status) {

    }

    @Override
    public void didReceiveBackChange(int status) {

    }

    @Override
    public void didReceiveForwardChange(int status) {

    }

    @Override
    public void didReceiveValidation(int status) {
        this.executeWaitingObjectMethod(this.waitingStatus);
        this.didValidate = true;
        this.isValidating = false;
    }

    private static enum GestureMethod {
        BACK_CHANGE,
        FORWARD_CHANGE,
        YES_NO_CHANGE,
        VALIDATION
    }


    public static enum Gesture {
        GESTURE_YES_NO,
        GESTURE_BACK,
        GESTURE_FORWARD,
        GESTURE_VALIDATION
    }

    public AccessGestureRecognizer(SensorManager manager) {
        // initialization of the sensor manager
        sensorManager = manager;

        // initialization of the two sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // initialization of the specific lists (each index will be for one type of Gesture)
        gestureList = new ArrayList<Gesture>();
        timestampList = new ArrayList<Long>();
        lastMeasureTimeList = new ArrayList<Long>();
        objectHandlerList = new ArrayList<GestureCallbackInterface>();
        gravityList = new ArrayList<Float[]>();
        geomagneticList = new ArrayList<Float[]>();
        oldPitchList = new ArrayList<Float>();
        oldRollList = new ArrayList<Float>();
        oldYawList = new ArrayList<Float>();
        firstMeasureList = new ArrayList<Boolean>();
     }

    /* This method adds a Gesture to the Gesture Recognizer
     * f is the frequency of between two measures.
     */
    public boolean addGesture(Gesture g, Long f, GestureCallbackInterface callBackObject) {
        boolean effective = false;
        if(!gestureList.contains(g)) {
            gestureList.add(g);
            timestampList.add(f);
            lastMeasureTimeList.add(new Long(0));
            objectHandlerList.add(callBackObject);
            gravityList.add(new Float[9]);
            geomagneticList.add(new Float[9]);
            oldPitchList.add(new Float(0));
            oldRollList.add(new Float(0));
            oldYawList.add(new Float(0));
            firstMeasureList.add(new Boolean(true));
            effective = true;
        }
        return  effective;
    }

    /* This method removes the gesture G if it exists.
     *
     */
    public boolean removeGesture(Gesture g) {
        boolean effective = false;
        if(gestureList.size() > 0 && gestureList.contains(g)) {
            int index = gestureList.indexOf(g);
            gestureList.remove(g);
            timestampList.remove(index);
            lastMeasureTimeList.remove(index);
            objectHandlerList.remove(index);
            gravityList.remove(index);
            geomagneticList.remove(index);
            oldPitchList.remove(index);
            oldRollList.remove(index);
            oldYawList.remove(index);
            firstMeasureList.remove(index);
            effective = true;
        }
        return effective;
    }

    /* Start the sensors
     *
     */
    public void startGestureRecognizer() throws Exception {
        if(gestureList.size() > 0) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);

            this.validator = new AccessGestureRecognizer(this.sensorManager);
        }
        else {
            Exception e = new Exception("No gesture observed. Can't start");
            throw e;
        }
    }

    /* Start the sensors
    *
    */
    public void stopGestureRecognizer() throws Exception {
        if(gestureList.size() > 0) {
            sensorManager.unregisterListener(this, accelerometer);
            sensorManager.unregisterListener(this, magnetometer);
        } else {
            Exception e = new Exception("No gesture observed. Can't stop");
            throw e;
        }
    }

    /* Calls the specific method for each gesture
     *
     */
    public void handleForGesture(Gesture g, SensorEvent event) {
        switch (g) {
            case GESTURE_BACK:

                break;
            case GESTURE_FORWARD:

                break;
            case GESTURE_YES_NO:
                this.handleYesNoGesture(g, event);
                break;
            case GESTURE_VALIDATION:
                this.handleValidationGesture(g, event);
            default:
                break;
        }
    }

    public void handleValidationGesture(Gesture g, SensorEvent event) {
        int index = this.gestureList.indexOf(g);
        Long timestamp = timestampList.get(index);
        Long lastMeasureTime = lastMeasureTimeList.get(index);
        GestureCallbackInterface objectHandler = objectHandlerList.get(index);
        Float[] gravity = gravityList.get(index);
        Float[] geomagnetic = geomagneticList.get(index);
        Float oldYaw = oldYawList.get(index);
        Float oldPitch = oldPitchList.get(index);
        Float oldRoll = oldRollList.get(index);
        Boolean isFirstMeasure = firstMeasureList.get(index);

        Long currentTime = System.currentTimeMillis();

        // the time is valid, we know check the values of the sensors
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            copyPrimitiveFloatsToObjectsFloat(gravity, event.values);
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            copyPrimitiveFloatsToObjectsFloat(geomagnetic, event.values);
        if(gravity.length == 9 && gravity[0] != null && geomagnetic.length == 9 && geomagnetic[0] != null) {

            float Rotation[] = new float[9];
            getValuesFromSensors(Rotation, gravity, geomagnetic);

            float orientation[] = new float[3];

            SensorManager.getOrientation(Rotation, orientation);

            float yaw = Math.round(Math.toDegrees(orientation[0]));
            float pitch = Math.round(Math.toDegrees(orientation[1]));
            float roll = Math.round(Math.toDegrees(orientation[2]));

            if(lastMeasureTime == 0) {
                // we initialize our lastMeasureTime
                lastMeasureTime = System.currentTimeMillis();
            } else if(currentTime - lastMeasureTime >= timestamp) {
                if(isFirstMeasure) {
                    oldPitch = pitch;
                    oldYaw = yaw;
                    oldRoll = roll;
                    firstMeasureList.set(index, new Boolean(false));
                    lastMeasureTime = currentTime;
                } else {
                    if(Math.abs(roll - oldRoll) >= 30 && currentTime - lastMeasureTime >= 1000) {
                        this.waitingObject = objectHandler;
                        this.waitingMethod = GestureMethod.VALIDATION;
                        try {
                            this.stopGestureRecognizer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        this.removeGesture(Gesture.GESTURE_VALIDATION);
                        this.executeWaitingObjectMethod(0);
                        return;
                    }
                    oldPitch = pitch;
                    oldRoll = roll;
                    oldYaw = yaw;
                }
            }
            gravity[0] = null;
            geomagnetic[0] = null;
        }
        lastMeasureTimeList.set(index, lastMeasureTime);
        gravityList.set(index, gravity);
        geomagneticList.set(index, geomagnetic);
        oldYawList.set(index, oldYaw);
        oldPitchList.set(index, oldPitch);
        oldRollList.set(index, oldRoll);
    }

    /* Calculate if it is a NO or a YES.
     *
     */
    public void handleYesNoGesture(Gesture g, SensorEvent event) {
        int index = this.gestureList.indexOf(g);
        Long timestamp = timestampList.get(index);
        Long lastMeasureTime = lastMeasureTimeList.get(index);
        GestureCallbackInterface objectHandler = objectHandlerList.get(index);
        Float[] gravity = gravityList.get(index);
        Float[] geomagnetic = geomagneticList.get(index);
        Float oldYaw = oldYawList.get(index);
        Float oldPitch = oldPitchList.get(index);
        Float oldRoll = oldRollList.get(index);
        Boolean isFirstMeasure = firstMeasureList.get(index);

        Long currentTime = System.currentTimeMillis();

        // the time is valid, we know check the values of the sensors
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            copyPrimitiveFloatsToObjectsFloat(gravity, event.values);
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            copyPrimitiveFloatsToObjectsFloat(geomagnetic, event.values);
        if(gravity.length == 9 && gravity[0] != null && geomagnetic.length == 9 && geomagnetic[0] != null) {

            float Rotation[] = new float[9];
            getValuesFromSensors(Rotation, gravity, geomagnetic);

            float orientation[] = new float[3];

            SensorManager.getOrientation(Rotation, orientation);

            float yaw = Math.round(Math.toDegrees(orientation[0]));
            float pitch = Math.round(Math.toDegrees(orientation[1]));
            float roll = Math.round(Math.toDegrees(orientation[2]));

            if(lastMeasureTime == 0) {
                    // we initialize our lastMeasureTime
                    lastMeasureTime = System.currentTimeMillis();
                } else if(currentTime - lastMeasureTime >= timestamp) {
                    if(isFirstMeasure) {
                        oldPitch = pitch;
                        oldYaw = yaw;
                        oldRoll = roll;
                        firstMeasureList.set(index, new Boolean(false));
                        lastMeasureTime = currentTime;
                    } else {
                        if(Math.abs(roll - oldRoll) >= 30) {
                            this.waitingStatus = (int) -(roll - oldRoll);
                            if(didValidate && (currentTime - lastMeasureTime >= 1000)) {
                                    prevalidationTime = System.currentTimeMillis();
                                    this.waitingObject = objectHandler;
                                    this.waitingMethod = GestureMethod.YES_NO_CHANGE;
                                    this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_YES_NO);
                                    didValidate = false;
                                    lastMeasureTime = currentTime;
                                    this.isValidating = true;
                                    this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                    try {
                                        this.validator.startGestureRecognizer();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (!didValidate) {
                                    prevalidationTime = System.currentTimeMillis();
                                    this.waitingObject = objectHandler;
                                    this.waitingMethod = GestureMethod.YES_NO_CHANGE;
                                    this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_YES_NO);
                                    lastMeasureTime = currentTime;
                                    this.isValidating = true;
                                    this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                    try {
                                        this.validator.startGestureRecognizer();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                        } else {

                        }
                        oldPitch = pitch;
                        oldRoll = roll;
                        oldYaw = yaw;
                    }
            }
            gravity[0] = null;
            geomagnetic[0] = null;
        }
        lastMeasureTimeList.set(index, lastMeasureTime);
        gravityList.set(index, gravity);
        geomagneticList.set(index, geomagnetic);
        oldYawList.set(index, oldYaw);
        oldPitchList.set(index, oldPitch);
        oldRollList.set(index, oldRoll);
    }

    /* Get the values from the sensors and put it in rotation.
     * rotation[0], rotation[1], rotation[2] are yaw pitch and roll values
     */
    public static void getValuesFromSensors(float[] rotation, Float[] gravity, Float[] geomagnetic) {
        float[] temp = new float[9];
        float[] primGravity = new float[9];
        float[] primGeomagnetic = new float[9];

        copyObjectsFloatToPrimitiveFloat(primGravity, gravity);
        copyObjectsFloatToPrimitiveFloat(primGeomagnetic, geomagnetic);

        boolean success = SensorManager.getRotationMatrix(temp, null, primGravity, primGeomagnetic);

        if(success) {
            SensorManager.remapCoordinateSystem(temp,
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z, rotation);
        }
    }

    // copy an array of float to an array of Float
    public static void copyPrimitiveFloatsToObjectsFloat(Float[] newArray, float[] oldArray) {
        int i = 0;
        for (float value : oldArray) {
            newArray[i++] = Float.valueOf(value);
        }
    }

    // copy an array of Float to an array of float
    public static void copyObjectsFloatToPrimitiveFloat(float[] newArray, Float[] oldArray) {
        int i = 0;
        for (Float value : oldArray) {
            if(value != null)
                newArray[i++] = value;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!isValidating) {
            for (Gesture g : this.gestureList) {
                this.handleForGesture(g, event);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void executeWaitingObjectMethod(int status) {
        switch(this.waitingMethod) {
            case BACK_CHANGE:
                break;
            case FORWARD_CHANGE:
                break;
            case YES_NO_CHANGE:
                this.waitingObject.didReceiveYesNoChange(status);
                break;
            case VALIDATION:
                this.waitingObject.didReceiveValidation(status);
            default:
                break;
        }
        this.waitingMethod = null;
        this.waitingObject = null;
    }
}
