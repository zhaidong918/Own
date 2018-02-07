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
        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
        alphaAnimation.setDuration(200);
        view.setAnimation(alphaAnimation);
    }

    public static void translateAnim() {



    }

}
