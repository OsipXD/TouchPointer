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

    private static float speedMultiplier;
    private static float gestureMultiplier;

    private static SharedPreferences sharedPref;

    public static void init(Context context) {
        sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);

        speedMultiplierKey = context.getString(R.string.speed_multiplier);
        gestureMultiplierKey = context.getString(R.string.gesture_multiplier);

        speedMultiplier = sharedPref.getFloat(speedMultiplierKey, 1.0f);
        gestureMultiplier = sharedPref.getFloat(gestureMultiplierKey, 1.0f);
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
}
