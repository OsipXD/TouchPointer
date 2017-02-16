package ru.endlesscode.touchpointer.injector;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.activity.MainActivity;
import ru.endlesscode.touchpointer.gesture.GesturePoint;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class EventInjector {
    private final static int ID = 42;

    private static final boolean debug;

    static {
        System.loadLibrary("EventInjector");

        // For debug
        debug = false;
        //noinspection ConstantConditions
        Native.enableDebug(debug ? 1 : 0);
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
                        Log.d(MainActivity.TAG, String.format("Event: %d %d %d", type, code, value));
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
                touchDevice = device;
                if (debug) {
                    startMonitoring();
                }

                return true;
            }

            device.close();
        }

        return false;
    }

    public static void release() {
        if (touchDevice != null) {
            touchDevice.close();
            if (debug) {
                stopMonitoring();
            }
        }
    }

    private static void startMonitoring() {
        monitorOn = true;
        deviceMonitor.start();
    }

    private static void stopMonitoring() {
        monitorOn = false;
    }

    public static void sendMotion(GesturePoint gesturePoint, long eventTime, int action) {
        try {
            // Waiting for event time
            while (true) {
                if (SystemClock.uptimeMillis() >= eventTime) {
                    break;
                }
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    touchDevice.sendTouchDown(EventInjector.ID, gesturePoint.getX(), gesturePoint.getY());
                    break;
                case MotionEvent.ACTION_UP:
                    touchDevice.sendTouchUp();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchDevice.sendTouchMove(gesturePoint.getX(), gesturePoint.getY());
            }
        } catch (Exception ignored) {}
    }
}
