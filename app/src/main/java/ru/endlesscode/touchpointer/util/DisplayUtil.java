package ru.endlesscode.touchpointer.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by OsipXD on 22.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class DisplayUtil {
    private static WindowManager wm;

    public static void init(WindowManager wm) {
        DisplayUtil.wm = wm;
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
        } catch (Exception e) {
            Log.d("TapTap", e.toString(), e);
        }
    }

    public static DisplayMetrics getMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static int getDisplayRotation() {
        return wm.getDefaultDisplay().getRotation();
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}

