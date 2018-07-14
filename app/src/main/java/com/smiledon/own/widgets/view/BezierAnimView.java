package com.smiledon.own.widgets.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.smiledon.own.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/5 16:51
 */

public class BezierAnimView extends View{

    public BezierAnimView(Context context) {
        this(context, null);
    }

    public BezierAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    Paint mDotPaint;
    Paint mLinePaint;
    List<Path> mLineList;
    List<Point> mPointList;
    Point mTouchPoint;

    float mRadius = 20f;

    private void init() {

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointList = new ArrayList<>();
        mLineList = new ArrayList<>();

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(DisplayUtils.dp2px(1f));
        mLinePaint.setColor(Color.BLUE);
        mLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point point : mPointList) {
            canvas.drawCircle(point.x, point.y, mRadius, mDotPaint);
        }
//        绘制抛物线轨迹
//        for (Path path : mLineList) {
//            canvas.drawPath(path, mLinePaint);
//        }
    }

    int count = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {
            count++;

            if (count < 66) {
                mTouchPoint = new Point((int)event.getRawX(), (int)event.getRawY());
                mPointList.add(mTouchPoint);
                Path path = new Path();
                path.moveTo(mTouchPoint.x, mTouchPoint.y);
                mLineList.add(path);
                startAnim(mTouchPoint, path);
            }
        }
        return true;
    }

    private void startAnim(Point startPoint, Path path) {
        Point endPoint = new Point(200, 1600);

        Point controlPoint = new Point();
        controlPoint.x = (startPoint.x + endPoint.x) / 2;
        controlPoint.y = startPoint.y - DisplayUtils.dp2px(100);

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(controlPoint), startPoint, endPoint);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(animation -> {
            animation.getAnimatedFraction();
            Point point = (Point) animation.getAnimatedValue();
            startPoint.x = point.x;
            startPoint.y = point.y;
            path.lineTo(point.x, point.y);
            invalidate();
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPointList.remove(startPoint);
                mLineList.remove(path);
                count--;
            }
        });
        valueAnimator.start();
    }

    /**
     * 抛物线
     */
    public class BezierEvaluator implements TypeEvaluator<Point> {

        private Point controlPoint;

        public BezierEvaluator(Point controlPoint) {
            this.controlPoint = controlPoint;
        }

        @Override
        public Point evaluate(float t, Point startValue, Point endValue) {
            int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controlPoint.x + t * t * endValue.x);
            int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controlPoint.y + t * t * endValue.y);

            return new Point(x, y);
        }
    }


}
