package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.util.Config;
import ru.endlesscode.touchpointer.util.DisplayUtil;
import ru.endlesscode.touchpointer.views.Pointer;
import ru.endlesscode.touchpointer.views.TouchArea;

public class PointerGestureListener extends GestureDetector.SimpleOnGestureListener {
    private final Pointer pointer;
    private final TouchArea area;
    private final GestureInjector gestureInjector;

    private Gesture savedGesture;

    private int x, y, oldX, oldY;
    private MotionEvent dragStartEvent;

    public PointerGestureListener(TouchArea area, Pointer pointer) {
        this.area = area;
        this.pointer = pointer;
        this.gestureInjector = new GestureInjector(area);

        x = pointer.getPointerX();
        y = pointer.getPointerY();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        oldX = x;
        oldY = y;

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(final MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                area.setDoubleTapped(false);
                savedGesture.add(e.getEventTime(), x, y, DisplayUtil.getDisplayRotation());

                if (savedGesture.getWaySize() == 3) {
                    gestureInjector.doubleTap(new GesturePoint(x, y, DisplayUtil.getDisplayRotation()));
                } else {
                    gestureInjector.gesture(savedGesture);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                onDoubleTapDrag(e);
                break;
        }

        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        onMove(e1, e2);

        return true;
    }

    private void onMove(MotionEvent from, MotionEvent to) {
        int x = (int) (this.oldX + (to.getRawX() - from.getRawX()) * Config.getSpeedMultiplier());
        int y = (int) (this.oldY + (to.getRawY() - from.getRawY()) * Config.getSpeedMultiplier());

        DisplayMetrics metrics = DisplayUtil.getMetrics();
        if (x < 0) {
            this.oldX += -x;
            x = 0;
        } else if (x > metrics.widthPixels) {
            this.oldX -= (x - metrics.widthPixels);
            x = metrics.widthPixels;
        }

        if (y < 0) {
            this.oldY += -y;
            y = 0;
        } else if (y > metrics.heightPixels) {
            this.oldY -= (y - metrics.heightPixels);
            y = metrics.heightPixels;
        }

        this.x = x;
        this.y = y;

        pointer.setPointerPosition(x, y);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        gestureInjector.tap(new GesturePoint(x, y, DisplayUtil.getDisplayRotation()));

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        if (!area.isDoubleTapped()) {
            area.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            area.onLongPress();
        }
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        area.setDoubleTapped(true);
        area.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        dragStartEvent = e;
        savedGesture = new Gesture(e.getEventTime(), x, y, DisplayUtil.getDisplayRotation());

        return true;
    }

    public void onDoubleTapDrag(final MotionEvent e) {
        onMove(dragStartEvent, e);
        savedGesture.add(e.getEventTime(), x, y, DisplayUtil.getDisplayRotation());
    }

    public void onLongPressUp(MotionEvent e) {
        gestureInjector.longTap(new GesturePoint(x, y, DisplayUtil.getDisplayRotation()));
    }
}
