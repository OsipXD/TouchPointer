package ru.endlesscode.touchpointer.gesture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Gesture {
    private final GesturePoint start;
    private final List<GesturePoint> way;
    private GesturePoint end;

    public Gesture(int x, int y) {
        this.start = this.end = new GesturePoint(x, y, 0);
        way = new ArrayList<>();
    }

    public void add(int x, int y, long timeOffset) {
        GesturePoint point = new GesturePoint(x, y, timeOffset);
        way.add(end);
        end = point;
    }

    public GesturePoint getStart() {
        return start;
    }

    public List<GesturePoint> getWay() {
        return way;
    }

    public GesturePoint getEnd() {
        return end;
    }
}
