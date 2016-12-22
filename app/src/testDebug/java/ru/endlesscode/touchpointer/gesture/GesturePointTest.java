package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.view.Surface;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;
import ru.endlesscode.touchpointer.util.WindowManagerUtil;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by OsipXD on 22.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(WindowManagerUtil.class)
public class GesturePointTest {
    public GesturePointTest() {
        DisplayMetrics metrics = mock(DisplayMetrics.class);
        metrics.widthPixels = 100;
        metrics.heightPixels = 200;

        mockStatic(WindowManagerUtil.class);
        when(WindowManagerUtil.getMetrics()).thenReturn(metrics);
    }

    @Test
    public void rotation() throws Exception {
        GesturePoint point = new GesturePoint(10, 20, Surface.ROTATION_0);
        Assert.assertEquals(10, point.getX());
        Assert.assertEquals(20, point.getY());

        point = new GesturePoint(10, 20, Surface.ROTATION_90);
        Assert.assertEquals(180, point.getX());
        Assert.assertEquals(10, point.getY());

        point = new GesturePoint(10, 20, Surface.ROTATION_180);
        Assert.assertEquals(90, point.getX());
        Assert.assertEquals(180, point.getY());

        point = new GesturePoint(10, 20, Surface.ROTATION_270);
        Assert.assertEquals(20, point.getX());
        Assert.assertEquals(90, point.getY());
    }

}