package is.handsome.labs.iotfoosball.interactor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import ru.yandex.speechkit.Error;
import ru.yandex.speechkit.PhraseSpotterListener;
import ru.yandex.speechkit.PhraseSpotterModel;
import ru.yandex.speechkit.SpeechKit;
import timber.log.Timber;

abstract class PhraseSpotter implements PhraseSpotterListener {

    private Context context;

    @Override
    public void onPhraseSpotterStarted() {
        startPhraseSpotter(context);
        Timber.d("PhraseSpotter started");
    }

    @Override
    public void onPhraseSpotterStopped() {
        Error stopResult = ru.yandex.speechkit.PhraseSpotter.stop();
        handleError(stopResult);
        Timber.d("PhraseSpotter stopped");
    }

    @Override
    public void onPhraseSpotterError(Error error) {
        handleError(error);
    }

    public void init(Context context, String yandexApi) {
        this.context = context;
        SpeechKit.getInstance().configure(context, yandexApi);
        PhraseSpotterModel model = new PhraseSpotterModel("phrase-spotter/commands");
        Error loadResult = model.load();
        if (loadResult.getCode() != Error.ERROR_OK) {
            Timber.d("Error occurred during model loading: %s", loadResult.getString());
        } else {
            // Set the listener.
            ru.yandex.speechkit.PhraseSpotter.setListener(this);
            // Set the model.
            Error setModelResult = ru.yandex.speechkit.PhraseSpotter.setModel(model);
            //Timber.d("Model has been loaded");
            handleError(setModelResult);
        }
    }

    public void onStart(Context context) {
        // Don't forget to call start.
        startPhraseSpotter(context);
    }

    public void onStop() {
        Error stopResult = ru.yandex.speechkit.PhraseSpotter.stop();
        handleError(stopResult);
    }

    private void handleError(Error error) {
        if (error.getCode() != Error.ERROR_OK) {
            Timber.d("Error occurred: %s", error.getString());
        }
    }

    private void startPhraseSpotter(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            Timber.d("Permission to record audio wasn't granted");
            return;
        }
        Error startResult = ru.yandex.speechkit.PhraseSpotter.start();
        handleError(startResult);
    }
}
