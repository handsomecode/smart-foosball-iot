package is.handsome.labs.iotfoosball.interactor;

import android.os.Handler;
import android.os.Message;

import timber.log.Timber;

import static is.handsome.labs.iotfoosball.view.MainActivity.A;
import static is.handsome.labs.iotfoosball.view.MainActivity.B;

class SerialHandler extends Handler {

    private static volatile SerialHandler instance;

    private CurrentGame currentGame;

    public static SerialHandler getInstance(CurrentGame currentGame) {
        SerialHandler localInstance = instance;
        if(localInstance == null) {
            synchronized (SerialHandler.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new SerialHandler(currentGame);
                }
            }
        }
        return localInstance;
    }

    private SerialHandler(CurrentGame currentGame) {
        this.currentGame = currentGame;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == 'a') {
            currentGame.notifyScored(A);
            Timber.d("Serial port message = GOAL in A");
        }
        if (msg.what == 'b') {
            currentGame.notifyScored(B);
            Timber.d("Serial port message = GOAL in B");
        }
    }
}