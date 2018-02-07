package com.smiledon.own.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * @author East Chak
 * @date 2018/1/19 16:47
 */

public class CircleLayout extends LinearLayout{


    public CircleLayout(Context context) {
        super(context);
    }

    public CircleLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scaleAnim(0.9f);
                break;
            case MotionEvent.ACTION_UP:
                scaleAnim(1.0f);
                break;
        }

        return super.onTouchEvent(event);
    }


    private void scaleAnim(float scale) {
        this.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(300)
                .start();
    }
}
