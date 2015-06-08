package AccessibilityService;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cnam on 05/05/2015.
 */
public class GestureService implements SensorEventListener, GestureListener {

    /*
     * Sensors section
     */

    /***
     * Le manager qui nous d�livrera les capteurs
     */
    private SensorManager sensorManager;

    /***
     * L'acc�l�rometre qui calcule les d�placements de l'appareil
     */
    private Sensor accelerometer;

    /***
     * Le magn�tometre, qui permet de calculer les angles si associ� � l'acc�l�rom�tre
     */
    private Sensor magnetometer;

    /***
     * Par d�faut, l'utilisateur est droitier.
     */
    private boolean droitier = true;

    /****
     * Section listes de variables
     * Dans chacune des listes ci dessous, pour un index donn�, on acc�de � toutes les variables
     * (calculs, temps, etc) li�es � un type de mouvement.
     */

    /***
     * La liste contenant tous les mouvements que le service �coutera
     */
    private List<Gesture> gestureList;

    /***
     * Le temps entre chaque mesure
     */
    private List<Long> timestampList;

    /***
     * La valeur num�rique de la derni�re mesure
     */
    private List<Long> lastMeasureTimeList;

    /***
     * L'objet auquel nous enverrons des messages en cas d'activit�, de mouvement capt�
     */
    private List<GestureListener> objectHandlerList;

    /***
     * Tableau contenant les donn�es de gravit� brutes
     */
    private List<Float[]> gravityList;

    /***
     * Tableau contenant les donn�es magn�tiques brutes
     */
    private List<Float[]> geomagneticList;

    /***
     * Derniere valeur d'angle "Yaw"
     */
    private List<Float> oldYawList;

    /***
     * Derniere valeur d'angle "Pitch"
     */
    private List<Float> oldPitchList;

    /***
     * Derni�re valeur d'angle "Roll"
     */
    private List<Float> oldRollList;

    /***
     * Bool�en permettant de savoir s'il s'agit de la premi�re mesure
     */
    private List<Boolean> firstMeasureList;

    /***
     * L'objet qui recevra un message de la part du service
     */
    private GestureListener waitingObject = null;

    /***
     * La m�thode � appeler pour le waiting object
     */
    private GestureMethod waitingMethod = null;

    /***
     * La valeur qui sera pass�e en param�tre de la m�thode du waiting object
     */
    private int waitingStatus = 0;

    /***
     * Bool�en d�terminant si le service vient de faire une validation
     */
    private boolean didValidate = false;

    /***
     * Bool�en d�terminant si le service est en train de valider un mouvement
     */
    private boolean isValidating = false;


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

    /***
     * Le service qui validera les diff�rentes actions d�tect�es
     */
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

    /***
     * Enum qui permet de g�rer les diff�rentes m�thodes � appeler de l'interface "Gesture Listener"
     */
    private enum GestureMethod {
        BACK_CHANGE,
        SHAKE_CHANGE,
        YES_NO_CHANGE,
        VALIDATION
    }

    /***
     * Enum qui permet de g�rer les diff�rents mouvements g�r�s par le service
     */
    public enum Gesture {
        GESTURE_YES_NO,
        GESTURE_BACK,
        GESTURE_SHAKE,
        GESTURE_VALIDATION
    }

    /***
     * Constructeur GestureService
     * @param manager - Le SensorManager qui d�livrera les capteurs
     */
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

    /***
     * Renvoie la lat�ralit� du service, gauche ou droite (faux ou vrai)
     * @return La lat�ralit� du service
     */
    public boolean isDroitier() {
        return droitier;
    }

    /***
     * Param�tre la lat�ralit� du service
     * @param d - Vrai pour droiti�, faux pour gaucher
     */
    public void setDroitier(boolean d) {
        droitier = d;
    }

    /***
     * Ajoute un mouvement � observer au service, si ce dernier n'est pas observ�
     * @param g - Le mouvement � observer
     * @param f - La fr�quence entre chaque saisie
     * @param callBackObject - L'objet � qui les messages seront envoy�s
     * @return le r�sultat de l'ajout (vrai ou faux)
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

    /***
     * Retire le mouvement que l'on enregistre en param�tre
     * @param g - Le mouvement � retirer
     * @return le r�sultat du retrait (vrai ou faux)
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

    /***
     * Lance l'enregistrement des mouvements de l'appareil par le service
     * @throws Exception en cas d'absence de mouvement � observer
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

    /***
     * Arr�te l'enregistrement des mouvements par le service
     * @throws Exception en cas d'absence de mouvement � observer
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

    /***
     * Appelle la fonction d�di�e au mouvement g pass� en param�tre
     * @param g - Le mouvement observ�
     * @param event - L'�v�nement du capteur qui a effectu� la mesure
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


    /*****
     *
     * La logique pour chaque handler est toujours la m�me, seules certaines parties changent, celles li�es aux angles �tudi�s.
     *
     */


    /***
     * Traite la validation
     * @param g - Le mouvement observ�
     * @param event - L'�v�nement du capteur qui a effectu� la mesure
     */
    public void handleValidationGesture(Gesture g, SensorEvent event) {

        /***
         *
         * R�cup�ration des variables li�es au mouvement g
         *
         */
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

        // the time is valid, we now check the values of the sensors
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

            /***
             *
             * R�cup�ration des 3 angles d'orientation de l'appareil
             *
             */
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
                    /***
                     * Condition d'acceptation de la validation
                     */
                    if (Math.abs(roll - oldRoll) >= 35 && Math.abs(pitch - oldPitch) <= 40 && currentTime - lastMeasureTime >= 600) {
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


    /***
     * Traite le mouvement de l'appareil vers l'arri�re
     * @param g - Le mouvement observ�
     * @param event - L'�v�nement du capteur qui a effectu� la mesure
     */
    private void handleBackGesture(Gesture g, SensorEvent event) {

        /***
         *
         * R�cup�ration des variables li�es au mouvement g
         *
         */
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

        // the time is valid, we now check the values of the sensors
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


    /***
     * Traite le mouvement de l'appareil lors d'une secousse
     * @param g - Le mouvement observ�
     * @param event - L'�v�nement du capteur qui a effectu� la mesure
     */
    private void handleShakeGesture(Gesture g, SensorEvent event) {

        /***
         *
         * R�cup�ration des variables li�es au mouvement g
         *
         */
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

        // the time is valid, we now check the values of the sensors
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

    /***
     * Traite le mouvement de l'appareil de gauche � droite, comme une validation
     * @param g - Le mouvement observ�
     * @param event - L'�v�nement du capteur qui a effectu� la mesure
     */
    public void handleYesNoGesture(Gesture g, SensorEvent event) {

        /***
         *
         * R�cup�ration des variables li�es au mouvement g
         *
         */
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

        // the time is valid, we now check the values of the sensors
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

    /***
     * R�cup�re les valeurs des capteurs et en d�duit les valeurs des angles de l'appareil
     * @param rotation - Le tableau contenant les trois angles
     *                 rotation[0], rotation[1], rotation[2] sont les valeurs des angles yaw pitch and roll
     * @param gravity - Le tableau contenant les valeurs de gravit�
     * @param geomagnetic - Le tableau contenant les valeurs magn�tiques
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

    /***
     * Copie des floats primitifs vers des Floats objets
     * @param newArray
     * @param oldArray
     */
    public static void copyPrimitiveFloatsToObjectsFloat(Float[] newArray, float[] oldArray) {
        int i = 0;
        for (float value : oldArray) {
            newArray[i++] = Float.valueOf(value);
        }
    }

    /***
     * Copie des Floats objects vers des floats primitifs
     * @param newArray
     * @param oldArray
     */
    public static void copyObjectsFloatToPrimitiveFloat(float[] newArray, Float[] oldArray) {
        int i = 0;
        for (Float value : oldArray) {
            if(value != null)
                newArray[i++] = value;
        }
    }

    /**
     * Appel� � chaque mesure.
     * Cette fonction permet d'appeler les fonctions d�di�es � chaque mouvement li� au service.
     * @param event - L'�v�nement du capteur qui a effetu� une mesure
     */
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

    /***
     * Appelle la methode de l'objet qui attend une r�ponse
     * @param status - Le "sens" du mouvement, la valeur ressortie des calculs
     */
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

    /***
     * Relance le service s'il est bloqu� (arrive dans certains cas
     */
    public void restartIfNeeded() {
        if(this.isValidating) {
            this.isValidating = false;
            this.removeGesture(Gesture.GESTURE_VALIDATION);
        }
        this.didValidate = true;
    }
}
