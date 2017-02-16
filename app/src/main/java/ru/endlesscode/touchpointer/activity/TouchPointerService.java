package ru.endlesscode.touchpointer.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.CompoundButton;
import ru.endlesscode.touchpointer.injector.EventInjector;
import ru.endlesscode.touchpointer.util.Config;
import ru.endlesscode.touchpointer.util.DisplayUtil;
import ru.endlesscode.touchpointer.views.CircleToggleButton;
import ru.endlesscode.touchpointer.views.Pointer;
import ru.endlesscode.touchpointer.views.TouchArea;

public class TouchPointerService extends Service {
    private static boolean enabled = false;

    private TouchArea touchArea;
    private CircleToggleButton overlayButton;
    private Pointer pointer;

    private WindowManager wm;

    static boolean isEnabled() {
        return enabled;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        this.wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayUtil.init(wm);

        this.initComponents();
        setTouchEnabled(false);

        EventInjector.init();
    }

    @Override
    public void onDestroy() {
        EventInjector.release();

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (overlayButton != null) {
            windowManager.removeView(overlayButton);
            overlayButton = null;
        }

        if (touchArea != null) {
            wm.removeView(touchArea);
            touchArea = null;
        }

        if (pointer != null) {
            wm.removeView(pointer);
            pointer = null;
        }

        enabled = false;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent restartIntent = new Intent(this, getClass());

            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            PendingIntent pi = PendingIntent.getService(this, 1, restartIntent, PendingIntent.FLAG_ONE_SHOT);
            am.setExact(AlarmManager.RTC, System.currentTimeMillis() + 3000, pi);
        }
    }

    private void setTouchEnabled(boolean enabled) {
        touchArea.setEnabled(enabled);
        pointer.setVisible(enabled);
    }

    private void initComponents() {
        pointer = new Pointer(this, null);

        int touchableFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        // Add touch area
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, touchableFlags, PixelFormat.TRANSLUCENT);
        DisplayUtil.disableAnimation(layoutParams);
        touchArea = new TouchArea(this, this.wm, this.pointer);
        wm.addView(touchArea, layoutParams);

        // Add button
        overlayButton = new CircleToggleButton(this);
        overlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setTouchEnabled(isChecked);
            }
        });

        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                Config.getButtonPosX(), Config.getButtonPosY(),
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        wm.addView(overlayButton, layoutParams);

        // Add pointer
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR, touchableFlags | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, PixelFormat.TRANSLUCENT);
        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        wm.addView(pointer, layoutParams);
    }

    static void onEnable() {
        enabled = true;
    }
}
