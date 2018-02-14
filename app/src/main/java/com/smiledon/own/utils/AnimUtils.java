package com.smiledon.own.utils;

import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;

/**
 * @author East Chak
 * @date 2018/1/15 19:21
 */

public class AnimUtils {

    public static void alphaAnim(View view) {

        alphaAnim(view, View.VISIBLE);
    }


    public static void alphaAnim(View view, int visible) {

        float startAlpha = visible == View.VISIBLE ? 0f : 1.0f;
        float endAlpha = visible == View.VISIBLE ? 1.0f : 0f;

        AlphaAnimation alphaAnimation = new AlphaAnimation(startAlpha, endAlpha);
        alphaAnimation.setDuration(200);
        view.setAnimation(alphaAnimation);
    }

    public static void alphaAnim(View view, int visible, boolean isAnim) {

        view.setVisibility(visible);

        if (isAnim) {
            alphaAnim(view,visible);
        }

    }

    public static void scaleAnim(View view, int visible) {
        view.setAnimation(AnimationUtils.loadAnimation(AppApplication.getInstance(), visible == View.VISIBLE ? R.anim.pop_down_anim : R.anim.pop_up_anim));
    }

    public static void setVisibleScaleAnim(View view, int visible) {
        view.setVisibility(visible);
        scaleAnim(view, visible);
    }

}
