package is.handsome.labs.iotfoosball;

import timber.log.Timber;

public class PhraseSpotterForCountStart extends PhraseSpotter {

    private SoundService soundService;

    public PhraseSpotterForCountStart(SoundService soundService) {
        this.soundService = soundService;
    }

    public void onPhraseSpotted(String s, int i) {
        Timber.d("LET'S GO !!!!! ");
        soundService.soundPlay();
    }
}