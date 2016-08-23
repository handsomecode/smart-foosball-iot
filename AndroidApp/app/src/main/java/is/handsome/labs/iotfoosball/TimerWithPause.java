package is.handsome.labs.iotfoosball;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import timber.log.Timber;

/**
 * Based on http://stackoverflow.com/a/9802426/4891132
 * Schedule a countdown until a time in the future, with
 * regular notifications on intervals along the way.
 * <p/>
 * The calls to {@link #onTick(long)} are synchronized to this object so that
 * one call to {@link #onTick(long)} won't ever occur before the previous
 * callback is complete.  This is only relevant when the implementation of
 * {@link #onTick(long)} takes an amount of time to execute that is significant
 * compared to the countdown interval.
 */

public abstract class TimerWithPause {

    /**
     * Real time while timer was paused
     */
    private long mPausedTime;

    /**
     * The interval in millis that the user receives callbacks
     */
    private final long mRefreshInterval;

    /**
     * The time on the timer when it was paused, if it is currently paused; 0 otherwise.
     */
    private long mPauseTime;

    private boolean mIsPaused;

    /**
     * True if timer was started running, false if not.
     */
    private boolean mRunAtStart;

    /**
     * @param refreshInterval The interval in millis at which to execute
     *                          {@link #onTick(long timeOnTimer)} callbacks
     * @param runAtStart        True if timer should start running, false if not
     */
    public TimerWithPause(long refreshInterval, boolean runAtStart) {
        Timber.d("timer - create");
        mRefreshInterval = refreshInterval;
        mRunAtStart = runAtStart;
        mPausedTime = 0;
        mPauseTime = 0;
        mIsPaused = true;
    }

    /**
     * Cancel the ticking and clears all remaining messages
     */
    public final void cancel() {
        Timber.d("timer - cancel");
        mHandler.removeMessages(MSG);
    }


    /**
     * Pauses the counter.
     */
    public void pause() {
        Timber.d("timer - pause");
        if (isRunning()) {
            mIsPaused = true;
            mPauseTime = SystemClock.elapsedRealtime();
            cancel();
        }
    }

    /**
     * Resumes the counter.
     */
    public void resume() {
        Timber.d("timer - resume");
        if (isPaused()) {
            mPausedTime = mPausedTime + (SystemClock.elapsedRealtime() - mPauseTime);
            mHandler.sendMessage(mHandler.obtainMessage(MSG));
            mIsPaused = false;
        }
    }

    /**
     * Tests whether the timer is paused.
     *
     * @return true if the timer is currently paused, false otherwise.
     */
    public boolean isPaused() {
        return mIsPaused;
    }

    /**
     * Tests whether the timer is running. (Performs logical negation on {@link #isPaused()})
     *
     * @return true if the timer is currently running, false otherwise.
     */
    public boolean isRunning() {
        return (!isPaused());
    }

    /**
     * Returns the number of milliseconds remaining until the timer is finished
     *
     * @return number of milliseconds remaining until the timer is finished
     */
    public long timeOn() {
        Timber.d("timer - timeOn");
        long timeOnTimer;
        if (isPaused()) {
            timeOnTimer = mPauseTime;
        } else {
            timeOnTimer = SystemClock.elapsedRealtime() - mPausedTime;
        }
        return timeOnTimer;
    }

    /**
     * Callback fired on regular interval
     *
     * @param timeOnTimer The amount of time until finished
     */
    public abstract void onTick(long timeOnTimer);

    private static final int MSG = 1;


    // handles counting down
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (TimerWithPause.this) {
                long millisOn = timeOn();
                millisOn = correctTime(millisOn);

                if (millisOn < mRefreshInterval) {
                    // no tick, just delay until done
                    sendMessageDelayed(obtainMessage(MSG), millisOn);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onTick(millisOn);

                    // take into account user's onTick taking time to execute
                    long delay = mRefreshInterval - (SystemClock.elapsedRealtime() - lastTickStart);

                    // special case: user's onTick took more than mRefreshInterval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mRefreshInterval;

                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    private long correctTime(long time) {
        long tempTime = (time / mRefreshInterval) * mRefreshInterval;
        long mod = time - tempTime;
        return mod < (mRefreshInterval / 2) ? tempTime : tempTime + mRefreshInterval;
    }

    public void reset() {
        pause();
        mPausedTime = 0;
        mPauseTime = 0;
    }

}
