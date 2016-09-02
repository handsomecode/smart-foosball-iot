package is.handsome.labs.iotfoosball.interactor;

import java.util.Locale;

import is.handsome.labs.iotfoosball.presenter.InterfacePresentorFromInteractor;

class TimerForClock extends TimerWithPause {
    private InterfacePresentorFromInteractor interfacePresentorFromInteractor;
    private long updateInterval;

    TimerForClock(InterfacePresentorFromInteractor interfacePresentorFromInteractor,
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
        interfacePresentorFromInteractor.setTime(String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds));
    }
}