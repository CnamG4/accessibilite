package com.example.huunneki.helloworld;

/**
 * Created by Huunneki on 05/05/2015.
 */
public interface GestureCallbackInterface {
    public void didReceiveYesNoChange(int status);
    public void didReceiveBackChange(int status);
    public void didReceiveForwardChange(int status);
}
