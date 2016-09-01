package is.handsome.labs.iotfoosball;

import java.util.Locale;

public class TimerForClock extends TimerWithPause {
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private long updateInterval;

    public TimerForClock(InterfacePresentorFromInteractor interfacePresentorFromInteractor,
            long refreshInterval,
            boolean runAtStart) {
        super(refreshInterval, runAtStart);
        this.interfacePresentorFromInteractor = interfacePresentorFromInteractor;
        updateInterval = refreshInterval;
    }

    @Override
    public void onTick(long timeOnTimer) {
        updateTimer(timeOnTimer);
    }

    @Override
    public void reset() {
        super.reset();
        interfacePresentorFromInteractor.setTime("00:00");
    }

    private void updateTimer(long millisOn) {
        long minutes = millisOn / (60 * updateInterval);
        long seconds = (millisOn / updateInterval) % 60;
        interfacePresentorFromInteractor.setTime(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
    }
}