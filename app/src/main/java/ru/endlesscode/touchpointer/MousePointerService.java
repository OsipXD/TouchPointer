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
import ru.endlesscode.touchpointer.views.TouchPointerLayout;

public class MousePointerService extends Service {
    private WindowManager wm;

    private TouchPointerLayout touchPointerLayout;
    private ToggleButton overlayButton;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        touchPointerLayout = new TouchPointerLayout(this);
        WindowManager.LayoutParams pointerAreaParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        wm.addView(touchPointerLayout, pointerAreaParams);
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
        wm.addView(overlayButton, buttonParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayButton != null) {
            wm.removeView(overlayButton);
            overlayButton = null;
        }

        if(touchPointerLayout != null) {
            wm.removeView(touchPointerLayout);
            touchPointerLayout = null;
        }
    }
}
