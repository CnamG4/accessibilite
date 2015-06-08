package com.example.tony.vibro;

import android.annotation.TargetApi;
import android.app.Service;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.content.Intent;
import android.app.Activity;
import android.util.Log;

import java.lang.String;
import java.util.Locale;
import java.util.Random;

public class TTSService extends Activity{

    private static TextToSpeech tts;

    public static TextToSpeech getTTS(Activity a){
        tts = new TextToSpeech(a, new TextToSpeech.OnInitListener() {

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

    public static TextToSpeech getTTS(Activity a, final Locale loc){
        tts = new TextToSpeech(a, new TextToSpeech.OnInitListener() {

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

    public static void Speak(String text) {
        // TODO Auto-generated method stub
        if(text==null||"".equals(text))
        {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }else {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public static String GetLanguage(){
        return tts.getLanguage().getDisplayLanguage();

    }

    public static void SetLanguage(Locale l){
        tts.setLanguage(l);
    }
}