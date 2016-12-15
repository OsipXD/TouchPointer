package ru.endlesscode.touchpointer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import ru.endlesscode.touchpointer.injector.ShellInterface;

public class MainActivity extends AppCompatActivity {
    private static boolean serviceEnabled = false;

    private Intent service;
    private TextView mouseSpeedVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShellInterface.isSuAvailable();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        Utils.init((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        service = new Intent(this, MousePointerService.class);

        mouseSpeedVal = (TextView) findViewById(R.id.mouseSpeedVal);
        SeekBar mouseSpeedBar = (SeekBar) findViewById(R.id.mouseSpeedBar);
        mouseSpeedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("onProgressChanged");
                Config.setSpeedMultiplier(1 + progress / 10f);
                mouseSpeedVal.setText("x" + Config.getSpeedMultiplier());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onClickService(View v) {
        if (serviceEnabled) {
            stopService(service);
        } else {
            startService(service);
        }

        serviceEnabled = !serviceEnabled;
    }
}
