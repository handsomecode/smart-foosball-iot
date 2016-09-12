package is.handsome.labs.iotfoosball.interactor;

import is.handsome.labs.iotfoosball.services.SoundService;
import timber.log.Timber;

class PhraseSpotterForCountStart extends PhraseSpotter {

    private SoundService soundService;

    public PhraseSpotterForCountStart(SoundService soundService) {
        this.soundService = soundService;
    }

    public void onPhraseSpotted(String s, int i) {
        Timber.d("LET'S GO !!!!! ");
        soundService.soundPlay();
    }
}