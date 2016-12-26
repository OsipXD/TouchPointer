package ru.endlesscode.touchpointer.util;

import android.content.Context;
import android.content.SharedPreferences;
import ru.endlesscode.touchpointer.R;

/**
 * Created by OsipXD on 15.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Config {
    private static String speedMultiplierKey;
    private static String gestureMultiplierKey;
    private static String buttonPosXKey;
    private static String buttonPosYKey;

    private static float speedMultiplier;
    private static float gestureMultiplier;
    private static int buttonPosX;
    private static int buttonPosY;

    private static SharedPreferences sharedPref;

    public static void init(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);

        speedMultiplierKey = context.getString(R.string.speed_multiplier);
        gestureMultiplierKey = context.getString(R.string.gesture_multiplier);
        buttonPosXKey = context.getString(R.string.button_pos_x);
        buttonPosYKey = context.getString(R.string.button_pos_y);

        speedMultiplier = sharedPref.getFloat(speedMultiplierKey, 1.0f);
        gestureMultiplier = sharedPref.getFloat(gestureMultiplierKey, 1.0f);
        buttonPosX = sharedPref.getInt(buttonPosXKey, -1);
        buttonPosY = sharedPref.getInt(buttonPosYKey, -1);
    }

    public static float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public static void setSpeedMultiplier(float speedMultiplier) {
        Config.speedMultiplier = speedMultiplier;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(speedMultiplierKey, speedMultiplier);
        editor.apply();
    }

    public static float getGestureMultiplier() {
        return gestureMultiplier;
    }

    public static void setGestureMultiplier(float gestureMultiplier) {
        Config.gestureMultiplier = gestureMultiplier;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat(gestureMultiplierKey, gestureMultiplier);
        editor.apply();
    }

    public static void setButtonCoords(int x, int y) {
        buttonPosX = x;
        buttonPosY = y;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(buttonPosXKey, buttonPosX);
        editor.putInt(buttonPosYKey, buttonPosY);
        editor.apply();
    }

    public static int getButtonPosX() {
        return buttonPosX;
    }

    public static int getButtonPosY() {
        return buttonPosY;
    }
}
