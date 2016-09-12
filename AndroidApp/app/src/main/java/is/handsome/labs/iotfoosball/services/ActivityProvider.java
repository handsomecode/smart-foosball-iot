package is.handsome.labs.iotfoosball.services;

import android.app.Activity;

public class ActivityProvider {

    private Activity activity;

    public ActivityProvider(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }
}
