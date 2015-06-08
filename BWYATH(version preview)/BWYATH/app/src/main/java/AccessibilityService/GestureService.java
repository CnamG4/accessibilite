package AccessibilityService;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huunneki on 05/05/2015.
 */
public class GestureService implements SensorEventListener, GestureListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

    private boolean droitier = true;

    // for each list for 1 index we have a gesture, the timestamp between the measures, and the function to be called
    private List<Gesture> gestureList;
    private List<Long> timestampList;
    private List<Long> lastMeasureTimeList;
    private List<GestureListener> objectHandlerList;
    private List<Float[]> gravityList;
    private List<Float[]> geomagneticList;
    private List<Float> oldYawList;
    private List<Float> oldPitchList;
    private List<Float> oldRollList;
    private List<Boolean> firstMeasureList;

    // the object that is waiting for a validation
    private GestureListener waitingObject = null;
    // the method of the object that needs to be called
    private GestureMethod waitingMethod = null;

    private boolean didValidate = false;
    private boolean isValidating = false;

    private long prevalidationTime = 1000;
    private int waitingStatus = 0;

    // for shake gesture
    private float mAccelShake; // acceleration apart from gravity
    private float mAccelShakeCurrent; // current acceleration including gravity
    private float mAccelShakeLast; // last acceleration including gravity

    // for yesno gesture
    private float mAccelYN; // acceleration apart from gravity
    private float mAccelYNCurrent; // current acceleration including gravity
    private float mAccelYNLast; // last acceleration including gravity

    // for back gesture
    private float mAccelBack; // acceleration apart from gravity
    private float mAccelBackCurrent; // current acceleration including gravity
    private float mAccelBackLast; // last acceleration including gravity

    // for validate gesture
    private float mAccelValidate; // acceleration apart from gravity
    private float mAccelValidateCurrent; // current acceleration including gravity
    private float mAccelValidateLast; // last acceleration including gravity

    private GestureService validator;

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
    public void didReceiveShakeChange(int status) {

    }

    @Override
    public void didReceiveValidation(int status) {
        int value = this.waitingStatus;
        if(status < 0) {
            value = 0;
        }
        this.executeWaitingObjectMethod(value);
        this.didValidate = true;
        this.isValidating = false;
        try {
            this.startGestureRecognizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static enum GestureMethod {
        BACK_CHANGE,
        SHAKE_CHANGE,
        YES_NO_CHANGE,
        VALIDATION
    }


    public static enum Gesture {
        GESTURE_YES_NO,
        GESTURE_BACK,
        GESTURE_SHAKE,
        GESTURE_VALIDATION
    }

    public GestureService(SensorManager manager) {
        // initialization of the sensor manager
        sensorManager = manager;

        // initialization of the two sensors

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // initialization of the specific lists (each index will be for one type of Gesture)
        gestureList = new ArrayList<Gesture>();
        timestampList = new ArrayList<Long>();
        lastMeasureTimeList = new ArrayList<Long>();
        objectHandlerList = new ArrayList<GestureListener>();
        gravityList = new ArrayList<Float[]>();
        geomagneticList = new ArrayList<Float[]>();
        oldPitchList = new ArrayList<Float>();
        oldRollList = new ArrayList<Float>();
        oldYawList = new ArrayList<Float>();
        firstMeasureList = new ArrayList<Boolean>();
    }

    public boolean isDroitier() {
        return droitier;
    }

    public void setDroitier(boolean d) {
        droitier = d;
    }

    /* This method adds a Gesture to the Gesture Recognizer
     * f is the frequency of between two measures.
     */
    public boolean addGesture(Gesture g, Long f, GestureListener callBackObject) {
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
            sensorManager.registerListener(this, accelerometer, 200000);
            sensorManager.registerListener(this, magnetometer, 200000);

            this.validator = new GestureService(this.sensorManager);
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
                this.handleBackGesture(g, event);
                break;
            case GESTURE_SHAKE:
                this.handleShakeGesture(g, event);
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
        GestureListener objectHandler = objectHandlerList.get(index);
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
        if (gravity.length == 9 && gravity[0] != null && geomagnetic.length == 9 && geomagnetic[0] != null) {

            float Rotation[] = new float[9];
            getValuesFromSensors(Rotation, gravity, geomagnetic);

            float orientation[] = new float[3];

            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];
            mAccelValidateLast = mAccelValidateCurrent;
            mAccelValidateCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelValidateCurrent - mAccelValidateLast;
            mAccelValidate = mAccelValidate * 0.9f + delta; // perform low-cut filter
            SensorManager.getOrientation(Rotation, orientation);

            float yaw = Math.round(Math.toDegrees(orientation[0]));
            float pitch = Math.round(Math.toDegrees(orientation[1]));
            float roll = Math.round(Math.toDegrees(orientation[2]));


            if (lastMeasureTime == 0) {
                // we initialize our lastMeasureTime
                lastMeasureTime = System.currentTimeMillis();
            } else if (currentTime - lastMeasureTime >= timestamp) {
                if (isFirstMeasure) {
                    oldPitch = pitch;
                    oldYaw = yaw;
                    oldRoll = roll;
                    firstMeasureList.set(index, new Boolean(false));
                    lastMeasureTime = currentTime;
                    mAccelValidateLast = mAccelValidate;
                } else {
                    if (Math.abs(roll - oldRoll) >= 35 && Math.abs(pitch-oldPitch) < 40 && Math.abs(yaw - oldYaw) >= 35 && currentTime - lastMeasureTime >= 1000) {
                        this.waitingObject = objectHandler;
                        this.waitingMethod = GestureMethod.VALIDATION;
                        try {
                            this.stopGestureRecognizer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        this.removeGesture(Gesture.GESTURE_VALIDATION);
                        int value = (int) -(roll - oldRoll);
                        value *= (droitier ? 1 : -1);
                        this.executeWaitingObjectMethod(value);
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


    /* Calculate if it is a BACK Gesture
     *
     */
    private void handleBackGesture(Gesture g, SensorEvent event) {
        int index = this.gestureList.indexOf(g);
        Long timestamp = timestampList.get(index);
        Long lastMeasureTime = lastMeasureTimeList.get(index);
        GestureListener objectHandler = objectHandlerList.get(index);
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

            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];
            mAccelBackLast = mAccelBackCurrent;
            mAccelBackCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelBackCurrent - mAccelBackLast;
            mAccelBack = mAccelBack * 0.9f + delta; // perform low-cut filter

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
                    mAccelBackLast = mAccelBack;
                } else {
                    if(Math.abs(pitch - oldPitch) >= 35 && Math.abs(roll - oldRoll) < 35) {
                        if(pitch - oldPitch < 0) {
                            this.waitingStatus = -(int) (pitch - oldPitch);
                            if (didValidate && (currentTime - lastMeasureTime >= 1000)) {
                                try {
                                    this.stopGestureRecognizer();
                                    prevalidationTime = System.currentTimeMillis();
                                    this.waitingObject = objectHandler;
                                    this.waitingMethod = GestureMethod.BACK_CHANGE;
                                    this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_BACK);
                                    lastMeasureTime = currentTime;
                                    didValidate = false;
                                    this.isValidating = true;
                                    this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                    this.validator.startGestureRecognizer();
                                    firstMeasureList.set(index, new Boolean(true));
                                    lastMeasureTime = (long) 0;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (!didValidate) {
                                try {
                                    this.stopGestureRecognizer();
                                    prevalidationTime = System.currentTimeMillis();
                                    this.waitingObject = objectHandler;
                                    this.waitingMethod = GestureMethod.BACK_CHANGE;
                                    this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_BACK);
                                    this.isValidating = true;
                                    this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                    this.validator.startGestureRecognizer();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                firstMeasureList.set(index, new Boolean(true));
                                lastMeasureTime = (long) 0;
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


    /* Calculate if it is a SHAKE Gesture
     *
     */
    private void handleShakeGesture(Gesture g, SensorEvent event) {
        int index = this.gestureList.indexOf(g);
        Long timestamp = timestampList.get(index);
        Long lastMeasureTime = lastMeasureTimeList.get(index);
        GestureListener objectHandler = objectHandlerList.get(index);
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

            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];
            mAccelShakeLast = mAccelShakeCurrent;
            mAccelShakeCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelShakeCurrent - mAccelShakeLast;
            mAccelShake = mAccelShake * 0.9f + delta; // perform low-cut filter

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
                    mAccelShakeLast = mAccelShake;
                } else {
                    if(Math.abs(roll - oldRoll) >= 40 && Math.abs(yaw - oldYaw) >= 40 && mAccelShake >= 4) {
                        this.waitingStatus = 1;
                        if (didValidate && (currentTime - lastMeasureTime >= 1000)) {
                            try {
                                this.stopGestureRecognizer();
                                prevalidationTime = System.currentTimeMillis();
                                this.waitingObject = objectHandler;
                                this.waitingMethod = GestureMethod.SHAKE_CHANGE;
                                this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_SHAKE);
                                lastMeasureTime = currentTime;
                                didValidate = false;
                                this.isValidating = true;
                                this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                this.validator.startGestureRecognizer();
                                firstMeasureList.set(index, new Boolean(true));
                                lastMeasureTime = (long) 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (!didValidate) {
                            try {
                                this.stopGestureRecognizer();
                                prevalidationTime = System.currentTimeMillis();
                                this.waitingObject = objectHandler;
                                this.waitingMethod = GestureMethod.SHAKE_CHANGE;
                                this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_SHAKE);
                                this.isValidating = true;
                                this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                this.validator.startGestureRecognizer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            firstMeasureList.set(index, new Boolean(true));
                            lastMeasureTime = (long) 0;
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

    /* Calculate if it is a NO or a YES.
     *
     */
    public void handleYesNoGesture(Gesture g, SensorEvent event) {
        int index = this.gestureList.indexOf(g);
        Long timestamp = timestampList.get(index);
        Long lastMeasureTime = lastMeasureTimeList.get(index);
        GestureListener objectHandler = objectHandlerList.get(index);
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

            float x = gravity[0];
            float y = gravity[1];
            float z = gravity[2];
            mAccelYNLast = mAccelYNCurrent;
            mAccelYNCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelYNCurrent - mAccelYNLast;
            mAccelYN = mAccelYN * 0.9f + delta; // perform low-cut filter

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
                    mAccelYNLast = mAccelYN;
                } else {
                    if(Math.abs(roll - oldRoll) >= 35 && Math.abs(pitch-oldPitch) < 40 && Math.abs(yaw - oldYaw) >= 35 && mAccelYN >= 1) {
                        this.waitingStatus = (int) -(roll - oldRoll);
                        this.waitingStatus *= (droitier ? 1 : -1);
                        if(didValidate && (currentTime - lastMeasureTime >= 1000)) {
                            try {
                                this.stopGestureRecognizer();
                                prevalidationTime = System.currentTimeMillis();
                                this.waitingObject = objectHandler;
                                this.waitingMethod = GestureMethod.YES_NO_CHANGE;
                                this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_YES_NO);
                                lastMeasureTime = currentTime;
                                didValidate = false;
                                this.isValidating = true;
                                this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                this.validator.startGestureRecognizer();
                                firstMeasureList.set(index, new Boolean(true));
                                lastMeasureTime = (long) 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (!didValidate) {
                            try {
                                this.stopGestureRecognizer();
                                prevalidationTime = System.currentTimeMillis();
                                this.waitingObject = objectHandler;
                                this.waitingMethod = GestureMethod.YES_NO_CHANGE;
                                this.waitingObject.didReceiveNotificationForGesture(Gesture.GESTURE_YES_NO);
                                this.isValidating = true;
                                this.validator.addGesture(Gesture.GESTURE_VALIDATION, (long) 350, this);
                                this.validator.startGestureRecognizer();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            firstMeasureList.set(index, new Boolean(true));
                            lastMeasureTime = (long) 0;
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
                this.waitingObject.didReceiveBackChange(status);
                break;
            case SHAKE_CHANGE:
                this.waitingObject.didReceiveShakeChange(status);
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

    public void restartIfNeeded() {
        if(this.isValidating) {
            this.isValidating = false;
            this.removeGesture(Gesture.GESTURE_VALIDATION);
        }
        this.didValidate = true;
    }
}
