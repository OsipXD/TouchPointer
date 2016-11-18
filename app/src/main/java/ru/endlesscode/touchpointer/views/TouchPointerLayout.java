package ru.endlesscode.touchpointer.views;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import ru.endlesscode.touchpointer.MousePointerService;
import ru.endlesscode.touchpointer.R;
import ru.endlesscode.touchpointer.gesture.PointerGestureListener;

/**
 * Created by OsipXD on 18.11.2016
 * It is part of the TouchPointer.
 * All rights reserved 2014 - 2016 © «EndlessCode Group»
 */
public class TouchPointerLayout extends FrameLayout {
    private static final int TOP_RIGHT  = 0;
    private static final int TOP_LEFT   = 1;
    private static final int BOT_LEFT   = 2;
    private static final int BOT_RIGHT  = 3;

    private Pointer pointer;
    private TouchArea[] touchAreas = new TouchArea[4];
    private GestureDetector gestureDetector;

    public TouchPointerLayout(MousePointerService context) {
        super(context);

        initComponent();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        this.setClickable(enabled);
        this.setVisibility(enabled ? VISIBLE : INVISIBLE);
    }

    private void initComponent() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.pointer_area_layout, this);
        pointer = (Pointer) findViewById(R.id.pointer);
        touchAreas[TOP_RIGHT] = (TouchArea) findViewById(R.id.touchArea1);
        touchAreas[TOP_RIGHT].setBackgroundColor(0x44ff0000);
        touchAreas[TOP_LEFT] = (TouchArea) findViewById(R.id.touchArea2);
        touchAreas[TOP_LEFT].setBackgroundColor(0x4400ff00);
        touchAreas[BOT_LEFT] = (TouchArea) findViewById(R.id.touchArea3);
        touchAreas[BOT_LEFT].setBackgroundColor(0x440000ff);
        touchAreas[BOT_RIGHT] = (TouchArea) findViewById(R.id.touchArea4);
        touchAreas[BOT_RIGHT].setBackgroundColor(0x44000000);

        this.gestureDetector = new GestureDetector(this.getContext(), new PointerGestureListener(this));
        for (TouchArea area : touchAreas) {
            area.init(gestureDetector);
        }
    }

    public void update(int x, int y) {
        int halfPointer = pointer.getWidth() / 2;

        ViewGroup.LayoutParams params = touchAreas[TOP_RIGHT].getLayoutParams();
        params.width =  this.getWidth() - (x + halfPointer);
        params.height = y + halfPointer;

        params = touchAreas[BOT_LEFT].getLayoutParams();
        params.width = x - halfPointer;
        params.height =  this.getHeight() - (y - halfPointer);

        touchAreas[TOP_RIGHT].requestLayout();
        pointer.requestLayout();
    }

    public Pointer getPointer() {
        return pointer;
    }
}
