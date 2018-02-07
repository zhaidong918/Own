package com.smiledon.library.view.snianav;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaidong on 2017/9/11.
 */

public class DynamicLine extends View {

    private int screenWidth = getScreenWidth();

    private int viewHeight = 20;
    private int lineHeight = 10;

    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private RectF mRectF;

    private float startX = 0;
    private float stopX = 0;

    public DynamicLine(Context context) {
        this(context, null);
    }

    public DynamicLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);

        mLinearGradient = new LinearGradient(0, 100,screenWidth,100, Color.parseColor("#ffc125"), Color.parseColor("#ff4500"), Shader.TileMode.MIRROR);
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(startX - stopX < 0)
            mRectF = new RectF(startX, 0, stopX, lineHeight);
        else
            mRectF = new RectF(stopX, 0, startX, lineHeight);
        canvas.drawRoundRect(mRectF, lineHeight/2, lineHeight/2, mPaint);
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.getMode(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void updateLineView(float startX, float stopX){
        this.startX = startX;
        this.stopX = stopX;
        invalidate();
    }

    private int getScreenWidth(){
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }
}
