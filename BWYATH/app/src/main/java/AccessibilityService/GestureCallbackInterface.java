package AccessibilityService;

import AccessibilityService.AccessGestureRecognizer;

/**
 * Created by Huunneki on 05/05/2015.
 */
public interface GestureCallbackInterface {
    public void didReceiveNotificationForGesture(AccessGestureRecognizer.Gesture g);
    public void didReceiveYesNoChange(int status);
    public void didReceiveBackChange(int status);
    public void didReceiveShakeChange(int status);
    public void didReceiveValidation(int status);
}
