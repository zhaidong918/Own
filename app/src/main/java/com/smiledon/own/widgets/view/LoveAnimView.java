package com.smiledon.own.widgets.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.smiledon.own.R;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/5 16:51
 */

public class LoveAnimView extends View{

    public LoveAnimView(Context context) {
        this(context, null);
    }

    public LoveAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoveAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private static final int PATH_WIDTH = 2;
    // 起始点
    private static final int[] START_POINT = new int[] {
            300, 270
    };
    // 爱心下端点
    private static final int[] BOTTOM_POINT = new int[] {
            300, 370
    };
    // 左侧控制点
    private static final int[] LEFT_CONTROL_POINT = new int[] {
            450, 200
    };
    // 右侧控制点
    private static final int[] RIGHT_CONTROL_POINT = new int[] {
            150, 200
    };

    private PathMeasure mPathMeasure;
    private Paint mPaint;
    private Path mPath;
    private float[] mCurrentPosition = new float[2];


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(PATH_WIDTH);
        mPaint.setColor(Color.RED);
        mPaint.setShader(new LinearGradient(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], Color.RED, Color.BLUE, Shader.TileMode.CLAMP));

        mPath = new Path();
        mPath.moveTo(START_POINT[0], START_POINT[1]);
        mPath.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], BOTTOM_POINT[0],
                BOTTOM_POINT[1]);
        mPath.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], START_POINT[0], START_POINT[1]);

        mPathMeasure = new PathMeasure(mPath, true);
        mCurrentPosition = new float[2];


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.photo);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mTextPaint.setShader(bitmapShader);
        mTextPaint.setTextSize(100);
        mTextPaint.setStrokeWidth(50);

    }
    Paint mTextPaint = new Paint();
    String text = "love";

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], 5, mPaint);
        canvas.drawCircle(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], 5, mPaint);

        // 绘制对应目标
        canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, mPaint);

        if (!isStartAnim) {
            isStartAnim = true;
            startPathAnim(1000);
        }

        canvas.drawText(text,200,400, mTextPaint);
    }

    boolean isStartAnim = false;

    // 开启路径动画
    public void startPathAnim(long duration) {

        // 0 － getLength()
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // 减速插值器
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();
            // 获取当前点坐标封装到mCurrentPosition
            mPathMeasure.getPosTan(value, mCurrentPosition, null);
            postInvalidate();
        });
        valueAnimator.start();

    }

}
