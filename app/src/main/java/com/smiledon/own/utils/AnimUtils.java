package com.smiledon.own.utils;

import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;

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

    public static void translateAnim() {



    }

}
