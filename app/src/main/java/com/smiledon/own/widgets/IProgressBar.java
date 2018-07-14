package com.smiledon.own.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.smiledon.own.R;

import java.text.NumberFormat;

/**
 * Created by SmileDon on 2018/5/7.
 */

public class IProgressBar extends android.support.v7.widget.AppCompatSeekBar {

    public IProgressBar(Context context) {
        this(context, null);
    }

    public IProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

    }


    private void initView() {

        mDotPaint = new Paint();
        mDotPaint.setColor(getResources().getColor(R.color.decrease_color));
        mDotPaint.setAntiAlias(true);

        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.highlight_color));
        mLinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(getResources().getColor(R.color.highlight_color));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(36);

        // 设置精确到小数点后2位
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

    }


    int mHeight;
    int mWidth;

    int leftPadding;
    int rightPadding;

    Paint mDotPaint;
    Paint mLinePaint;
    Paint mTextPaint;

    // 创建一个数值格式化对象
    NumberFormat numberFormat;




    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        leftPadding = getPaddingLeft();
        rightPadding = getPaddingRight();

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth() - leftPadding - rightPadding;

    }

    int radius = 20;
    int lineHeight = 8;

    int touchX = 0;


    @Override
    protected synchronized void onDraw(Canvas canvas) {

/*        RectF rectF = new RectF(leftPadding + 0,
                mHeight/2 - lineHeight/2,
                leftPadding + mWidth,
                mHeight/2 + lineHeight/2);

        canvas.drawRect(rectF, mLinePaint);


        RectF iRectF = new RectF(leftPadding + 0,
                mHeight/2 - lineHeight/2,
                leftPadding + touchX,
                mHeight/2 + lineHeight/2);

        canvas.drawRect(iRectF, mDotPaint);*/

        super.onDraw(canvas);

        canvas.drawCircle(leftPadding + 0,mHeight/2, radius, getProgress() >=0 ? mDotPaint : mLinePaint);

        canvas.drawCircle(leftPadding + mWidth/4,mHeight/2, radius, getProgress() >= 25 ? mDotPaint : mLinePaint);

        canvas.drawCircle(leftPadding + mWidth/2,mHeight/2, radius, getProgress() >= 50 ? mDotPaint : mLinePaint);

        canvas.drawCircle(leftPadding + mWidth/4 * 3,mHeight/2, radius, getProgress() >= 75 ? mDotPaint : mLinePaint);

        canvas.drawCircle(leftPadding + mWidth,mHeight/2, radius, getProgress() >= 100 ? mDotPaint : mLinePaint);

//        canvas.drawText(progress + "%", leftPadding, mHeight, mTextPaint);


    }


}
