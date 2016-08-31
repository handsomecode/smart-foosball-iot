package is.handsome.labs.iotfoosball;

import android.widget.Button;

import java.util.Locale;

public class TimerForClock extends TimerWithPause {
    private ActivityWithPresenter activityWithPresenter;
    private long mUpdateInterval;

    public TimerForClock(ActivityWithPresenter activityWithPresenter,
            long refreshInterval,
            boolean runAtStart) {
        super(refreshInterval, runAtStart);
        this.activityWithPresenter = activityWithPresenter;
        mUpdateInterval = refreshInterval;
    }

    @Override
    public void onTick(long timeOnTimer) {
        updateTimer(timeOnTimer);
    }

    @Override
    public void reset() {
        super.reset();
        activityWithPresenter.setTime("00:00");
    }

    private void updateTimer(long millisOn) {
        long minutes = millisOn / (60 * mUpdateInterval);
        long seconds = (millisOn / mUpdateInterval) % 60;
        activityWithPresenter.setTime(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
}