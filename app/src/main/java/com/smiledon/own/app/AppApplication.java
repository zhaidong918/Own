package com.smiledon.own.app;

import android.content.Context;
import android.os.Handler;

import com.smiledon.own.utils.LogUtil;

import org.litepal.LitePalApplication;


/**
 * @author East Chak
 * @date 2018/1/3 9:48
 */

    public class AppApplication extends LitePalApplication {

    protected static Context sContext;
    protected static Handler sHandler;
    protected static int sMainThreadId;
    private static AppApplication sApp;

    public static synchronized AppApplication getInstance() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        sContext = getApplicationContext();
        sHandler = new Handler();
        sMainThreadId = android.os.Process.myTid();

        LogUtil.init();

    }

    /**
     * 获取上下文对象
     *
     * @return context
     */
    public static Context getContext() {
        return sContext;
    }

    /**
     * 获取全局handler
     *
     * @return 全局handler
     */
    public static Handler getHandler() {
        return sHandler;
    }

    /**
     * 获取主线程id
     *
     * @return 主线程id
     */
    public static int getMainThreadId() {
        return sMainThreadId;
    }
}
