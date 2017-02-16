package ru.endlesscode.touchpointer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.util.ColorUtil;
import ru.endlesscode.touchpointer.util.DisplayUtil;

/**
 * Created by OsipXD on 17.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Pointer extends View {
    private final Paint cursor = new Paint();
    private final Paint cursorBorder = new Paint();
    private final Paint border = new Paint();

    private int size;
    private int borderSize;
    private int pointerX;
    private int pointerY;

    public Pointer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Pointer,
                0, 0
        );

        int color;
        int borderColor;
        int size;
        int borderSize;
        try {
            color = a.getColor(R.styleable.Pointer_pointerColor, Color.DKGRAY);
            borderColor = a.getColor(R.styleable.Pointer_borderColor, 0xFF388E3C);
            size = a.getDimensionPixelSize(R.styleable.Pointer_pointerSize, DisplayUtil.dpToPx(10));
            borderSize = a.getDimensionPixelSize(R.styleable.Pointer_borderSize, DisplayUtil.dpToPx(1));
        } finally {
            a.recycle();
        }

        this.setColor(color);
        this.setSize(size, borderSize);

        this.cursor.setStrokeCap(Paint.Cap.ROUND);
        this.cursorBorder.setStrokeCap(Paint.Cap.ROUND);

        this.border.setColor(borderColor);
        this.border.setStrokeWidth(borderSize * 2);
        this.border.setStyle(Paint.Style.STROKE);

        DisplayMetrics metrics = DisplayUtil.getMetrics();
        this.pointerX = metrics.widthPixels / 2;
        this.pointerY = metrics.heightPixels / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.size + this.borderSize;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        DisplayMetrics metrics = DisplayUtil.getMetrics();
        canvas.drawPoint(pointerX, pointerY, cursorBorder);
        canvas.drawPoint(pointerX, pointerY, cursor);
        canvas.drawRect(0, 0, metrics.widthPixels, metrics.heightPixels, border);
    }

    public void setVisible(boolean visible) {
        this.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    public void setPointerPosition(int pointerX, int pointerY) {
        this.pointerX = pointerX;
        this.pointerY = pointerY;

        invalidate();
    }

    public int getPointerX() {
        return pointerX;
    }

    public int getPointerY() {
        return pointerY;
    }

    private void setColor(int color) {
        this.cursor.setColor(color);
        this.cursorBorder.setColor(ColorUtil.isColorDark(color) ? Color.LTGRAY : Color.DKGRAY);

        invalidate();
    }

    private void setSize(int size, int borderSize) {
        this.size = size;
        this.borderSize = borderSize;

        this.cursor.setStrokeWidth(this.size);
        this.cursorBorder.setStrokeWidth(this.size + this.borderSize);

        requestLayout();
    }
}
