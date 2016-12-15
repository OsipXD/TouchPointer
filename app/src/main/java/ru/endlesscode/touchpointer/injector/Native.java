package ru.endlesscode.touchpointer.injector;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
class Native {
    native static void enableDebug(int enable);

    native static int scanFiles();

    native static int openDevice(int id);

    native static int removeDevice(int id);

    native static String getPath(int id);

    native static String getName(int id);

    native static int pollDevice(int id);

    native static int getType();

    native static int getCode();

    native static int getValue();

    native static int sendEvent(int id, int type, int code, int value);
}
