package ru.endlesscode.touchpointer.gesture;

import android.os.AsyncTask;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import ru.endlesscode.touchpointer.Config;
import ru.endlesscode.touchpointer.RootExecutor;
import ru.endlesscode.touchpointer.views.Pointer;
import ru.endlesscode.touchpointer.views.TouchPointerLayout;

public class PointerGestureListener extends GestureDetector.SimpleOnGestureListener {
    private final Pointer pointer;
    private TouchPointerLayout layout;

    private Clicker clicker;
    private int x, y, oldX, oldY;

    public PointerGestureListener(TouchPointerLayout layout) {
        this.layout = layout;
        this.pointer = layout.getPointer();

        x = pointer.getPointerX();
        y = pointer.getPointerY();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("Down", "I'm here");
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
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("Double Tap Event", "I'm here");
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
        int x = (int) (oldX + (e2.getRawX() - e1.getRawX()) * Config.getSpeedMultiplier());
        int y = (int) (oldY + (e2.getRawY() - e1.getRawY()) * Config.getSpeedMultiplier());

        RelativeLayout parent = (RelativeLayout) pointer.getParent();
        if (x < 0) {
            x = 0;
        } else if (x > parent.getWidth()) {
            x = parent.getWidth();
        }

        if (y < 0) {
            y = 0;
        } else if (y > parent.getHeight()) {
            y = parent.getHeight();
        }

        this.x = x;
        this.y = y;

        pointer.setPointerPosition(x, y);
        layout.update(x, y);

        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Log.d("Single Tap Confirmed", "I'm here");
        if (clicker == null || clicker.getStatus() != AsyncTask.Status.RUNNING) {
            clicker = new Clicker(x, y);
            clicker.execute();

            return true;
        }

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
        float x = e.getX();
        float y = e.getY();

        Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

        return true;
    }

    private class Clicker extends AsyncTask<Void, Void, Void> {
        private final int x;
        private final int y;

        Clicker(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RootExecutor.exec("input tap " + x + " " + y);
            return null;
        }
    }
}
