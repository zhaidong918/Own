package com.smiledon.own.widgets.view

import android.animation.*
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.smiledon.own.R
import com.smiledon.own.utils.DisplayUtils

/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/11 17:08
 */
class LoadingCircleView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttr(attrs)
        initPaint()
    }


    private var mDefaultCircleColor = 0
    private var mDefaultLoadingColor = 0
    private var mLoadingOkColor = 0
    private var mLoadingErrorColor = 0
    private var mStrokeWidth = 0f

    private fun initAttr(attributeSet: AttributeSet?) {
        if(attributeSet == null) return

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LoadingCircleView)
        mDefaultCircleColor = typedArray.getColor(R.styleable.LoadingCircleView_default_circle_color, Color.parseColor("#dddee1"))
        mDefaultLoadingColor = typedArray.getColor(R.styleable.LoadingCircleView_loading_arc_color, Color.parseColor("#25262e"))
        mLoadingOkColor = typedArray.getColor(R.styleable.LoadingCircleView_loading_ok_color, Color.parseColor("#24f694"))
        mLoadingErrorColor = typedArray.getColor(R.styleable.LoadingCircleView_loading_error_color, Color.parseColor("#e76e43"))

        mStrokeWidth = typedArray.getDimension(R.styleable.LoadingCircleView_circle_width, DisplayUtils.dp2px(3f).toFloat())

        typedArray.recycle()
    }

    private var mDefaultCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLoadingArcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mLoadingCompletePaint = Paint(Paint.ANTI_ALIAS_FLAG)

   private fun initPaint(){

        mDefaultCirclePaint.strokeWidth = mStrokeWidth
        mDefaultCirclePaint.style = Paint.Style.STROKE
        mDefaultCirclePaint.color = mDefaultCircleColor

        mLoadingArcPaint.strokeWidth = mStrokeWidth
        mLoadingArcPaint.style = Paint.Style.STROKE
        mLoadingArcPaint.color = mDefaultLoadingColor

        mLoadingCompletePaint.strokeWidth = mStrokeWidth
        mLoadingCompletePaint.style = Paint.Style.STROKE
        mLoadingCompletePaint.color = mLoadingOkColor
    }

    //circle 半径
    private var mRadius = 0f
    //圆弧的起点和弧度
    private var mStartAngle = 0f
    private var mSweepAngle = 0f
    //视图的最中心
    private val mCenterPoint = PointF()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //视图中心
        mCenterPoint.x = measuredWidth.toFloat()/2
        mCenterPoint.y = measuredHeight.toFloat()/2

        //计算最大圆半径
        val mWidth= measuredWidth.toFloat() - paddingLeft - paddingRight
        val mHeight = measuredHeight.toFloat() - paddingTop - paddingBottom
        mRadius = Math.min(mWidth, mHeight)/2

    }

    //用于判断loading结束 开始绘制 √ ×
    private var isLoadingComplete = false
    //用于判断loading结束 绘制 √ 还是 ×
    private var isLoadingOk= false
    // √ × 的绘制路径
    private var mCompletePath = Path()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //绘制默认圆
        canvas.drawCircle(
                mCenterPoint.x,
                mCenterPoint.y,
                mRadius - mStrokeWidth / 2, // mRadius与mStrokeWidth的关系  一半圆内 一半圆外
                mDefaultCirclePaint
        )

        //绘制圆环滚动
        canvas.drawArc(
                mCenterPoint.x - mRadius + mStrokeWidth / 2,
                mCenterPoint.y - mRadius + mStrokeWidth / 2,
                mCenterPoint.x + mRadius - mStrokeWidth / 2,
                mCenterPoint.y + mRadius - mStrokeWidth / 2,
                mStartAngle,
                mSweepAngle,
                false,
                mLoadingArcPaint
        )

        //绘制加载完成
        if (isLoadingComplete) {
            canvas.drawPath(mCompletePath, mLoadingCompletePaint)
        }
    }

    //加载动画
    private var mLoadingAnimatorSet = AnimatorSet()
    //加载完成动画
    private var mLoadingCompleteAnimatorSet = AnimatorSet()

    private lateinit var mLoadingArcStartAngleAnimator: ValueAnimator
    /**
     * 旋转开始点
     */
    private fun initLoadingArcAnim() {

        mLoadingArcStartAngleAnimator = ValueAnimator.ofFloat(0f, 360f)
        mLoadingArcStartAngleAnimator.duration = 1000
        mLoadingArcStartAngleAnimator.repeatCount = ValueAnimator.INFINITE
        mLoadingArcStartAngleAnimator.interpolator = LinearInterpolator()
        mLoadingArcStartAngleAnimator.addUpdateListener { animation ->
            run {
                mStartAngle = animation.animatedValue as Float
                mLastStartAngle = mStartAngle
                invalidate()
            }
        }
    }


    //记录最后一次 绘制的七点
    private var mLastSweepAngle = 0f
    //记录最后一次 弧度
    private var mLastStartAngle = 0f

    private lateinit var mLoadingArc2Animator: ValueAnimator

    /**
     * 旋转跨度
     */
    private fun initLoadingArcSweepAngleAnim() {

        mLoadingArc2Animator = ValueAnimator.ofFloat(15f, 135f)
        mLoadingArc2Animator.duration = 1000
        mLoadingArc2Animator.repeatCount = ValueAnimator.INFINITE
        mLoadingArc2Animator.repeatMode = ValueAnimator.REVERSE
        mLoadingArc2Animator.interpolator = LinearInterpolator()
        mLoadingArc2Animator.addUpdateListener { animation ->
            run {
                mSweepAngle = animation.animatedValue as Float
                mLastSweepAngle = mSweepAngle
            }
        }
    }

    private lateinit var mLoadingArcEndAnimator: ValueAnimator

    /**
     * 过度颜色 将默认的进度变为0后 改成对应的颜色
     */
    private fun initLoadingArcEndAnimator() {

        mLoadingArcEndAnimator = ValueAnimator.ofFloat(0f, 1f)
        //保证整体动画速度和前面一致  mLastSweepAngle * 1.5 保证弧度的结束点持续运动而不是固定的
        mLoadingArcEndAnimator.duration = (mLastSweepAngle * 1.5 * 1000/ 360).toLong()
        mLoadingArcEndAnimator.interpolator = LinearInterpolator()
        mLoadingArcEndAnimator.addUpdateListener { animation ->
            run {
                //从上次动画的起点和弧度开始变化
                mStartAngle = mLastStartAngle + animation.animatedFraction * mLastSweepAngle * 1.5f
                mSweepAngle = mLastSweepAngle * (1 - animation.animatedFraction)
                invalidate()
            }
        }

        mLoadingArcEndAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                updateLoadingColor()
            }
        })
    }

    /**
     * 更新画笔颜色
     */
    private fun updateLoadingColor() {
        mLoadingCompletePaint.color = if(isLoadingOk) mLoadingOkColor else mLoadingErrorColor
        mLoadingArcPaint.color = if(isLoadingOk) mLoadingOkColor else mLoadingErrorColor
    }


    private lateinit var mLoadingCircleAnimator: ValueAnimator

    /**
     * 加载完成动画
     */
    private fun initLoadingCircleAnimator() {

        mLoadingCircleAnimator = ValueAnimator.ofFloat(0f, 360f)
        mLoadingCircleAnimator.duration = 1000
        mLoadingCircleAnimator.interpolator = LinearInterpolator()
        mLoadingCircleAnimator.addUpdateListener { animation ->
            run {
                //从上次动画的起点和弧度开始增加 （上一个动画会将mSweepAngle 变为0）
                mSweepAngle = animation.animatedValue as Float
                invalidate()
            }
        }

        mLoadingCircleAnimator.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                isLoadingComplete = true
            }
        })
    }

    private lateinit var mLoadingCompleteAnimator: ValueAnimator

    /**
     * 加载完成动画 绘制 √ ×
     */
    private fun initLoadingCompleteAnimator() {

        mCompletePath.reset()

        when (isLoadingOk) {
        //√√√√√√√√√√√√√√√√√√√√√√√√√√√√
            true -> {
                mCompletePath.moveTo(
                        mCenterPoint.x - mRadius / 4f,
                        mCenterPoint.y
                )
                mCompletePath.lineTo(
                        mCenterPoint.x,
                        mCenterPoint.y + mRadius / 4f
                )
                mCompletePath.lineTo(
                        mCenterPoint.x + mRadius / 4f,
                        mCenterPoint.y - mRadius / 4f
                )
            }
        // ×××××××××××××××××××××××××××
            false -> {
                mCompletePath.moveTo(
                        mCenterPoint.x - mRadius / 4f,
                        mCenterPoint.y - mRadius / 4f
                )
                mCompletePath.lineTo(
                        mCenterPoint.x + mRadius / 4f,
                        mCenterPoint.y + mRadius / 4f
                )

                mCompletePath.moveTo(
                        mCenterPoint.x - mRadius / 4f,
                        mCenterPoint.y + mRadius / 4f

                )
                mCompletePath.lineTo(
                        mCenterPoint.x + mRadius / 4f,
                        mCenterPoint.y - mRadius / 4f
                )
            }
        }


        var mPathMeasure = PathMeasure(mCompletePath, false)

        mLoadingCompleteAnimator = ValueAnimator.ofFloat(1f, 0f)
        mLoadingCompleteAnimator.duration = 200
        mLoadingCompleteAnimator.interpolator = LinearInterpolator()
        mLoadingCompleteAnimator.addUpdateListener { animation ->
            run {
                val value = animation.animatedValue as Float
                mLoadingCompletePaint.pathEffect = DashPathEffect(floatArrayOf(mPathMeasure.length,  mPathMeasure.length), value *  mPathMeasure.length)
                invalidate()
            }
        }
    }

    /**
     * 加载动画
     */
    fun startLoading() {

        isLoadingComplete = false
        mLoadingArcPaint.color = mDefaultLoadingColor

        mLoadingAnimatorSet.cancel()

        initLoadingArcAnim()
        initLoadingArcSweepAngleAnim()

        mLoadingAnimatorSet.playTogether(mLoadingArcStartAngleAnimator, mLoadingArc2Animator)
        mLoadingAnimatorSet.start()
    }

    /**
     * 加载完成动画
     */
    fun startLoadingComplete(isOk: Boolean) {

        mLoadingAnimatorSet.cancel()
        mLoadingCompleteAnimatorSet.cancel()

        isLoadingOk = isOk

        initLoadingArcEndAnimator()
        initLoadingCircleAnimator()
        initLoadingCompleteAnimator()

        mLoadingCompleteAnimatorSet.playSequentially(mLoadingArcEndAnimator, mLoadingCircleAnimator, mLoadingCompleteAnimator)

        mLoadingCompleteAnimatorSet.start()
    }


}

