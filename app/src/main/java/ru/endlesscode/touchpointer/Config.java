package ru.endlesscode.touchpointer;

/**
 * Created by OsipXD on 15.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Config {
    private static float speedMultiplier = 1.0f;

    public static float getSpeedMultiplier() {
        return speedMultiplier;
    }

    static void setSpeedMultiplier(float speedMultiplier) {
        Config.speedMultiplier = speedMultiplier;
    }
}
