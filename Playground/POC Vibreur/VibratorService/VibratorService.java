import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;


/**
 * Created by tony on 17/04/2015.
 */
public class VibratorService {
    /**
     * Created by tony on 17/04/2015.
     */
    private Activity activity;
    public VibratorService(Activity a){
        this.activity = a;
    }

    public void cancelled(){
        Vibrator v = (Vibrator) activity.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

    public void validate(){
        Vibrator v = (Vibrator) activity.getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
        long [] pattern = { 0, 100, 100, 100};
        v.vibrate(pattern, -1);
    }
}
