package AccessibilityService;

/**
 * Created by Cnam on 05/05/2015.
 */
public interface GestureListener {

    /***
     * Cette fonction est appelée dès qu'un mouvement est détecté
     * @param g - Le mouvement détecté
     */
    public void didReceiveNotificationForGesture(GestureService.Gesture g);

    /***
     * Méthode appelée dès qu'un Oui / Non est détecté
     * @param status - L'état du mouvement
     */
    public void didReceiveYesNoChange(int status);

    /***
     * Méthode appelée dès qu'un mouvement arrière est détecté
     * @param status - L'état du mouvement
     */
    public void didReceiveBackChange(int status);

    /***
     * Méthode appelée dès qu'une secousse est détectée
     * @param status - L'état du mouvement
     */
    public void didReceiveShakeChange(int status);

    /***
     * Méthode appelée dès qu'une validation est détectée
     * @param status - L'état du mouvement
     */
    public void didReceiveValidation(int status);
}
