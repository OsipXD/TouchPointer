package ru.endlesscode.touchpointer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.util.Config;
import ru.endlesscode.touchpointer.util.DisplayUtil;

public class CalibrateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_calibrate);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Config.setButtonCoords((int) event.getRawX() - DisplayUtil.dpToPx(20), (int) event.getRawY() - DisplayUtil.dpToPx(40));

            Intent mainActivity = new Intent(getBaseContext(), MainActivity.class);
            startActivity(mainActivity);
        }

        return super.onTouchEvent(event);
    }
}
