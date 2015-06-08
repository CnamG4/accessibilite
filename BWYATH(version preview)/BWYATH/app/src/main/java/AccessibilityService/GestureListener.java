package AccessibilityService;

/**
 * Created by Cnam on 05/05/2015.
 */
public interface GestureListener {

    /***
     * Cette fonction est appel�e d�s qu'un mouvement est d�tect�
     * @param g - Le mouvement d�tect�
     */
    public void didReceiveNotificationForGesture(GestureService.Gesture g);

    /***
     * M�thode appel�e d�s qu'un Oui / Non est d�tect�
     * @param status - L'�tat du mouvement
     */
    public void didReceiveYesNoChange(int status);

    /***
     * M�thode appel�e d�s qu'un mouvement arri�re est d�tect�
     * @param status - L'�tat du mouvement
     */
    public void didReceiveBackChange(int status);

    /***
     * M�thode appel�e d�s qu'une secousse est d�tect�e
     * @param status - L'�tat du mouvement
     */
    public void didReceiveShakeChange(int status);

    /***
     * M�thode appel�e d�s qu'une validation est d�tect�e
     * @param status - L'�tat du mouvement
     */
    public void didReceiveValidation(int status);
}
