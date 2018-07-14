package com.smiledon.own.widgets.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.smiledon.own.R;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/4 15:01
 */

public class AnimView extends View {

    public AnimView(Context context) {
        this(context, null);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    Paint mPaint;
    Paint mBgPaint;
    Paint mScanPaint;
    int mStartColor;
    int mEndColor;
    int mAlphaWhiteColor;

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        mPaint.setAntiAlias(true);

        mStartColor = ContextCompat.getColor(getContext(), R.color.blue_start);
        mEndColor = ContextCompat.getColor(getContext(), R.color.blue_end);
        mAlphaWhiteColor = ContextCompat.getColor(getContext(), R.color.white_alpha7);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);

        mScanPaint = new Paint();
        mScanPaint.setAntiAlias(true);
    }


    int mDiameter;
    int mRadius;
    int mCircleCount = 3;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mDiameter = Math.min(getMeasuredHeight(), getMeasuredWidth());
        mRadius = mDiameter / 2;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        mPaint.setColor(mEndColor);
        canvas.drawRect(0, 0, mDiameter, mDiameter, mPaint);

        Shader shader = new RadialGradient(mDiameter / 2, mDiameter / 2, mDiameter / 2, mStartColor, mEndColor, Shader.TileMode.CLAMP);
        mBgPaint.setShader(shader);
        canvas.drawCircle(mDiameter / 2, mDiameter / 2, mDiameter / 2 , mBgPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.WHITE);

        for (int i = 0; i < mCircleCount; i++) {
            mPaint.setAlpha(255 * (i + 1) / mCircleCount);
            int count = mCircleCount * 2 -1;
            mPaint.setStrokeWidth((i + 1) * 3);
            canvas.drawCircle(mDiameter / 2, mDiameter / 2, (mDiameter * (count - i) / count - mDiameter * i/count) / 2, mPaint);
        }

        mScanPaint.setShader(new SweepGradient(mDiameter/2, mDiameter/2, new int[] { mAlphaWhiteColor, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT}, null));
        canvas.drawCircle(mDiameter / 2, mDiameter / 2, mDiameter / 2 , mScanPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mAlphaWhiteColor);
        mPaint.setShader(new LinearGradient(0, 0, mDiameter / 2, mDiameter / 2, Color.RED, Color.BLACK, Shader.TileMode.CLAMP));
        canvas.drawArc(0, 0, mDiameter, mDiameter, agree, 20, true, mPaint);

        if (agree == -1) {
            startAnim();
        }

    }

    int agree = -1;

    public void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(animation -> {
            agree = (int) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();
    }

}
