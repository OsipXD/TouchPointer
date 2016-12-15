package ru.endlesscode.touchpointer.views;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import ru.endlesscode.touchpointer.gesture.PointerGestureListener;

/**
 * Created by OsipXD on 18.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class TouchArea extends View {
    private final PointerGestureListener gestureListener;
    private final GestureDetector gestureDetector;
    private final WindowManager wm;
    private boolean doubleTapped = false;
    private boolean touchable = true;

    TouchArea(Context context, WindowManager wm, Pointer pointer) {
        super(context, null);

        this.wm = wm;
        this.gestureListener = new PointerGestureListener(this, pointer);
        this.gestureDetector = new GestureDetector(context, gestureListener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.setClickable(enabled);
        this.setVisibility(enabled ? VISIBLE : INVISIBLE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!touchable) {
                return false;
        }

        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE && doubleTapped) {
            gestureListener.onDoubleTapDrag(event);
        }

        return false;
    }

    public void onDown() {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getLayoutParams();
        params.width = 0;
        params.height = 0;
        this.touchable = false;
        wm.updateViewLayout(this, params);
    }

    public void onUp() {
        WindowManager.LayoutParams params = (WindowManager.LayoutParams) this.getLayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.touchable = true;
        wm.updateViewLayout(this, params);
    }

    public void onDoubleTapped() {
        doubleTapped = true;
    }
}
