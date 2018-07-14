package com.smiledon.own.widgets.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import com.smiledon.own.R;

import java.util.ArrayList;
import java.util.List;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/5 16:51
 */

public class SmallLoveAnimView extends FrameLayout{


    public SmallLoveAnimView(Context context) {
        this(context, null);
    }

    public SmallLoveAnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmallLoveAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    List<Love> loveList;

    List<float[]> mLineList;
    List<Paint> mPaintList;

    Bitmap mBitmap;

    private void init() {

        setWillNotDraw(false);

        mLineList = new ArrayList<>();
        mPaintList = new ArrayList<>();

        loveList = new ArrayList<>();

        mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.love);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Love love : loveList) {
            drawRotateBitmap(canvas, love.paint, mBitmap, love.rotate, love.point[0] - mBitmap.getWidth()/2, love.point[1] - mBitmap.getHeight()/2);
//            canvas.drawBitmap(mBitmap, love.point[0] - mBitmap.getWidth()/2, love.point[1] - mBitmap.getHeight()/2, love.paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {

            float[] floats = new float[2];
            floats[0] = event.getRawX();
            floats[1] = event.getRawY();

            Love love = new Love();
            love.point = floats;
            love.paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            Path path = new Path();
            path.moveTo(event.getRawX(), event.getRawY());
            path.lineTo((int) (Math.random() * getScreenWidth()), -10);

            //旋转度数
//            love.rotate = (int) (180 * Math.random() - 90);
            love.rotate = 0;

            loveList.add(love);

            startAnim(love, path);
        }
        return super.dispatchTouchEvent(event);
    }


    private void startAnim(Love love, Path path) {

        PathMeasure pathMeasure = new PathMeasure(path, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            love.paint.setAlpha((int) ((1 - animation.getAnimatedFraction() * 0.8)  * 255));
            float value = (Float) animation.getAnimatedValue();
            // 获取当前点坐标封装到mCurrentPosition
            pathMeasure.getPosTan(value, love.point, null);
            postInvalidate();
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                loveList.remove(love);
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

    /**
     * 绘制自旋转位图
     *
     * @param canvas
     * @param paint
     * @param bitmap
     *            位图对象
     * @param rotation
     *            旋转度数
     * @param posX
     *            在canvas的位置坐标
     * @param posY
     */
    private void drawRotateBitmap(Canvas canvas, Paint paint, Bitmap bitmap,
                                  float rotation, float posX, float posY) {
        Matrix matrix = new Matrix();
        int offsetX = bitmap.getWidth() / 2;
        int offsetY = bitmap.getHeight() / 2;
        matrix.postTranslate(-offsetX, -offsetY);
        matrix.postRotate(rotation);
        matrix.postTranslate(posX + offsetX, posY + offsetY);
        canvas.drawBitmap(bitmap, matrix, paint);
    }


    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    class Love {
        public float[] point;
        public Paint paint;
        public int rotate;
    }

}
