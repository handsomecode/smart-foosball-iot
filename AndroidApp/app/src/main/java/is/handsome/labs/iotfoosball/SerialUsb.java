package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import timber.log.Timber;

public class SerialUsb {

    private static final int BAUD_RATE = 115200;

    private Handler mHandler;
    private UsbSerialPort mUsbSerialPort;
    private SerialInputOutputManager mSerialIOManager;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager.Listener mSerialInputListener =
            new SerialInputOutputManager.Listener() {
    //TODO check this place for fitting guidelines

        @Override
        public void onRunError(Exception e) {
            Timber.d("Runner stopped");
        }

        @Override
        public void onNewData(byte[] data) {
            int k;
            for (byte d: data) {
                k = d;
                mHandler.sendEmptyMessage(k);
            }
        }
    };

    public SerialUsb(Context context, Handler handler) {
        init(context);
        mHandler = handler;
    }

    public void init(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers =
                UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(context, "ÜSB devices Not found", Toast.LENGTH_LONG).show();
        } else {
            UsbSerialDriver driver = availableDrivers.get(0);
            mUsbSerialPort = driver.getPorts().get(0);

            UsbDeviceConnection connection =
                    usbManager.openDevice(mUsbSerialPort.getDriver().getDevice());
            if (connection == null) {
                return;
            }

            try {
                mUsbSerialPort.open(connection);
                mUsbSerialPort.setParameters(BAUD_RATE,
                        UsbSerialPort.DATABITS_8,
                        UsbSerialPort.STOPBITS_1,
                        UsbSerialPort.PARITY_NONE);
                Toast.makeText(context, "ÜSB device have been found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Timber.d(e, "Error setting up device");
                try {
                    mUsbSerialPort.close();
                } catch (IOException e2) {
                    Timber.d(e2, "Close serial port");
                }
                mUsbSerialPort = null;
                return;
            }
            onDeviceStateChange();
        }
    }

    public void close() {
        stopIoManager();
        if (mUsbSerialPort != null) {
            try {
                mUsbSerialPort.close();
            } catch (IOException e) {
                Timber.d(e, "Close serial port");
            }
            mUsbSerialPort = null;
        }
    }

    private void stopIoManager() {
        if (mSerialIOManager != null) {
            Timber.d("Stopping io manager ..");
            mSerialIOManager.stop();
            mSerialIOManager = null;
        }
    }

    private void startIoManager() {
        if (mUsbSerialPort != null) {
            Timber.d("Starting io manager ..");
            mSerialIOManager = new SerialInputOutputManager(mUsbSerialPort, mSerialInputListener);
            mExecutor.submit(mSerialIOManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

}
