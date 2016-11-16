package ru.endlesscode.touchpointer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MousePointerService extends Service {
    private WindowManager wm;

    private PointerView pointerArea;
    private ToggleButton overlayButton;
    private WindowManager.LayoutParams pointerAreaParams;
    private WindowManager.LayoutParams buttonParams;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_SHORT).show();

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        pointerArea = new PointerView(this);
        pointerArea.setBackgroundColor(0x33aaddff);
        pointerAreaParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT);
        wm.addView(pointerArea, pointerAreaParams);
        pointerArea.setEnabled(false);

        overlayButton = new ToggleButton(this);
        overlayButton.setBackgroundColor(Color.DKGRAY);
        overlayButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pointerArea.setEnabled(isChecked);
            }
        });

        buttonParams = new WindowManager.LayoutParams(
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
        Toast.makeText(getBaseContext(),"onDestroy", Toast.LENGTH_SHORT).show();
        if (overlayButton != null) {
            wm.removeView(overlayButton);
            overlayButton = null;
        }

        if(pointerArea.isEnabled()) {
            wm.removeView(pointerArea);
            pointerArea = null;
        }
    }
}
