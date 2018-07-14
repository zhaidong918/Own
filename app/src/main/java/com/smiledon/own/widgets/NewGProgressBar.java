package com.smiledon.own.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.smiledon.own.R;

import java.text.NumberFormat;

/**
 * Created by SmileDon on 2018/5/7.
 */

public class NewGProgressBar extends View {

    public NewGProgressBar(Context context) {
        this(context, null);
    }

    public NewGProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewGProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

    }

    private void initView() {

        mLightPaint = new Paint();
        mLightPaint.setColor(getResources().getColor(R.color.blue));
        mLightPaint.setAntiAlias(true);

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(getResources().getColor(R.color.background_layout));
        mDefaultPaint.setAntiAlias(true);

        mWhitePaint = new Paint();
        mWhitePaint.setColor(getResources().getColor(R.color.white));
        mWhitePaint.setAntiAlias(true);

        mGrayPaint = new Paint();
        mGrayPaint.setColor(getResources().getColor(R.color.black_overlay));
        mGrayPaint.setAntiAlias(true);

        // 设置精确到小数点后2位
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

    }


    int mHeight;
    int mWidth;

    int leftPadding;
    int rightPadding;

    Paint mLightPaint;
    Paint mDefaultPaint;
    Paint mWhitePaint;
    Paint mGrayPaint;

    // 创建一个数值格式化对象
    NumberFormat numberFormat;

    public void setDotColor(int color){
        mLightPaint.setColor(getResources().getColor(color));
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        leftPadding = getPaddingLeft();
        rightPadding = getPaddingRight();

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth() - leftPadding - rightPadding;

    }

    int mSolidRadius = 12;
    int mHollowRadius = 15;
    int mLineHeight = 8;
    int mTouchX = 0;
    String mProgress = "0";

    int mDotSize = 5;

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        /*下面是绘制背景*/
        RectF rectF = new RectF(leftPadding + 0,
                mHeight/2 - mLineHeight /2,
                leftPadding + mWidth,
                mHeight/2 + mLineHeight /2);

        canvas.drawRect(rectF, mDefaultPaint);

        /*下面是绘制滑动区域*/
        RectF iRectF = new RectF(leftPadding + 0,
                mHeight/2 - mLineHeight /2,
                leftPadding + mTouchX,
                mHeight/2 + mLineHeight /2);

        canvas.drawRect(iRectF, mLightPaint);


        /*下面是绘制五个定点*/
        boolean isDrawTouchX = false;
        for (int i = 0; i < mDotSize; i++) {

            int dotPosition = mWidth * i / (mDotSize - 1);

            canvas.drawCircle(leftPadding + dotPosition,mHeight/2, mHollowRadius, mTouchX >= dotPosition ? mWhitePaint : mGrayPaint);
            canvas.drawCircle(leftPadding + dotPosition,mHeight/2, mSolidRadius, mTouchX >= dotPosition ? mLightPaint : mDefaultPaint);

            isDrawTouchX = mTouchX == mDotSize;

        }

          /*绘制滑动的点*/
        if (!isDrawTouchX) {
            canvas.drawCircle(leftPadding + mTouchX,mHeight/2, isTouch ? mHollowRadius + 5 : mHollowRadius, mWhitePaint);
            canvas.drawCircle(leftPadding + mTouchX,mHeight/2, isTouch ? mSolidRadius + 5 : mSolidRadius, mLightPaint);
        }

        super.onDraw(canvas);
    }

    private boolean isTouch = false;


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mTouchX = (int) event.getX() - leftPadding;
                updateProgress();
                return true;
            case MotionEvent.ACTION_MOVE:
                mTouchX = (int) event.getX() - leftPadding;
                updateProgress();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                updateProgress();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void updateProgress(){
        if(mTouchX > mWidth)
            mTouchX = mWidth;
        if(mTouchX < 0)
            mTouchX = 0;
        mProgress = numberFormat.format(mTouchX * 100/mWidth);

        invalidate();

        if(mOnProgressChangedListener != null)
            mOnProgressChangedListener.onProgressChanged(mProgress);
    }

    public interface OnProgressChangedListener{
        void onProgressChanged(String progress);
    }

    private  OnProgressChangedListener mOnProgressChangedListener;

    public void setOnProgressChangedListener(OnProgressChangedListener listener){
        mOnProgressChangedListener = listener;
    }

    public void updateProgress(int progress){
        mProgress = String.valueOf(progress);
        mTouchX = progress * mWidth / 100;
        invalidate();
    }

}
