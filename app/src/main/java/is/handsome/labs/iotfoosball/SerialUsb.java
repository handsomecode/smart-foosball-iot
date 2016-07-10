package is.handsome.labs.iotfoosball;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
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

public abstract class SerialUsb {

    private static final String TAG = "serial";
    private static final int BAUD_RATE = 115200;

    private UsbSerialPort mUsbSerialPort;
    private SerialInputOutputManager mSerialIOManager;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SerialInputOutputManager.Listener mSerialInputListener = new SerialInputOutputManager.Listener() {

        @Override
        public void onRunError(Exception e) {
            Log.d(TAG, "Runner stopped.");
        }

        @Override
        public void onNewData(byte[] data) {
            SerialUsb.this.onNewData(data);
        }
    };

    public SerialUsb() {
    }

    public abstract void onNewData(byte[] date); //for do smthng with new data

    public void init(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(context, "ÜSB devices Not found", Toast.LENGTH_LONG).show();
        } else {
            UsbSerialDriver driver = availableDrivers.get(0);
            mUsbSerialPort = driver.getPorts().get(0);

            UsbDeviceConnection connection = usbManager.openDevice(mUsbSerialPort.getDriver().getDevice());
            if (connection == null) {
                return;
            }

            try {
                mUsbSerialPort.open(connection);
                mUsbSerialPort.setParameters(BAUD_RATE, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                try {
                    mUsbSerialPort.close();
                } catch (IOException e2) {
                    Log.e(TAG, "Close serial port", e2);
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
                Log.e(TAG, "Close serial port", e);
            }
            mUsbSerialPort = null;
        }
    }

    private void stopIoManager() {
        if (mSerialIOManager != null) {
            Log.i(TAG, "Stopping io manager ..");
            mSerialIOManager.stop();
            mSerialIOManager = null;
        }
    }

    private void startIoManager() {
        if (mUsbSerialPort != null) {
            Log.i(TAG, "Starting io manager ..");
            mSerialIOManager = new SerialInputOutputManager(mUsbSerialPort, mSerialInputListener);
            mExecutor.submit(mSerialIOManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

}
