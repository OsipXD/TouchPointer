package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.view.Surface;
import ru.endlesscode.touchpointer.util.DisplayUtil;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class GesturePoint {
    private final int x;
    private final int y;
    private final long timeOffset;

    GesturePoint(int x, int y, int rotation) {
        this(x, y, rotation, 0);
    }

    GesturePoint(int x, int y, int rotation, long timeOffset) {
        DisplayMetrics metrics = DisplayUtil.getMetrics();
        switch (rotation) {
            case Surface.ROTATION_90:
                this.x = metrics.heightPixels - y;
                //noinspection SuspiciousNameCombination
                this.y = x;
                break;
            case Surface.ROTATION_180:
                this.x = metrics.widthPixels - x;
                this.y = metrics.heightPixels - y;
                break;
            case Surface.ROTATION_270:
                //noinspection SuspiciousNameCombination
                this.x = y;
                this.y = metrics.widthPixels - x;
                break;
            default:
                this.x = x;
                this.y = y;
                break;
        }

        this.timeOffset = timeOffset;
    }

    long getTimeOffset() {
        return timeOffset;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }
}
