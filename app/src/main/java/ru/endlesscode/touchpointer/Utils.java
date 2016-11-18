package ru.endlesscode.touchpointer;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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

    static DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }
}
