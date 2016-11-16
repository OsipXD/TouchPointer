package ru.endlesscode.touchpointer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * TODO: document your custom view class.
 */
class PointerView extends View {
    private Clicker clicker;
    private Paint paint;
    private float x, y, oldX, oldY;
    private GestureDetector gestureDetector;

    PointerView(MousePointerService context) {
        super(context);

        this.setLongClickable(true);

        paint = new Paint();
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(30);
        paint.setStrokeCap(Paint.Cap.ROUND);

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        this.x = metrics.widthPixels / 2f;
        this.y = metrics.heightPixels / 2f;

        this.gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (x < 0) {
            x = 0;
        } else if (x > this.getWidth()) {
           x = this.getWidth();
        }

        if (y < 0) {
            y = 0;
        } else if (y > this.getHeight()) {
            y = this.getHeight();
        }

        canvas.drawPoint(x, y, paint);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.setClickable(enabled);
        this.setVisibility(enabled ? VISIBLE : INVISIBLE);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private class Clicker extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            PointerView.this.setEnabled(false);
        }


        @Override
        protected Void doInBackground(Void... params) {
            RootExecutor.exec("input tap " + x + " " + y);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            PointerView.this.setEnabled(true);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            oldX = x;
            oldY = y;

            return true;
        }

//        @Override
//        public void onLongPress(MotionEvent e) {
//            Log.d("Long Press", "I'm here");
//        }
//
//        @Override
//        public boolean onContextClick(MotionEvent e) {
//            Log.d("Context Click", "I'm here");
//            return true;
//        }
//
//        @Override
//        public boolean onDoubleTapEvent(MotionEvent e) {
//            Log.d("Double Tap Event", "I'm here");
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//            Log.d("Fling", "I'm here");
//            return true;
//        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            x = oldX + (e2.getX() - e1.getX()) * Config.getSpeedMultiplier();
            y = oldY + (e2.getY() - e1.getY()) * Config.getSpeedMultiplier();
            PointerView.this.invalidate();

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (clicker == null || clicker.getStatus() != AsyncTask.Status.RUNNING) {
                PointerView.this.setEnabled(false);
                clicker = new Clicker();
                clicker.execute();

                return true;
            }

            return false;
        }

//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            Log.d("Single Tap Up", "I'm here");
//            return true;
//        }
//
//        @Override
//        public void onShowPress(MotionEvent e) {
//            Log.d("Show Pres", "I'm here");
//        }
//
//        @Override
//        public boolean onDoubleTap(MotionEvent e) {
//            float x = e.getX();
//            float y = e.getY();
//
//            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
//
//            return true;
//        }
    }
}
