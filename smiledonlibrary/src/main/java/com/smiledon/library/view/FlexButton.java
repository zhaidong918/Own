package com.smiledon.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by zhaidong on 2017/9/9.
 */

public class FlexButton extends ViewGroup {

    public static final float RADIUS_DISTANCE =10;

    private Paint mPaint;

    private int mWidth = 0;
    private int mHeight = 0;
    private float radius = 0;

    private float ratio;        //小圆与大圆的边距和可伸缩距离的比例

    private float defineRadiusDistance;
    private float radiusDistance = RADIUS_DISTANCE; //大圆和小圆的半径差
    private float smallRadius = 0;

    private RectF mRectF;
    private float rectLeftX = 0;
    private float rectRightX = 0;

    private boolean isOnfold = true;  //true  展开状态  false 折叠状态
    private boolean isFlexing = false;  //true  正在伸缩
    private float flexDistanceOnce = 30;     //单次移动的距离
    private float flexDistance;             //可伸缩的距离

    private Rect eventRect;

    public FlexButton(Context context) {
        this(context, null);
    }

    public FlexButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlexButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

//        setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startFlex();
//            }
//        });
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            isFlexing = true;

            //折叠的距离为 mWidth - 2*radius
            //当前状态为展开
            if(isOnfold){
//                rectRightX = rectRightX - (mWidth - 2*radius)/100;
                rectRightX -= flexDistanceOnce;
                if (rectRightX > radius) {
                    radiusDistance = (rectRightX - radius) * ratio;
                    handler.sendEmptyMessageDelayed(0, 1);
                }
                else {
                    radiusDistance = 0;
                    rectRightX = radius;
                    isOnfold = false;
                    isFlexing = false;
                }
            }
            //当前状态为收缩
            else {
//                rectRightX = rectRightX + (mWidth - 2*radius)/100;
                rectRightX += flexDistanceOnce;
                if (rectRightX < mWidth - radius) {
                    radiusDistance = (rectRightX - radius) * ratio;
                    handler.sendEmptyMessageDelayed(0, 1);
                } else {
                    radiusDistance = defineRadiusDistance;
                    rectRightX = mWidth - radius;
                    isOnfold = true;
                    isFlexing = false;
                }
            }
            invalidate();
        }
    };



    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        startFlex();


        if(eventRect.contains((int)event.getX(), (int)event.getY())){
            startFlex();
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(mWidth == 0) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec);
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
            radius = mHeight / 2;
            radiusDistance = radius*0.3f;
            defineRadiusDistance = radiusDistance;

            rectLeftX = radius;
            rectRightX = mWidth - radius;

            flexDistance = mWidth - 2*radius;
            ratio = radiusDistance/flexDistance;

            eventRect = new Rect(0, 0, mHeight, mHeight);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {

        //左侧圆
        mPaint.setColor(Color.GRAY);
        canvas.drawCircle(radius, radius, radius, mPaint);

        //中间矩形
        mRectF = new RectF(rectLeftX, 0, rectRightX, mHeight);
        canvas.drawRect(mRectF, mPaint);
        //右侧圆
        canvas.drawCircle(rectRightX, radius, radius, mPaint);

        //左侧小圆
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(radius, radius, radius - radiusDistance, mPaint);

        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void startFlex(){
        if(!isFlexing)
            handler.sendEmptyMessageDelayed(0,30);
    }

}

