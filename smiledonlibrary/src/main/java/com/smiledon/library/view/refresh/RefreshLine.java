package com.smiledon.library.view.refresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * Created by zhaidong on 2017/9/13.
 */

public class RefreshLine extends View {


    public static final int LINE_SHRINK = 1;
    public static final int DOT_MOVE = 2;
    public static final int DOT_DEFAULT = 0;

    private int lineStaus = DOT_DEFAULT;

    private float slideRatio = 0.3f;

    private Paint mPaint;
    private float lineWidth = 10;
    private float lineHeight;

    private float margin = 10;

    private float dotMoveDis;

    private float startY;
    private float endY;
    private RectF mRectF;

    public RefreshLine(Context context) {
        this(context, null);
    }

    public RefreshLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        lineHeight = MeasureSpec.getSize(heightMeasureSpec) - 2*margin;

        endY = lineWidth;
        startY = 0;
        dotMoveDis = 0.5f*lineHeight;
        animDotX = 10*lineWidth;
        mPos = new float[]{animDotX, lineHeight/2};

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Path mPath;
    private PathMeasure mPathMeasure;
    private float[] mPos;

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.BLUE);
        mRectF = new RectF(0,startY,lineWidth, endY);
        canvas.drawRoundRect(mRectF, lineWidth/2f, lineWidth/2f, mPaint);

        mPaint.setColor(Color.GRAY);
        mRectF = new RectF(2*lineWidth,startY,3*lineWidth, endY);
        canvas.drawRoundRect(mRectF, lineWidth/2f, lineWidth/2f, mPaint);

        mPaint.setColor(Color.GRAY);
        mRectF = new RectF(4*lineWidth,startY,5*lineWidth, endY);
        canvas.drawRoundRect(mRectF, lineWidth/2f, lineWidth/2f, mPaint);

        mPaint.setColor(Color.BLUE);
        mRectF = new RectF(6*lineWidth,startY,7*lineWidth, endY);
        canvas.drawRoundRect(mRectF, lineWidth/2f, lineWidth/2f, mPaint);


        mPath = new Path();
        mPath.moveTo(animDotX, lineHeight/2);
        mPath.lineTo(animDotX, 0);
        mPath.lineTo(animDotX, lineHeight);
        mPath.lineTo(animDotX, lineHeight/2);

        mPathMeasure = new PathMeasure(mPath, false);
        canvas.drawCircle(mPos[0], mPos[1], lineWidth/2, mPaint);

        super.onDraw(canvas);
    }

    private float animDotX;

    // 开启路径动画
    public void startPathAnim(long duration) {
        // 0 － getLength()
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        // 减速插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(1);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                mPathMeasure.getPosTan(value, mPos, null);
                postInvalidate();
            }
        });

        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mHandler.sendEmptyMessageDelayed(lineStaus, 20);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        valueAnimator.start();

    }

    public void moveLineView(float y){

        y *= slideRatio;

        if(y <0 || y > lineHeight || lineStaus != DOT_DEFAULT) return;

        if(y <= dotMoveDis){
            startY = y;
        }
        endY = y + lineWidth;
        invalidate();

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            endY -= 2*lineWidth;

            switch (msg.what) {
                case LINE_SHRINK:
                    if(endY < dotMoveDis) {
                        lineStaus = DOT_MOVE;
                        endY = dotMoveDis;
                        startY = dotMoveDis - lineWidth;
                        startPathAnim(1000);
                    }
                    else {
                        mHandler.sendEmptyMessageDelayed(lineStaus, 1);
                        invalidate();
                    }
                    invalidate();
                    break;
                case DOT_MOVE:
                    invalidate();
                    startY -= 2*lineWidth;
                    if(endY < lineWidth || startY < 0) {
                        endY = lineWidth;
                        startY = 0;
                        lineStaus = DOT_DEFAULT;
                    }
                    else
                        mHandler.sendEmptyMessageDelayed(lineStaus, 1);

                    invalidate();
                    break;
            }

        }
    };

    public void startRefresh(float y){

        y *= slideRatio;

        if(y < dotMoveDis)
            lineStaus = DOT_MOVE;
        else
            lineStaus = LINE_SHRINK;
        mHandler.sendEmptyMessageDelayed(lineStaus, 20);
    }

    public int getLineStatus(){
        return lineStaus;
    }

}
