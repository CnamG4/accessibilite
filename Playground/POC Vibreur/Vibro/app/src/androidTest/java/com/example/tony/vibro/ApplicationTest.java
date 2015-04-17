package com.example.tony.vibro;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.test.ApplicationTestCase;
import android.view.View;
import android.widget.Button;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    void cancelled(){
        Vibrator v = (Vibrator)this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
        try {
            wait(100);
        }
        catch (Exception ex){
            System.out.println("Unable to wait + " + ex);
        }
        v.vibrate(200);
    }

}

