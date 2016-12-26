package ru.endlesscode.touchpointer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import ru.endlesscode.touchpointer.util.Config;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.injector.ShellInterface;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Touch Pointer";

    private Intent service;
    private TextView mouseSpeedVal;
    private TextView gestureSpeedVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config.init(this);
        setContentView(R.layout.activity_main);

        ShellInterface.isSuAvailable();
        this.initComponents();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Config.getButtonPosX() == -1 || Config.getButtonPosY() == -1) {
            Intent aboutScreen = new Intent(getBaseContext(), CalibrateActivity.class);
            this.startActivity(aboutScreen);
            return;
        }

        service = new Intent(this, TouchPointerService.class);
        if (!TouchPointerService.isEnabled()) {
            startTPService();
        }

        Switch enableSwitch = (Switch) findViewById(R.id.enableSwitch);
        enableSwitch.setChecked(TouchPointerService.isEnabled());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponents() {
        mouseSpeedVal = (TextView) findViewById(R.id.mouseSpeedVal);
        gestureSpeedVal = (TextView) findViewById(R.id.gestureSpeedVal);

        SeekBar mouseSpeedBar = (SeekBar) findViewById(R.id.mouseSpeedBar);
        mouseSpeedBar.setProgress((int) ((Config.getSpeedMultiplier() - 1) * 10));
        mouseSpeedVal.setText(getString(R.string.percent_value, Config.getSpeedMultiplier()));
        mouseSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Config.setSpeedMultiplier(1 + progress / 10f);
                mouseSpeedVal.setText(getString(R.string.percent_value, Config.getSpeedMultiplier()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        SeekBar gestureSpeedBar = (SeekBar) findViewById(R.id.gestureSpeedBar);
        gestureSpeedBar.setProgress((int) ((Config.getGestureMultiplier() - 1) * 10));
        gestureSpeedVal.setText(getString(R.string.percent_value, Config.getGestureMultiplier()));
        gestureSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Config.setGestureMultiplier(1 + progress / 10f);
                gestureSpeedVal.setText(getString(R.string.percent_value, Config.getGestureMultiplier()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


//        ToggleButton btn = (ToggleButton) findViewById(R.id.toggleButton);
    }

    private void startTPService() {
        startService(service);
        TouchPointerService.onEnable();
    }

    public void onServiceSwitchClicked(View view) {
        if (((Switch) view).isChecked()) {
            if (!TouchPointerService.isEnabled()) {
                startTPService();
            }
        } else if (TouchPointerService.isEnabled()) {
            stopService(service);
        }
    }

    public void onCalibrateBtnClicked(View view) {
        stopService(service);

        Intent calibrationActivity = new Intent(this, CalibrateActivity.class);
        startActivity(calibrationActivity);
    }
}
