package com.smiledon.own.widgets.view

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.smiledon.own.R


/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/9 15:56
 */
class WaveView : View{

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyle: Int = 0) : super(context, attributeSet, defaultStyle){
        getAttr(attributeSet)
    }

    companion object {
        val TOP = 0
        val BOTTOM = 1
    }


    private val mBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    init {
        mBorderPaint.color = ContextCompat.getColor(context,R.color.blue_color)

    }

    fun setWaveColor(colorId: Int) {
        mBorderPaint.color = ContextCompat.getColor(context,colorId)
        invalidate()
    }

    private fun getAttr(attributeSet: AttributeSet?) {
        if(attributeSet == null) return
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.WaveView)
        waveFillType = typedArray.getInt(R.styleable.WaveView_waveFillType, BOTTOM)
        typedArray.recycle()
    }

    private var mHeight: Float = 0f
    private var mWidth: Float = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()

        waveLength = measuredWidth/waveCount

    }

    private var mPath = Path()

    private var offSet: Float = 0f
    private var waveCount: Int = 2
    private var waveLength: Int = 0

    private var waveFillType = BOTTOM

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.reset()

        mPath.moveTo(offSet, mHeight / 2)

        for (index in 0..waveCount) {
            //取振幅最大值当作控制点
            mPath.quadTo(
                    offSet + index * waveLength + waveLength / 4,
                    0f,
                    offSet + index * waveLength + waveLength / 2,
                    mHeight / 2)

            mPath.quadTo(
                    offSet + index * waveLength + waveLength * 3 / 4,
                    mHeight,
                    offSet + index * waveLength + waveLength,
                    mHeight / 2)
        }


        when (waveFillType) {
            TOP -> {
                mPath.lineTo(mWidth, 0f)
                mPath.lineTo(0f, 0f)
            }
            BOTTOM -> {
                mPath.lineTo(mWidth, mHeight)
                mPath.lineTo(0f, mHeight)
            }
        }

        mPath.close()

        canvas.drawPath(mPath, mBorderPaint)

        if (!isInitAnimator) {
            initSubmitOne()
            isInitAnimator = true
        }
    }

    private var isInitAnimator = false
    private lateinit var mAnimator: ValueAnimator
    private fun initSubmitOne() {

        mAnimator = ValueAnimator.ofFloat(waveLength.toFloat(), 0f)
        mAnimator.duration = 2000
        mAnimator.repeatCount = ValueAnimator.INFINITE
        mAnimator.interpolator = LinearInterpolator()
        mAnimator.addUpdateListener { animation ->
            run {
                offSet = animation.animatedValue as Float
                offSet *= -1
                postInvalidate()
            }
        }
        mAnimator.start()
    }

    //TODO....
    fun stop() {
        mAnimator.cancel()
    }

    fun pause() {
        mAnimator.pause()
    }

    fun resume() {
        mAnimator.resume()
    }

}