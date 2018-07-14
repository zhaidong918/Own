package com.diting.market.widgets.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.diting.market.R;

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
        mLightPaint.setColor(getResources().getColor(R.color.blue_btn_color));
        mLightPaint.setAntiAlias(true);

        mDefaultPaint = new Paint();
        mDefaultPaint.setColor(getResources().getColor(R.color.progress_color));
        mDefaultPaint.setAntiAlias(true);

        mWhitePaint = new Paint();
        mWhitePaint.setColor(getResources().getColor(R.color.white_color));
        mWhitePaint.setAntiAlias(true);

        mGrayPaint = new Paint();
        mGrayPaint.setColor(getResources().getColor(R.color.black_6));
        mGrayPaint.setAntiAlias(true);

        // 设置精确到小数点后2位
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
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

    @Override
    protected synchronized void onDraw(Canvas canvas) {

        RectF rectF = new RectF(leftPadding + 0,
                mHeight/2 - mLineHeight /2,
                leftPadding + mWidth,
                mHeight/2 + mLineHeight /2);

        canvas.drawRect(rectF, mDefaultPaint);

        RectF iRectF = new RectF(leftPadding + 0,
                mHeight/2 - mLineHeight /2,
                leftPadding + mTouchX,
                mHeight/2 + mLineHeight /2);

        canvas.drawRect(iRectF, mLightPaint);

        canvas.drawCircle(leftPadding + 0,mHeight/2, mHollowRadius, mTouchX >= 0 ? mWhitePaint : mGrayPaint);
        canvas.drawCircle(leftPadding + 0,mHeight/2, mSolidRadius, mTouchX >= 0 ? mLightPaint : mDefaultPaint);

        canvas.drawCircle(leftPadding + mWidth/4,mHeight/2, mHollowRadius, mTouchX >= mWidth/4 ? mWhitePaint : mGrayPaint);
        canvas.drawCircle(leftPadding + mWidth/4,mHeight/2, mSolidRadius, mTouchX >= mWidth/4 ? mLightPaint : mDefaultPaint);

        canvas.drawCircle(leftPadding + mWidth/2,mHeight/2, mHollowRadius, mTouchX >= mWidth/2 ? mWhitePaint : mGrayPaint);
        canvas.drawCircle(leftPadding + mWidth/2,mHeight/2, mSolidRadius, mTouchX >= mWidth/2 ? mLightPaint : mDefaultPaint);

        canvas.drawCircle(leftPadding + mWidth/4 * 3,mHeight/2, mHollowRadius, mTouchX >= mWidth/4 * 3 ? mWhitePaint : mGrayPaint);
        canvas.drawCircle(leftPadding + mWidth/4 * 3,mHeight/2, mSolidRadius, mTouchX >= mWidth/4 * 3 ? mLightPaint : mDefaultPaint);

        canvas.drawCircle(leftPadding + mWidth,mHeight/2, mHollowRadius, mTouchX >= mWidth ? mWhitePaint : mGrayPaint);
        canvas.drawCircle(leftPadding + mWidth,mHeight/2, mSolidRadius, mTouchX >= mWidth ? mLightPaint : mDefaultPaint);

        super.onDraw(canvas);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {

       *//*

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchX = (int) event.getX() - leftPadding;
                updateProgress();
                return true;
            case MotionEvent.ACTION_MOVE:
                mTouchX = (int) event.getX() - leftPadding;
                updateProgress();
                break;
            case MotionEvent.ACTION_UP:
                updateProgress();
                break;
        }

        return super.onTouchEvent(event);*//*
        return mGestureDetector.onTouchEvent(event);
    }*/

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

    public GestureDetector.OnGestureListener onGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("GestureDetector","onDown");
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("GestureDetector","onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("GestureDetector","onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("GestureDetector","onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("GestureDetector","onLongPress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("GestureDetector","onFling");
            return false;
        }

    };

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), onGestureListener);

}
