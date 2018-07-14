package com.smiledon.own.widgets.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.smiledon.own.R;
import com.smiledon.own.utils.DisplayUtils;

/**
 * file description
 *
 *   ------------------------------------------
 *   |icon  Desc   inputContent       del pwd |
 *   ------------------------------------------
 *
 * @auther ZhaiDong
 * @date 2018/7/5 10:08
 */

public class CommonEditText extends android.support.v7.widget.AppCompatEditText {

    public static class Type{

        public static final int ICON = 1;
        public static final int DESCRIPTION = 1;
        public static final int PASSWORD = 1;

    }

    private Context mContext;

    public CommonEditText(Context context) {

        super(context);
        init(context);
    }

    public CommonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }


    private Bitmap mLeftIcon;
    private String mLeftDescription = "用户名";

    private Bitmap mRightDeleteIcon;
    private Bitmap mRightWarnningIcon;
    private Bitmap mRightPwdVisibleIcon;
    private Bitmap mRightPwdInVisibleIcon;

    private Paint mLeftIconPaint;
    private Paint mLeftDescriptionPaint;
    private Paint mRightTipsIconPaint;
    private Paint mRightPwdIconPaint;

    private void init(Context context) {

        setSingleLine(true);

        mContext = context;

        mLeftIconPaint = new Paint();
        mLeftIconPaint.setAntiAlias(true);

        mLeftDescriptionPaint = new Paint();
        mLeftDescriptionPaint.setAntiAlias(true);

        mRightTipsIconPaint = new Paint();
        mRightTipsIconPaint.setAntiAlias(true);

        mRightPwdIconPaint = new Paint();
        mRightPwdIconPaint.setAntiAlias(true);

        mLeftIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_user);
        mRightDeleteIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_delete);
        mRightWarnningIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_warning);
        mRightPwdVisibleIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_invisible);
        mRightPwdInVisibleIcon = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_visible);

    }

    private int mHeight;
    private int mWidth;

    private int mLeftPadding = 0;
    private int mRightPadding = 0;
    private int mTopPadding = 0;
    private int mBottomPadding = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

        mLeftPadding = getLeftPaddingOffset();
        mRightPadding = getRightPaddingOffset();
        mTopPadding = getTopPaddingOffset();
        mBottomPadding = getBottomPaddingOffset();

    }

    @Override
    public void draw(Canvas canvas) {

        drawLeftIcon(canvas);
        drawDescription(canvas);

        drawPwdIcon(canvas);
        drawOperateIcon(canvas);

        if (!isAdjustPadding) {
            setPadding(
                    mLeftPadding  + mLeftIcon.getWidth() + mDescriptionRect.width(),
                    mTopPadding,
                    mRightPadding + mRightDeleteIcon.getWidth() + mRightPwdVisibleIcon.getWidth(),
                    mBottomPadding
            );
            isAdjustPadding = true;
        }

        super.draw(canvas);

    }

    private void drawLeftIcon(Canvas canvas) {
        Rect src = new Rect(0,0, mLeftIcon.getWidth(), mLeftIcon.getHeight());
        Rect dst = new Rect(mLeftPadding, mTopPadding, mLeftPadding + mLeftIcon.getWidth(), mTopPadding + mLeftIcon.getHeight());
        canvas.drawBitmap(mLeftIcon, src, dst, null);
    }

    Rect mDescriptionRect;

    boolean isAdjustPadding = false;

    private void drawDescription(Canvas canvas) {

        mLeftDescriptionPaint.setTextSize(DisplayUtils.sp2px(14));
        mLeftDescriptionPaint.setColor(Color.parseColor("#8a8a8a"));

        //测量字符高度
        mDescriptionRect = new Rect();
        mLeftDescriptionPaint.getTextBounds(mLeftDescription, 0, mLeftDescription.length(), mDescriptionRect);

        canvas.drawText(mLeftDescription, mLeftPadding + mLeftIcon.getWidth(), mDescriptionRect.height(), mLeftDescriptionPaint);

    }

    private void drawPwdIcon(Canvas canvas) {

        Rect src = new Rect(0,0, mRightPwdVisibleIcon.getWidth(), mRightPwdVisibleIcon.getHeight());
        Rect dst = new Rect(mWidth - mRightPadding - mRightPwdVisibleIcon.getWidth(), mTopPadding, mWidth - mRightPadding, mTopPadding + mRightPwdVisibleIcon.getHeight());
        canvas.drawBitmap(mRightPwdVisibleIcon, src, dst, null);

    }

    private void drawOperateIcon(Canvas canvas) {

        Rect src = new Rect(0,0, mRightDeleteIcon.getWidth(), mRightDeleteIcon.getHeight());
        Rect dst = new Rect(mWidth - mRightPadding - mRightDeleteIcon.getWidth() - mRightPwdVisibleIcon.getWidth(), mTopPadding, mWidth - mRightPadding - mRightPwdVisibleIcon.getWidth(), mTopPadding + mRightDeleteIcon.getHeight());
        canvas.drawBitmap(mRightDeleteIcon, src, dst, null);

    }

}
