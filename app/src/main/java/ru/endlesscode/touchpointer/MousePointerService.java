package ru.endlesscode.touchpointer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import ru.endlesscode.touchpointer.injector.EventInjector;
import ru.endlesscode.touchpointer.util.WindowManagerUtil;
import ru.endlesscode.touchpointer.views.TouchPointerLayout;

public class MousePointerService extends Service {
    private static boolean enabled = false;

    private TouchPointerLayout touchPointerLayout;
    private ToggleButton overlayButton;

    public static boolean isEnabled() {
        return enabled;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManagerUtil.init(windowManager);
        enabled = true;

        touchPointerLayout = new TouchPointerLayout(this, windowManager);
        touchPointerLayout.initComponents();
        touchPointerLayout.setEnabled(false);

        overlayButton = new ToggleButton(this);
        overlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                touchPointerLayout.setEnabled(isChecked);
            }
        });
        WindowManager.LayoutParams buttonParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        buttonParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        windowManager.addView(overlayButton, buttonParams);

        EventInjector.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (overlayButton != null) {
            windowManager.removeView(overlayButton);
            overlayButton = null;
        }

        if(touchPointerLayout != null) {
            touchPointerLayout.removeComponents();
            touchPointerLayout = null;
        }

        EventInjector.release();
        enabled = false;
    }
}
