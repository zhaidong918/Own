package com.smiledon.own.widgets.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import com.smiledon.own.utils.DisplayUtils

/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/11 11:36
 */
class MeasureSpecView: View{

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defaultStyle : Int = 0): super(context, attributeSet, defaultStyle)


    private val mContent = "测量一下，这个要好长"
    private val mContentRect = Rect()
    private val mContentPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    init {
        mContentPaint.color = Color.BLUE
        mContentPaint.textSize = DisplayUtils.dp2px(16f).toFloat()
        mContentPaint.getTextBounds(mContent, 0, mContent.length, mContentRect)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        measureWidth(widthMeasureSpec)
        measureHeight(heightMeasureSpec)

        setMeasuredDimension(mWidth, mHeight)

    }

    private fun measureWidth(widthMeasureSpec: Int) {

        var specSize = MeasureSpec.getSize(widthMeasureSpec)
        var specMode = MeasureSpec.getMode(widthMeasureSpec)

        when (specMode) {
            MeasureSpec.AT_MOST->{
                mWidth = mContentRect.width() + paddingLeft + paddingRight
            }
            MeasureSpec.EXACTLY->{
                mWidth = specSize
            }
            MeasureSpec.UNSPECIFIED->{
                mWidth = Math.max(suggestedMinimumWidth, specSize)
            }
        }
    }

    private fun measureHeight(heightMeasureSpec: Int){

        var specSize = MeasureSpec.getSize(heightMeasureSpec)
        var specMode = MeasureSpec.getMode(heightMeasureSpec)

        when (specMode) {
            //自适应 wrap_content
            MeasureSpec.AT_MOST->{
                //粗略高度
                mHeight= mContentRect.height() + paddingTop + paddingBottom
                //精确高度
                mHeight= (-mContentPaint.ascent() + mContentPaint.descent() + paddingTop + paddingBottom).toInt()
            }
            //固定数值
            MeasureSpec.EXACTLY->{
                mHeight = specSize
            }
            MeasureSpec.UNSPECIFIED->{
                mHeight = Math.max(suggestedMinimumWidth, specSize)
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(mContent, (width/2 - mContentRect.width()/2).toFloat(), (height/2 + mContentRect.height()/2).toFloat(), mContentPaint)
    }

}