package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.List;

public class SerialUsbFoosball {

    private static final String TAG = "serial";
    private static final int SIDE_A = 48;
    private static final int SIDE_B = 49;
    private static final int BAUD_RATE = 115200;

    public SerialUsbFoosball(Context context) {
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
    }

}
