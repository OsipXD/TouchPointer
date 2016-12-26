package ru.endlesscode.touchpointer.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.ToggleButton;
import ru.endlesscode.touchpointer.activity.MainActivity;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.util.DisplayUtil;

/**
 * TODO: document your custom view class.
 */
public class CircleToggleButton extends ToggleButton {
    private GestureDetector gestureDetector;
    private boolean longPress = false;

    public CircleToggleButton(Context context) {
        this(context, null);
    }

    public CircleToggleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.setSelected(this.isSelected());
        this.setLongClickable(true);
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.setTextOff("");
        this.setTextOn("");
        this.setMinimumHeight(DisplayUtil.dpToPx(40));
        this.setMinimumWidth(DisplayUtil.dpToPx(40));
        ViewCompat.setElevation(this, DisplayUtil.dpToPx(3));

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.circle_toggle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackground(drawable);
        } else {
            this.setBackgroundDrawable(drawable);
        }
    }

    private void onToggleOn() {
        this.animate().scaleYBy(.7f).scaleYBy(.7f).scaleX(1.f).scaleY(1.f).alphaBy(.3f).alpha(1.f).start();
    }

    private void onToggleOff() {
        this.animate().scaleYBy(1.f).scaleYBy(1.f).scaleX(.7f).scaleY(.7f).alphaBy(1.f).alpha(.3f).start();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (selected) {
            onToggleOn();
        } else {
            onToggleOff();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (longPress) {
                longPress = false;
                return false;
            }

            if (CircleToggleButton.this.isChecked()) {
                onToggleOff();
            } else {
                onToggleOn();
            }
        }

        return super.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            CircleToggleButton.this.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            Intent intent = new Intent(CircleToggleButton.this.getContext(), MainActivity.class);
            CircleToggleButton.this.longPress = true;
            CircleToggleButton.this.getContext().startActivity(intent);
        }
    }
}
