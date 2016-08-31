package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import timber.log.Timber;

import static is.handsome.labs.iotfoosball.MainActivity.A;
import static is.handsome.labs.iotfoosball.MainActivity.B;

public class UsbService {

    private static SerialHandler sSerialHandler;
    private SerialUsb serialUsb;
    private Context context;

    public UsbService(Context context, CurrentGame currentGame) {
        sSerialHandler = new SerialHandler(currentGame);
        this.context = context;
        this.serialUsb = new SerialUsb(sSerialHandler);
    }

    public void onResume() {
        serialUsb.init(context);
    }

    public void onPause() {
        serialUsb.close();
    }

    private class SerialHandler extends Handler {
        private CurrentGame currentGame;

        public SerialHandler(CurrentGame currentGame) {
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
}
