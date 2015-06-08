package AccessibilityService;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;


/**
 * Created by Cnam on 17/04/2015.
 */
public class VibratorService {

        /*
            Variable privé de type Activité.
         */
        private Activity activity;

        /*
            Constructeur qui prend en paramère l'activite dans laquelle est instanciee le Vibreur
         */
        public VibratorService(Activity a){
            this.activity = a;
        }

        /*
            Fonction qui lance la sequence de vibration correspondante à la validation d'un evenement
         */
        public void validate(){
            Vibrator v = (Vibrator) activity.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
            long [] pattern = { 0, 100, 100, 100};
            v.vibrate(pattern, -1);
        }

    /*
        Fonction qui lance la sequence de vibration correspondante à une erreur
     */
        public void error(){
            Vibrator v = (Vibrator) activity.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
            long [] pattern = { 0, 800, 0, 0};
            v.vibrate(pattern, -1);
        }
}
