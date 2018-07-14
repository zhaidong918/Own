package com.smiledon.own.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/29 16:21
 */

public class LeakSolutionUtil {

    /**
     * 15<= API <=23
     * 修复安卓输入法内存泄漏问题
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {

 /*       if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1|| Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return;
        }*/

        if (destContext == null) return;

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm == null) return;
        //mLastSrcView 华为机型才会导致内存泄漏
        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrcView"};

        Field field;
        Object object;

        for (String param : arr) {
            try {

                field = imm.getClass().getDeclaredField(param);

                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }

                object = field.get(imm);
                if (object != null && object instanceof View) {
                    if (((View) object).getContext() == destContext) {
                        field.set(imm, null);
                    }
                    else{

                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

