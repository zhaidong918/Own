package com.smiledon.own.ui.activity

import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import com.smiledon.own.R
import com.smiledon.own.base.activity.BaseActivity
import com.smiledon.own.databinding.ActivityCircularRevealBinding


/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/7/13 10:20
 */
class CircularRevealActivity : BaseActivity(){


    companion object {
        val MUSIC = 0
        val FILM = 1
    }


    private lateinit var mBinding: ActivityCircularRevealBinding

    override fun createContentView(): View {
        mBinding = inflate(R.layout.activity_circular_reveal)
        return mBinding.root
    }

    override fun initView(savedInstanceState: Bundle?) {

        mBinding.musicTv.setOnClickListener {
            if (mType != MUSIC) {
                mType = MUSIC
                switchContent()
            }
        }

        mBinding.filmTv.setOnClickListener {
            if (mType != FILM) {
                mType = FILM
                switchContent()
            }
        }

        mType = MUSIC
        mBinding.contentLayout.setBackgroundResource(R.drawable.img_music )
    }

    private var mType = -1

    private fun switchContent() {

        when (mType) {
            MUSIC->{
                mBinding.overlayLayout.setBackgroundResource(R.drawable.img_film)
                mBinding.contentLayout.setBackgroundResource(R.drawable.img_music)
                startSwitch(mBinding.musicTv.width/2, mBinding.musicTv.height/2)
            }
            FILM->{
                mBinding.overlayLayout.setBackgroundResource(R.drawable.img_music)
                mBinding.contentLayout.setBackgroundResource(R.drawable.img_film)
                startSwitch(mBinding.root.width - mBinding.filmTv.width/2, mBinding.filmTv.height/2)
            }
        }
    }

    private fun startSwitch(centerX:Int, centerY: Int) {

        // get the final radius for the clipping circle
        val dx = Math.max(centerX, mBinding.root.width - centerX)
        val dy = Math.max(centerY, mBinding.root.height - centerY)
        val finalRadius = Math.hypot(dx.toDouble(), dy.toDouble()).toFloat()

        // Android native animator
        val animator = ViewAnimationUtils.createCircularReveal(mBinding.contentLayout, centerX, centerY, 0f, finalRadius)
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = 500
        animator.start()
    }

}