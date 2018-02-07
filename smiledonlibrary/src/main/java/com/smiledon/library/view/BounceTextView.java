package com.smiledon.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaidong on 2017/9/15.
 */

public class BounceTextView extends View {

    private Paint mPaint;
    private int viewHeight = 200;

    private float tv=0;
    private long duration = 50;

    public BounceTextView(Context context) {
        this(context, null);
    }

    public BounceTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawText(String.valueOf(curTv), 0, viewHeight/2, mPaint);

        super.onDraw(canvas);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            curTv += onceAddTv;
            if(curTv >= tv)
                curTv = tv;
            else
                mHandler.sendEmptyMessageDelayed(0, 1);
            invalidate();
        }
    };

    private float curTv= 0;
    private float onceAddTv;

    public void bounceTv(String text) {
        tv = Float.parseFloat(text);
        onceAddTv = tv/duration;

        mHandler.sendEmptyMessageDelayed(0, 20);
    }

}
