package is.handsome.labs.iotfoosball.services;

import android.content.Context;
import android.media.SoundPool;

public class SoundService {
    private SoundPool soundPool;
    private int soundId;

    public SoundService (Context context, int soundRef) {
        this.soundPool = new SoundPool.Builder().build();
        this.soundId = soundPool.load(context, soundRef, 1);
    }

    public void soundPlay () {
        soundPool.play(soundId, 1, 1, 1, 0, 1);
    }
}
