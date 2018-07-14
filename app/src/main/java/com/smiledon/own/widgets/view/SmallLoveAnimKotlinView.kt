package com.smiledon.own.widgets.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.smiledon.own.R

/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/9 10:39
 */
class SmallLoveAnimKotlinView: FrameLayout {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)

    private val mLoveList: ArrayList<Love> = ArrayList()

    private var mBitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.love)

    init {
        setWillNotDraw(false)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        when(ev.action){
            MotionEvent.ACTION_DOWN -> {

                val love = Love()

                val floatArray = floatArrayOf(ev.rawX, ev.rawY)
                love.point = floatArray

                love.paint = Paint(Paint.ANTI_ALIAS_FLAG)

                var endX = (Math.random() * mScreenWidth).toFloat()
                var endY = -mBitmap.height.toFloat()

                val path = Path()
                path.moveTo(ev.rawX, ev.rawY)
                path.lineTo(endX, endY)

                mLoveList.add(love)

                startAnim(love, path)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun startAnim(love: Love, path: Path) {
        val pathMeasure = PathMeasure(path, false)
        val valueAnimator =  ValueAnimator.ofFloat(0f, pathMeasure.length)
        valueAnimator.duration = 1500
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener { animation ->
            run {
                love.paint.alpha = ((1 - animation.animatedFraction * 0.8) * 255).toInt()
                val value: Float = animation.animatedValue as Float

                pathMeasure.getPosTan(value, love.point, null)
                postInvalidate()
            }
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                mLoveList.remove(love)
            }
        })
        valueAnimator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (love in mLoveList) {
            drawRotateBitmap(canvas, love.paint, mBitmap, love.rotation, love.point[0] - mBitmap.width / 2, love.point[1] - mBitmap.height / 2)
        }
    }

    private fun drawRotateBitmap(canvas: Canvas, paint: Paint, bitmap: Bitmap, rotation: Int, posX: Float, posY: Float){

        val matrix = Matrix()
        val offsetX = bitmap.width / 2
        val offsetY = bitmap.height / 2

        matrix.postTranslate((-offsetX).toFloat(), (-offsetY).toFloat())
        matrix.postRotate(rotation.toFloat())
        matrix.postTranslate(posX + offsetX, posY + offsetY)
        canvas.drawBitmap(bitmap, matrix, paint)
    }

    private val mScreenWidth:Int
        get() {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            var displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

    inner class Love{

        lateinit var point: FloatArray

        lateinit var paint: Paint

        var rotation: Int = 0

    }

}