package com.smiledon.own.widgets.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.smiledon.own.R
import com.smiledon.own.utils.DisplayUtils
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/9 15:56
 */
class SubmitButton : View{

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyle : Int = 0): super(context, attributeSet, defaultStyle)

    private val mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBodyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mRightPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mStartBodyColor = ContextCompat.getColor(context,R.color.blue_color)
    private val mEndBodyColor = ContextCompat.getColor(context,R.color.white)

    init {
        mRightPaint.color = mEndBodyColor
        mRightPaint.strokeWidth = DisplayUtils.dp2px(2f).toFloat()
        mRightPaint.style = Paint.Style.STROKE
        mTextPaint.color = mEndBodyColor
        mTextPaint.textSize = DisplayUtils.dp2px(14f).toFloat()
        mBodyPaint.color = mStartBodyColor

        mCirclePaint.color = mEndBodyColor
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = DisplayUtils.dp2px(2f).toFloat()
    }

    private var mRadius: Float = 0f
    private var mBorderWidth: Float = 0f
    private var mHeight: Float = 0f
    private var mWidth: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
        mBorderWidth = measuredHeight / 12f
        mRadius = measuredHeight / 2f

        mTwoArcDistance = mWidth - mHeight
    }

    fun setButtonColor(colorId: Int) {
        mBodyPaint.color = ContextCompat.getColor(context, colorId)
        invalidate()
    }


    private var isDrawRight: Boolean = false
    private var isDrawCircle: Boolean = false
    private var isAnim: Boolean = false

    override fun onDraw(canvas: Canvas) {

        drawArc(canvas)
        drawRect(canvas)
        drawText(canvas)

        if(isDrawRight)
            drawRight(canvas)

        if(isDrawCircle)
            drawCircle(canvas)

        super.onDraw(canvas)
    }

    private var mTwoArcDistance: Float = 0f

    /**
     * 绘制左右半圆
     */
    private fun drawArc(canvas: Canvas) {
        canvas.drawArc(mWidth/2f-mTwoArcDistance/2f-mRadius, 0f, mWidth/2f-mTwoArcDistance/2f+mRadius, mHeight, 90f, 270f, true, mBodyPaint)
        canvas.drawArc(mWidth/2f+mTwoArcDistance/2f-mRadius, 0f, mWidth/2f+mTwoArcDistance/2f+mRadius, mHeight, 270f, 450f, true, mBodyPaint)
    }

    /**
     * 绘制半圆中间得长方形
     */
    private fun drawRect(canvas: Canvas) {
        canvas.drawRect(mWidth/2f-mTwoArcDistance/2f, 0f, mWidth/2f+mTwoArcDistance/2f, mHeight, mBodyPaint)
    }

    /**
     * 绘制文字
     */
    private val text = "Submit"
    private val textRect = Rect()
    private fun drawText(canvas: Canvas){
        mTextPaint.getTextBounds(text, 0, text.length, textRect)
        canvas.drawText(text, mWidth/2f - textRect.width()/2, mHeight/2f + textRect.height()/2f, mTextPaint)
    }

    private lateinit var mScale2CenterValueAnimator: ValueAnimator
    private lateinit var mDrawRightValueAnimator: ValueAnimator
    private lateinit var mDrawCircleValueAnimator: ValueAnimator
    private lateinit var mCenter2ScaleValueAnimator: ValueAnimator
    private lateinit var mAnimatorSet: AnimatorSet

    private fun initSubmitOne() {

        mScale2CenterValueAnimator = ValueAnimator.ofFloat(mWidth - mHeight, 0f)
        mScale2CenterValueAnimator.duration = 500
        mScale2CenterValueAnimator.interpolator = LinearInterpolator()
        mScale2CenterValueAnimator.addUpdateListener { animation ->
            run {
                var fraction: Float = animation.animatedFraction
                mTextPaint.alpha = ((1 - fraction) * 255).toInt()
                mTwoArcDistance = animation.animatedValue as Float
                postInvalidate()
            }
        }
    }

    private val mRightPath = Path()
    private lateinit var mRightPathMeasure: PathMeasure
    /**
     * 绘制勾勾
     */
    private fun drawRight(canvas: Canvas) {
        canvas.drawPath(mRightPath, mRightPaint)
    }

    private fun initSubmitTwo() {

        mRightPath.moveTo(mWidth/2f - mRadius / 3f, mRadius)
        mRightPath.lineTo(mWidth/2f, mRadius + mRadius / 3f)
        mRightPath.lineTo(mWidth/2f + mRadius / 3f, mRadius - mRadius / 3f)

        mRightPathMeasure = PathMeasure(mRightPath, false)

        mDrawRightValueAnimator = ValueAnimator.ofFloat(1f, 0f)
        mDrawRightValueAnimator.duration = 200
        mDrawRightValueAnimator.interpolator = LinearInterpolator()
        mDrawRightValueAnimator.addUpdateListener { animation ->
            run {
                isDrawRight = true
                val value = animation.animatedValue as Float
                mRightPaint.pathEffect = DashPathEffect(floatArrayOf(mRightPathMeasure.length,  mRightPathMeasure.length), value *  mRightPathMeasure.length)
                invalidate()
            }
        }
    }

    /**
     * 绘制进度圆
     */
    private var mCircleAngle: Float = 0f
    private fun drawCircle(canvas: Canvas) {

        canvas.drawArc(mWidth/2f - mRadius + mCirclePaint.strokeWidth,  + mCirclePaint.strokeWidth, mWidth/2f + mRadius -  + mCirclePaint.strokeWidth, mHeight -  + mCirclePaint.strokeWidth, -90f, mCircleAngle, false, mCirclePaint)
    }

    private fun initSubmitThree() {
        mDrawCircleValueAnimator = ValueAnimator.ofFloat(0f, 360f)
        mDrawCircleValueAnimator.duration = 500
        mDrawCircleValueAnimator.interpolator = LinearInterpolator()
        mDrawCircleValueAnimator.addUpdateListener { animation ->
            run {
                isDrawCircle = true
                mCircleAngle = animation.animatedValue as Float
                invalidate()
            }
        }
    }

    private fun initSubmitFour() {

        mCenter2ScaleValueAnimator = ValueAnimator.ofFloat( 0f, mWidth - mHeight)
        mCenter2ScaleValueAnimator.duration = 500
        mCenter2ScaleValueAnimator.interpolator = LinearInterpolator()
        mCenter2ScaleValueAnimator.addUpdateListener { animation ->
            run {
                isDrawCircle = false
                mTwoArcDistance = animation.animatedValue as Float
                postInvalidate()
            }
        }
    }

    fun reset() {
        Observable.timer(1, TimeUnit.SECONDS)
                .subscribe({
                    isDrawRight = false
                    mTextPaint.alpha = 255
                    postInvalidate()
                    isAnim = false
                })
    }

    fun startSubmit() {

        if(isAnim) return
        isAnim = true

        initSubmitOne()
        initSubmitTwo()
        initSubmitThree()
        initSubmitFour()

        mAnimatorSet = AnimatorSet()
        mAnimatorSet.playSequentially(mScale2CenterValueAnimator, mDrawCircleValueAnimator, mCenter2ScaleValueAnimator, mDrawRightValueAnimator)


        mAnimatorSet.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                reset()
            }
        })

        mAnimatorSet.start()
    }

}