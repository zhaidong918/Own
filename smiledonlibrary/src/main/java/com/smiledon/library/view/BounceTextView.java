package com.smiledon.library.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.NumberFormat;

/**
 * 数字从0 变化到目标数字
 * Created by zhaidong on 2017/9/15.
 */

public class BounceTextView extends View {

    private Paint mPaint;
    private int mHeight;

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
        mPaint.setTextSize(40f);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
    }

    NumberFormat numberFormat;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(numberFormat.format(number), 0, mHeight/2, mPaint);
    }

    float number = 0;
    ValueAnimator valueAnimator;

    public void setText(float f) {

        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(number, f);
            valueAnimator.setDuration(3000);
        }
        else{
            valueAnimator.cancel();
            valueAnimator.setFloatValues(number, f);
        }
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                number = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();

    }

}
