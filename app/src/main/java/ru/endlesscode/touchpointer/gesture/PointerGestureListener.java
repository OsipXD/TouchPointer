package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.Config;
import ru.endlesscode.touchpointer.views.Pointer;
import ru.endlesscode.touchpointer.views.TouchArea;

public class PointerGestureListener extends GestureDetector.SimpleOnGestureListener {
    private final Pointer pointer;
    private final TouchArea area;
    private final GestureInjector gestureInjector;

    private MotionEvent doubleTapStartEvent;
    private Gesture savedGesture;

    private int x, y, oldX, oldY;

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
        Log.d("Long Press", "I'm here");
    }

    @Override
    public boolean onContextClick(MotionEvent e) {
        Log.d("Context Click", "I'm here");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(final MotionEvent e) {
        Log.d("Double Tap Event", "I'm here");
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                savedGesture.add(x, y, e.getEventTime() - doubleTapStartEvent.getEventTime());
                gestureInjector.sendGesture(savedGesture);
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

        DisplayMetrics metrics = pointer.getMetrics();
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
        gestureInjector.sendClick(x, y);

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
        area.onDoubleTapped();
        doubleTapStartEvent = e;
        savedGesture = new Gesture(x, y);

        Log.d("Double Tap", "Tapped at: (" + e.getRawX() + "," + e.getRawY() + ")");

        return true;
    }

    public void onDoubleTapDrag(final MotionEvent e) {
        onMove(doubleTapStartEvent, e);
        savedGesture.add(x, y, e.getEventTime() - doubleTapStartEvent.getEventTime());
    }
}
