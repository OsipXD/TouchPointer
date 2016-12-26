package ru.endlesscode.touchpointer.gesture;

import ru.endlesscode.touchpointer.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
class Gesture {
    private final List<GesturePoint> way;
    private final long startTime;
    private final float gestureSpeed;

    Gesture(long startTime, int x, int y, int rotation) {
        this.startTime = startTime;
        way = new ArrayList<>();
        way.add(new GesturePoint(x, y, rotation));

        this.gestureSpeed = Config.getGestureMultiplier();
    }

    void add(long time, int x, int y, int rotation) {
        way.add(new GesturePoint(x, y, rotation, (long) ((time - this.startTime) / this.gestureSpeed)));
    }

    List<GesturePoint> getWay() {
        return way;
    }

    int getWaySize() {
        return way.size();
    }
}
