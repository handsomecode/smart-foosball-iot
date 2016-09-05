package is.handsome.labs.iotfoosball.services;

import android.content.Context;
import android.os.Handler;


public class UsbService {

    private SerialUsb serialUsb;
    private Context context;

    public UsbService(Context context, Handler handler) {
        this.context = context;
        this.serialUsb = new SerialUsb(handler);
    }

    public void onResume() {
        serialUsb.init(context);
    }

    public void onPause() {
        serialUsb.close();
    }
}
