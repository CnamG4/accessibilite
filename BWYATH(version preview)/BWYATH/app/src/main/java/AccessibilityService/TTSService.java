package AccessibilityService;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.app.Activity;
import android.util.Log;
import java.lang.String;
import java.util.Locale;


/**
 * Created by Cnam on 17/04/2015.
 */
public class TTSService extends Activity{

    private static TextToSpeech tts;

    /*
       Methodes qui prend en parametre l'activite dans laquelle le TextToSpeech est renseigne
       et renvoie un objet de type TextToSpeech
    */
    public static TextToSpeech getTTS(Activity a){
        tts = new TextToSpeech(a, new OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(Locale.FRANCE);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        return tts;
    }

    /*
        Methodes qui prend en parametre l'activite dans laquelle le TextToSpeech est renseigne
        ainsi qu'une variable de type Locale qui va etre le language du TextToSpeech
        et renvoie un objet de type TextToSpeech
     */
    public static TextToSpeech getTTS(Activity a, final Locale loc){
        tts = new TextToSpeech(a, new OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(loc);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });
        return tts;
    }

    /*
        Methode qui prend en paramètre une chaine de caractere.
        Enonce la chaine a haute vois en se servant tu TextToSpeech
     */
    public static void Speak(String text) {
        // TODO Auto-generated method stub
        if(text==null||"".equals(text))
        {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    /*
        Coupe la parole du TextToSpeech
     */

    public static void Stop() {
        tts.stop();
    }

    /*
        Renvoie une chaine de caractere représentant le language du TextToSpeech
     */
    public static String GetLanguage(){
        return tts.getLanguage().getDisplayLanguage();

    }

    /*
        Methode qui prend en paramatre une variable de Type Locale.
        Permet de changer le language du TextToSpeech
     */
    public static void SetLanguage(Locale l){
        tts.setLanguage(l);
    }
}