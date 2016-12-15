package ru.endlesscode.touchpointer.gesture;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.injector.EventInjector;
import ru.endlesscode.touchpointer.views.TouchArea;

import java.util.List;

/**
 * Created by OsipXD on 14.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class GestureInjector {
    private final TouchArea area;

    GestureInjector(TouchArea area) {
        this.area = area;
    }

    public void sendClick(final int x, final int y) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                EventInjector.sendTouch(SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y);
                EventInjector.sendTouch(SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y);
            }
        }).execute();
    }

    public void sendGesture(final Gesture gesture) {
        new SendGestureTask(new Runnable() {
            @Override
            public void run() {
                GesturePoint start = gesture.getStart();
                List<GesturePoint> way = gesture.getWay();
                GesturePoint end = gesture.getEnd();
                long time = SystemClock.uptimeMillis();

                EventInjector.sendTouch(time, MotionEvent.ACTION_DOWN, start.x, start.y);

                if (!way.isEmpty()) {
                    for (GesturePoint point : way) {
                        EventInjector.sendTouch(time+ point.timeOffset, MotionEvent.ACTION_MOVE, point.x, point.y);
                    }
                }

                EventInjector.sendTouch(time + end.timeOffset, MotionEvent.ACTION_UP, end.x, end.y);
            }
        }).execute();
    }

    private class SendGestureTask extends AsyncTask<Void, Void, Void> {
        private final Runnable task;

        SendGestureTask(Runnable task) {
            this.task = task;
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

            task.run();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            area.onUp();
        }
    }
}
