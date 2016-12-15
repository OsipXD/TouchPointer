package ru.endlesscode.touchpointer.gesture;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
class GesturePoint {
    final int x;
    final int y;
    final long timeOffset;

    GesturePoint(int x, int y, long timeOffset) {
        this.x = x;
        this.y = y;
        this.timeOffset = timeOffset;
    }
}
