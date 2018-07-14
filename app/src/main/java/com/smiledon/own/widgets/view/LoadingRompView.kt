package com.smiledon.own.widgets.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.smiledon.own.R
import com.smiledon.own.utils.DisplayUtils

/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/11 17:08
 */
class LoadingRompView : View {

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyle: Int = 0) : super(context, attributeSet, defaultStyle){
        getAttr(attributeSet)
    }

    private fun getAttr(attributeSet: AttributeSet?) {
        if(attributeSet == null) return
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.WaveView)
        typedArray.recycle()
    }

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    private val mShadowColor = ContextCompat.getColor(context,R.color.background_activity)
    private val mCircleColor = ContextCompat.getColor(context,R.color.progress_color)
    private val mRectColor = ContextCompat.getColor(context,R.color.blue_color)
    private val mTriangleColor = ContextCompat.getColor(context,R.color.orange_color)

    private val mStrokeWidth = DisplayUtils.dp2px(3f).toFloat()

    init {
        mPaint.strokeWidth = mStrokeWidth
        mPaint.color = mCircleColor

        mShadowPaint.strokeWidth = mStrokeWidth
        mShadowPaint.color = mShadowColor

    }

    private var mHeight = 0f
    private var mWidth = 0f
    private var mShadowMaxWidth = DisplayUtils.dp2px(20f)
    private var mShadowMaxHeight = DisplayUtils.dp2px(5f)
    private var mShadowMinWidth = DisplayUtils.dp2px(4f)
    private var mShadowMinHeight = DisplayUtils.dp2px(1f)

    private var mRompMaxHeight = 0f
    private var mRompHeight = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()

        mRadius = mShadowMaxWidth * 2 / 5f

        //振幅 剩下控件的三分之二
        mRompMaxHeight = (mHeight - 2 * mRadius - mShadowMaxHeight / 2) * 2 / 3

        mShadowCenterPoint.x = mWidth / 2
        mShadowCenterPoint.y = mHeight - mShadowMaxHeight / 2

        mLoadingCenterPoint.x = mWidth / 2
        mLoadingCenterPoint.y = mHeight - mShadowMaxHeight / 2 - mRadius

    }

    private val mShadowRect = RectF()
    private val mRompRect = RectF()

    private val mShadowCenterPoint = PointF()
    private val mShadowOffsetPoint = PointF()

    private val mLoadingCenterPoint = PointF()

    private var type = 0
    private var mRepeatCount = 0
    private var mLoadingRotate = 0f
    private val mTrianglePath = Path()

    private var mRadius = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mShadowRect.left = mShadowCenterPoint.x - mShadowOffsetPoint.x
        mShadowRect.top = mShadowCenterPoint.y - mShadowOffsetPoint.y
        mShadowRect.right = mShadowCenterPoint.x + mShadowOffsetPoint.x
        mShadowRect.bottom = mShadowCenterPoint.y + mShadowOffsetPoint.y

        //绘制地下得阴影
        canvas.drawOval(
                mShadowRect,
                mShadowPaint
        )


        when (type) {
            0 -> {//绘制圆环滚动
                mPaint.color = mCircleColor
                canvas.drawCircle(
                        mLoadingCenterPoint.x,
                        mLoadingCenterPoint.y - mRompHeight,
                        mRadius,
                        mPaint
                )
            }

            1 -> {
                mPaint.color = mRectColor
                canvas.save()
                canvas.rotate(mLoadingRotate,
                        mLoadingCenterPoint.x,
                        mLoadingCenterPoint.y - mRompHeight)

                mRompRect.left = mLoadingCenterPoint.x - mRadius
                mRompRect.top = mLoadingCenterPoint.y - mRompHeight - mRadius
                mRompRect.right = mLoadingCenterPoint.x + mRadius
                mRompRect.bottom = mLoadingCenterPoint.y - mRompHeight + mRadius

                //绘制圆环滚动
                canvas.drawRect(
                        mRompRect,
                        mPaint
                )
                canvas.restore()
            }
            2 -> {
                mPaint.color = mTriangleColor
                canvas.save()
                canvas.rotate(mLoadingRotate,
                        mLoadingCenterPoint.x,
                        mLoadingCenterPoint.y - mRompHeight)

                mTrianglePath.reset()
                mTrianglePath.moveTo(
                        mLoadingCenterPoint.x - mRadius,
                        mLoadingCenterPoint.y - mRompHeight + mRadius)
                mTrianglePath.lineTo(
                        mLoadingCenterPoint.x + mRadius,
                        mLoadingCenterPoint.y - mRompHeight + mRadius)
                mTrianglePath.lineTo(
                        mLoadingCenterPoint.x ,
                        mLoadingCenterPoint.y - mRompHeight - mShadowMaxWidth / 3f)
                mTrianglePath.close()

                //绘制圆环滚动
                canvas.drawPath(
                        mTrianglePath,
                        mPaint
                )
                canvas.restore()

            }
        }
    }

    private var isLoading = false

    private lateinit var mLoadingAnimator: ValueAnimator

    /**
     * 加载动画
     */
    fun startLoading() {

        if(isLoading) return

        isLoading = true

        mLoadingAnimator = ValueAnimator.ofFloat(0f, mRompMaxHeight)
        mLoadingAnimator.repeatMode = ValueAnimator.REVERSE
        mLoadingAnimator.repeatCount = ValueAnimator.INFINITE
        mLoadingAnimator.interpolator = DecelerateInterpolator()
        mLoadingAnimator.duration = 500
        mLoadingAnimator.addUpdateListener{animation ->
            run {
                val fraction = animation.animatedFraction
                mShadowOffsetPoint.x = mShadowMinWidth/2 +  fraction * (mShadowMaxWidth - mShadowMinWidth)/2
                mShadowOffsetPoint.y = mShadowMinHeight/2 + fraction * (mShadowMaxHeight - mShadowMinHeight)/2

                mRompHeight = animation.animatedValue as Float

                mLoadingRotate = 20f + fraction * 250f

                postInvalidate()
            }
        }

        mLoadingAnimator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationRepeat(animation: Animator?) {
                mRepeatCount ++
                mRepeatCount %= 2
                if (mRepeatCount == 0) {
                    type ++
                    type %= 3
                }
            }
        })

        mLoadingAnimator.start()
    }
}

