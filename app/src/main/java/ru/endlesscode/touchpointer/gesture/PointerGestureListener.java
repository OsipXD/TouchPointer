package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.Config;
import ru.endlesscode.touchpointer.util.WindowManagerUtil;
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
    public void onLongPress(MotionEvent e) {
        this.area.onLongPress();
        Log.d("Long Press", "I'm here");
    }

    @Override
    public boolean onContextClick(MotionEvent e) {
        Log.d("Context Click", "I'm here");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(final MotionEvent e) {
        Log.d("Double Tap Event", e.getRawX() + ", " + e.getRawY());
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                area.setDoubleTapped(false);
                savedGesture.add(e.getEventTime(), x, y, WindowManagerUtil.getDisplayRotation());

                if (savedGesture.getWaySize() == 3) {
                    gestureInjector.doubleTap(new GesturePoint(x, y, WindowManagerUtil.getDisplayRotation()));
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("Fling", "I'm here");
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.d("Scroll", "I'm here");
        onMove(e1, e2);

        return true;
    }

    private void onMove(MotionEvent from, MotionEvent to) {
        int x = (int) (oldX + (to.getRawX() - from.getRawX()) * Config.getSpeedMultiplier());
        int y = (int) (oldY + (to.getRawY() - from.getRawY()) * Config.getSpeedMultiplier());

        DisplayMetrics metrics = WindowManagerUtil.getMetrics();
        if (x < 0) {
            x = 0;
        } else if (x > metrics.widthPixels) {
            x = metrics.widthPixels;
        }

        if (y < 0) {
            y = 0;
        } else if (y > metrics.heightPixels) {
            y = metrics.heightPixels;
        }

        this.x = x;
        this.y = y;

        pointer.setPointerPosition(x, y);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        gestureInjector.tap(new GesturePoint(x, y, WindowManagerUtil.getDisplayRotation()));

        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.d("Single Tap Up", "I'm here");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.d("Show Pres", "I'm here");
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        area.setDoubleTapped(true);
        dragStartEvent = e;
        savedGesture = new Gesture(e.getEventTime(), x, y, WindowManagerUtil.getDisplayRotation());

        Log.d("Double Tap", "Tapped at: (" + e.getRawX() + "," + e.getRawY() + ")");

        return true;
    }

    public void onDoubleTapDrag(final MotionEvent e) {
        onMove(dragStartEvent, e);
        savedGesture.add(e.getEventTime(), x, y, WindowManagerUtil.getDisplayRotation());
    }

    public void onLongPressUp(MotionEvent e) {
        gestureInjector.longTap(new GesturePoint(x, y, WindowManagerUtil.getDisplayRotation()));
    }
}
