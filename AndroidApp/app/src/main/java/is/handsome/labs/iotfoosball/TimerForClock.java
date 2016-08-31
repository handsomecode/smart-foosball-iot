package is.handsome.labs.iotfoosball;

import java.util.Locale;

public class TimerForClock extends TimerWithPause {
    private FeedbackFromPresenterActivity feedbackFromPresenterActivity;
    private long updateInterval;

    public TimerForClock(FeedbackFromPresenterActivity feedbackFromPresenterActivity,
            long refreshInterval,
            boolean runAtStart) {
        super(refreshInterval, runAtStart);
        this.feedbackFromPresenterActivity = feedbackFromPresenterActivity;
        updateInterval = refreshInterval;
    }

    @Override
    public void onTick(long timeOnTimer) {
        updateTimer(timeOnTimer);
    }

    @Override
    public void reset() {
        super.reset();
        feedbackFromPresenterActivity.setTime("00:00");
    }

    private void updateTimer(long millisOn) {
        long minutes = millisOn / (60 * updateInterval);
        long seconds = (millisOn / updateInterval) % 60;
        feedbackFromPresenterActivity.setTime(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
}