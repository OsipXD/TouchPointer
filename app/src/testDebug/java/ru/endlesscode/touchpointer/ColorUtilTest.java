package ru.endlesscode.touchpointer;

import android.graphics.Color;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.legacy.PowerMockRunner;
import ru.endlesscode.touchpointer.util.ColorUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by OsipXD on 22.12.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Color.class)
public class ColorUtilTest {
    public ColorUtilTest() {
        mockStatic(Color.class);
    }

    @Test
    public void isColorDark_LightColor_ReturnFalse() throws Exception {
        int color = Color.WHITE;
        when(Color.red(color)).thenReturn(0xFF);
        when(Color.green(color)).thenReturn(0xFF);
        when(Color.blue(color)).thenReturn(0xFF);

        boolean result = ColorUtil.isColorDark(color);

        assertFalse(result);
    }

    @Test
    public void isColorDark_DarkColor_ReturnTrue() throws Exception {
        int color = Color.BLACK;
        when(Color.red(color)).thenReturn(0x00);
        when(Color.green(color)).thenReturn(0x00);
        when(Color.blue(color)).thenReturn(0x00);

        boolean result = ColorUtil.isColorDark(color);

        assertTrue(result);
    }

    @Test
    public void isColorDark_BorderColor_ReturnTrue() throws Exception {
        int color = 0xFF00D900;
        when(Color.red(color)).thenReturn(0x00);
        when(Color.green(color)).thenReturn(0xD9);
        when(Color.blue(color)).thenReturn(0x00);

        boolean result = ColorUtil.isColorDark(color);

        assertTrue(result);
    }
}