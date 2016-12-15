package ru.endlesscode.touchpointer.views;

import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import ru.endlesscode.touchpointer.MousePointerService;
import ru.endlesscode.touchpointer.Utils;

/**
 * Created by OsipXD on 18.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class TouchPointerLayout {
    private final MousePointerService context;
    private final WindowManager wm;

    private Pointer pointer;
    private TouchArea touchArea;

    public TouchPointerLayout(MousePointerService context, WindowManager wm) {
        this.context = context;
        this.wm = wm;
    }

    public void setEnabled(boolean enabled) {
        touchArea.setEnabled(enabled);
        pointer.setEnabled(enabled);
    }

    public void initComponents() {
        pointer = new Pointer(this.context, null, wm);

        int untouchableFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        int touchableFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        // Add pointer
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, untouchableFlags, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        wm.addView(pointer, layoutParams);

        // Add touchable area
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, touchableFlags, PixelFormat.TRANSLUCENT);
        Utils.disableAnimation(layoutParams);
        touchArea = new TouchArea(this.context, this.wm, this.pointer);
        touchArea.setBackgroundColor(0x44ff0000);
        wm.addView(touchArea, layoutParams);

        DisplayMetrics metrics = this.getMetrics();
        int x = metrics.widthPixels / 2;
        int y = metrics.heightPixels / 2;

        this.pointer.setPointerPosition(x, y);
    }

    public Pointer getPointer() {
        return pointer;
    }

    private DisplayMetrics getMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics;
    }

    public void removeComponents() {
        wm.removeView(touchArea);
        wm.removeView(pointer);
    }
}
