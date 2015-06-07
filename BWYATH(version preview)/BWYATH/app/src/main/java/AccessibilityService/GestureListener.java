package AccessibilityService;

/**
 * Created by Huunneki on 05/05/2015.
 */
public interface GestureListener {
    public void didReceiveNotificationForGesture(GestureService.Gesture g);
    public void didReceiveYesNoChange(int status);
    public void didReceiveBackChange(int status);
    public void didReceiveShakeChange(int status);
    public void didReceiveValidation(int status);
}
