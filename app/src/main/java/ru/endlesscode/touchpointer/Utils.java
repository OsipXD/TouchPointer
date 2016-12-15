package ru.endlesscode.touchpointer;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by OsipXD on 17.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Utils {
    private static final DisplayMetrics displayMetrics = new DisplayMetrics();

    static void init(WindowManager wm) {
        wm.getDefaultDisplay().getMetrics(displayMetrics);
    }

    public static boolean isColorDark(int color){
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;

        return darkness >= 0.5;
    }

    public static void disableAnimation(WindowManager.LayoutParams params) {
        String className = "android.view.WindowManager$LayoutParams";
        try {
            Class layoutParamsClass = Class.forName(className);

            Field privateFlags = layoutParamsClass.getField("privateFlags");
            Field noAnim = layoutParamsClass.getField("PRIVATE_FLAG_NO_MOVE_ANIMATION");

            int privateFlagsValue = privateFlags.getInt(params);
            int noAnimFlag = noAnim.getInt(params);
            privateFlagsValue |= noAnimFlag;

            privateFlags.setInt(params, privateFlagsValue);

            // Dynamically do stuff with this class
            // List constructors, fields, methods, etc.

        } catch (Exception e) {
            Log.d("TapTap", e.toString(), e);
            // Unknown exception
        }
    }
}
