package ru.endlesscode.touchpointer.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.Utils;

/**
 * Created by OsipXD on 17.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class Pointer extends View {
    private int size;
    private int borderSize;
    private int color;

    private final Paint cursor = new Paint();
    private final Paint border = new Paint();
    private int pointerX;
    private int pointerY;

    public Pointer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Pointer,
                0, 0
        );

        int color = a.getColor(R.styleable.Pointer_pointerColor, Color.DKGRAY);
        int size = a.getDimensionPixelSize(R.styleable.Pointer_pointerSize, 30);
        int borderSize = a.getDimensionPixelSize(R.styleable.Pointer_borderSize, 3);

        this.setColor(color);
        this.setSize(size, borderSize);

        this.cursor.setStrokeCap(Paint.Cap.ROUND);
        this.border.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.size + this.borderSize;
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = this.getWidth() / 2;
        int y = this.getHeight() / 2;

        canvas.drawPoint(x, y, border);
        canvas.drawPoint(x, y, cursor);
    }

    public void setPointerPosition(int pointerX, int pointerY) {
        this.pointerX = pointerX;
        this.pointerY = pointerY;

        int marginLeft = pointerX - this.getWidth() / 2;
        int marginTop = pointerY - this.getHeight() / 2;

        Log.d("TapTap", "Position: (" + marginLeft + ", " + marginTop + ")");
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.getLayoutParams();
        params.setMargins(marginLeft, marginTop, 0, 0);
    }

    public int getPointerX() {
        return pointerX;
    }

    public int getPointerY() {
        return pointerY;
    }

    public void setColor(int color) {
        this.color = color;

        this.cursor.setColor(this.color);
        this.border.setColor(Utils.isColorDark(this.color) ? Color.LTGRAY : Color.DKGRAY);

        invalidate();
        requestLayout();
    }

    public void setSize(int size, int borderSize) {
        this.size = size;
        this.borderSize = borderSize;

        this.cursor.setStrokeWidth(this.size);
        this.border.setStrokeWidth(this.size + this.borderSize);

        invalidate();
        requestLayout();
    }
}
