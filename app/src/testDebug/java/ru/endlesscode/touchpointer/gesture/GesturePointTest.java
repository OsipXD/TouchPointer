package ru.endlesscode.touchpointer.gesture;

import android.util.DisplayMetrics;
import android.view.Surface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;
import ru.endlesscode.touchpointer.util.DisplayUtil;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by OsipXD on 22.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(DisplayUtil.class)
public class GesturePointTest {
    public GesturePointTest() {
        DisplayMetrics metrics = mock(DisplayMetrics.class);
        metrics.widthPixels = 100;
        metrics.heightPixels = 200;

        mockStatic(DisplayUtil.class);
        when(DisplayUtil.getMetrics()).thenReturn(metrics);
    }

    @Test
    public void getXY_Rotation0() throws Exception {
        GesturePoint point = new GesturePoint(10, 20, Surface.ROTATION_0);

        int x = point.getX();
        int y = point.getY();

        assertEquals(10, x);
        assertEquals(20, y);
    }

    @Test
    public void getXY_Rotation90() throws Exception {
        GesturePoint point = new GesturePoint(10, 20, Surface.ROTATION_90);

        int x = point.getX();
        int y = point.getY();

        assertEquals(180, x);
        assertEquals(10, y);
    }

    @Test
    public void getXY_Rotation180() throws Exception {
        GesturePoint point = new GesturePoint(10, 20, Surface.ROTATION_180);

        int x = point.getX();
        int y = point.getY();

        assertEquals(90, x);
        assertEquals(180, y);
    }

    @Test
    public void getXY_Rotation270() throws Exception {
        GesturePoint point = new GesturePoint(10, 20, Surface.ROTATION_270);

        int x = point.getX();
        int y = point.getY();

        assertEquals(20, x);
        assertEquals(90, y);
    }

}