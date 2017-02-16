package ru.endlesscode.touchpointer.gesture;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import ru.endlesscode.touchpointer.injector.EventInjector;
import ru.endlesscode.touchpointer.views.TouchArea;

import java.util.List;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
class GestureInjector {
    private final TouchArea area;

    GestureInjector(TouchArea area) {
        this.area = area;
    }

    void tap(final GesturePoint point) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                EventInjector.sendMotion(point, SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN);
                EventInjector.sendMotion(point, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP);
            }
        }).execute();
    }

    void gesture(final Gesture gesture) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                long time = SystemClock.uptimeMillis();
                List<GesturePoint> way = gesture.getWay();
                for (int i = 0, waySize = way.size(); i < waySize; i++) {
                    int motion = (i == 0 ? MotionEvent.ACTION_DOWN : (i == waySize - 1 ? MotionEvent.ACTION_UP : MotionEvent.ACTION_MOVE));
                    GesturePoint point = way.get(i);
                    EventInjector.sendMotion(point, time + point.getTimeOffset(), motion);
                }
            }
        }).execute();
    }

    void longTap(final GesturePoint point) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                long time = SystemClock.uptimeMillis();
                EventInjector.sendMotion(point,time, MotionEvent.ACTION_DOWN);
                EventInjector.sendMotion(point, time + ViewConfiguration.getLongPressTimeout() * 2, MotionEvent.ACTION_UP);
            }
        }).execute();
    }

    void doubleTap(final GesturePoint point) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                long time = SystemClock.uptimeMillis();

                // First Tap
                EventInjector.sendMotion(point, time, MotionEvent.ACTION_DOWN);
                EventInjector.sendMotion(point, time, MotionEvent.ACTION_UP);

                // Second Tap
                EventInjector.sendMotion(point, time + ViewConfiguration.getDoubleTapTimeout() / 3, MotionEvent.ACTION_DOWN);
                EventInjector.sendMotion(point, time + ViewConfiguration.getDoubleTapTimeout() / 3, MotionEvent.ACTION_UP);
            }
        }, true).execute();
    }

    private class SendGestureTask extends AsyncTask<Void, Void, Void> {
        private final Runnable task;
        private boolean withBreaks;

        SendGestureTask(Runnable task) {
            this(task, false);
        }

        SendGestureTask(Runnable task, boolean withBreaks) {
            this.task = task;
            this.withBreaks = withBreaks;
        }

        @Override
        protected void onPreExecute() {
            area.onDown();
        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this) {
                try {
                    wait(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (!withBreaks) {
                publishProgress();
            }

            task.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            area.onUp();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (withBreaks) {
                area.onUp();
            }
        }
    }
}
