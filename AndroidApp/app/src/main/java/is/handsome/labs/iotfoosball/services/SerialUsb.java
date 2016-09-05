package is.handsome.labs.iotfoosball.services;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import timber.log.Timber;

public class SerialUsb {

    private class WorkerThreadFactory implements ThreadFactory {
        private int counter = 0;
        private String prefix = "";

        public WorkerThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, prefix + "-" + counter++);
        }
    }

    private static final int BAUD_RATE = 115200;

    private UsbSerialPort usbSerialPort;
    private SerialInputOutputManager serialIOManager;
    private ExecutorService executor;
    private SerialInputOutputManager.Listener serialInputListener;

    public SerialUsb(final Handler handler) {
        WorkerThreadFactory threadFactory = new WorkerThreadFactory("myThread");
        executor = Executors.newSingleThreadExecutor(threadFactory);
        serialInputListener = new SerialInputOutputManager.Listener() {
            //TODO check this place for fitting guidelines
            private
            String serialMessage = "";

            @Override
            public void onRunError(Exception e) {
                Timber.d("Runner stopped");
            }

            @Override
            public void onNewData(byte[] data) {
                for (byte aData : data) {
                    if ((int)aData != 13 && (int)aData != 10) {
                        serialMessage += Character.toString((char) aData);
                        handler.sendEmptyMessage((int)aData);
                    }
                    if ((int)aData == 10) {
                        Timber.d("Serial port message = " + serialMessage);
                        serialMessage = "";
                    }
                }
            }
        };
    }

    public void init(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers =
                UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);
        if (availableDrivers.isEmpty()) {
            Toast.makeText(context, "ÜSB devices Not found", Toast.LENGTH_LONG).show();
        } else {
            UsbSerialDriver driver = availableDrivers.get(0);
            usbSerialPort = driver.getPorts().get(0);

            UsbDeviceConnection connection =
                    usbManager.openDevice(usbSerialPort.getDriver().getDevice());
            if (connection == null) {
                return;
            }

            try {
                usbSerialPort.open(connection);
                usbSerialPort.setParameters(BAUD_RATE,
                        UsbSerialPort.DATABITS_8,
                        UsbSerialPort.STOPBITS_1,
                        UsbSerialPort.PARITY_NONE);
                Toast.makeText(context, "ÜSB device have been found", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Timber.d(e, "Error setting up device");
                try {
                    usbSerialPort.close();
                } catch (IOException e2) {
                    Timber.d(e2, "Close serial port");
                }
                usbSerialPort = null;
                return;
            }
            onDeviceStateChange();
        }
    }

    public void close() {
        stopIoManager();
        if (usbSerialPort != null) {
            try {
                usbSerialPort.close();
            } catch (IOException e) {
                Timber.d(e, "Close serial port");
            }
            usbSerialPort = null;
        }
    }

    private void stopIoManager() {
        if (serialIOManager != null) {
            Timber.d("Stopping io manager ..");
            serialIOManager.stop();
            serialIOManager = null;
        }
    }

    private void startIoManager() {
        if (usbSerialPort != null) {
            Timber.d("Starting io manager ..");
            serialIOManager = new SerialInputOutputManager(usbSerialPort, serialInputListener);
            executor.submit(serialIOManager);
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }
}
