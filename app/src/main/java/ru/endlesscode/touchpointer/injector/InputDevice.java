package ru.endlesscode.touchpointer.injector;

import android.util.Log;

/**
 * Created by OsipXD on 15.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class InputDevice {
    private final static String LT = "Events";

    private static final int EV_SYN = 0x00;
    private static final int EV_KEY = 0x01;
    private static final int EV_ABS = 0x03;

    private static final int REL_X = 0x00;
    private static final int REL_Y = 0x01;

    private static final int SYN_REPORT = 0x00;
    private static final int SYN_MT_REPORT = 0x02;

    // Reports the cross-sectional area of the touch contact, or the length of the longer dimension of the touch contact.
    private static final int ABS_MT_TOUCH_MAJOR     = 48;
    // Reports the X coordinate of the tool.
    private static final int ABS_MT_POSITION_X      = 53;
    // Reports the Y coordinate of the tool.
    private static final int ABS_MT_POSITION_Y      = 54;
    // ID of the touch (important for multi-touch reports)
    private static final int ABS_MT_TRACKING_ID     = 57;
    // Reports the physical pressure applied to the tip of the tool or the signal strength of the touch contact.
    private static final int ABS_MT_PRESSURE        = 58;

    // Indicates whether the tool is touching the device.
    private static final int BTN_TOUCH = 0x14a;

    private int id;
    private String path;
    private String name;
    private boolean opened;

    InputDevice(int id, String path) {
        this.id = id;
        this.path = path;
    }

    private int sendEvent(int type, int code, int value) {
        return Native.sendEvent(this.id, type, code, value);
    }

    public int getPollingEvent() {
        return Native.pollDevice(id);
    }

    public int getPollingType() {
        return Native.getType();
    }

    public int getPollingCode() {
        return Native.getCode();
    }

    public int getPollingValue() {
        return Native.getValue();
    }

    public boolean isOpened() {
        return opened;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    void close() {
        opened = false;
        Native.removeDevice(id);
    }

    public void sendTouchDown(int id, int x, int y) {
        sendEvent(EV_ABS, ABS_MT_TRACKING_ID, id);
        sendEvent(EV_ABS, ABS_MT_POSITION_X, x);
        sendEvent(EV_ABS, ABS_MT_POSITION_Y, y);
        sendEvent(EV_ABS, ABS_MT_TOUCH_MAJOR, 5);
        sendEvent(EV_ABS, ABS_MT_PRESSURE, 50);
        sendSynReport();
    }

    public void sendTouchUp(int x, int y) {
        sendEvent(EV_ABS, ABS_MT_TRACKING_ID, -1);
        sendSynReport();
    }

    public void sendTouchMove(int x, int y) {
        sendEvent(EV_ABS, ABS_MT_POSITION_X, x);
        sendEvent(EV_ABS, ABS_MT_POSITION_Y, y);
        sendSynReport();
    }

    private void sendSynReport() {
        sendEvent(EV_SYN, SYN_REPORT, 0);
    }

    /**
     * function open : opens an input event node
     *
     * @param forceOpen will try to set permissions and then reopen if first open attempt fails
     * @return true if input event node has been opened
     */
    public boolean open(boolean forceOpen) {
        int res = Native.openDevice(id);

        // if opening fails, we might not have the correct permissions, try changing 660 to 666
        if (res != 0 && forceOpen && ShellInterface.isSuAvailable()) {
            ShellInterface.runCommand("chmod 666 " + path);
            res = Native.openDevice(id);
        }

        name = Native.getName(id);
        opened = (res == 0);
        Log.d(LT, "Open:" + path + " Name:" + name + " Result:" + opened);

        return opened;
    }
}