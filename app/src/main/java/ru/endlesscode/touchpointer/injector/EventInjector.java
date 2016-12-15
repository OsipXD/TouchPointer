package ru.endlesscode.touchpointer.injector;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class EventInjector {
    private final static String LT = "Events";

    static {
        System.loadLibrary("EventInjector");
        Native.enableDebug(1);
    }

    private static InputDevice touchDevice;

    private static boolean monitorOn = false;
    private static Thread deviceMonitor = new Thread(new Runnable() {
        private int lastType = -1;
        private int lastCode = -1;
        private int lastValue = -1;

        @Override
        public void run() {
            while (monitorOn) {
                if (touchDevice.getPollingEvent() == 0) {
                    int type = touchDevice.getPollingType();
                    int code = touchDevice.getPollingCode();
                    int value = touchDevice.getPollingValue();

                    if (type != this.lastType || code != lastCode || value != lastValue) {
                        this.lastType = type;
                        this.lastCode = code;
                        this.lastValue = value;
                        Log.d(LT, String.format("Event: %d %d %d", type, code, value));
                    }
                }
            }
        }
    });

    private EventInjector() {}

    public static boolean init() {
        int count = Native.scanFiles();

        for (int i = 0; i < count; i++) {
            InputDevice device = new InputDevice(i, Native.getPath(i));
            device.open(true);

            if (device.getName().contains("touch")) {
                Log.d(LT, "Touch device:" + device.getPath() + " Name:" + device.getName() + " opened");
                touchDevice = device;
                startMonitoring();

                return true;
            }

            device.close();
        }

        return false;
    }

    public static void release() {
        if (touchDevice != null) {
            touchDevice.close();
            stopMonitoring();
        }
    }

    public static void sendTouch(long eventTime, int action, int x, int y) {
        try {
            while (true) {
                if (SystemClock.uptimeMillis() >= eventTime) {
                    break;
                }
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touchDevice.sendTouchDown(42, x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    touchDevice.sendTouchUp(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchDevice.sendTouchMove(x, y);
            }
        } catch (Exception ignored) {}
    }

    public static void startMonitoring() {
        monitorOn = true;
        deviceMonitor.start();
    }

    public static void stopMonitoring() {
        monitorOn = false;
    }
}
