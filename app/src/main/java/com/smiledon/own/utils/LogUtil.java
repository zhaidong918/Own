package com.smiledon.own.utils;

import android.util.Log;

import java.util.Locale;

/**
 * 定制的Log系统，若要支持日志上传等，应该在此类里统一处理
 * 统一使用tag: pos
 *
 * @author qinyu
 * @date 2017-9-30 15:56
 */

public class LogUtil {

    private static final String TAG = "OWN_LOG";

    /**
     * 具体上传功能还未实现
     */
    // TODO
    private static boolean enableUpload = false;

    /**
     * DEBUG:3
     * INFO:4
     * WARN:5
     * ERROR:6
     * eg: adb shell setprop log.tag.pos VERBOSE
     */
    private static int LOG_LEVEL;

    public static void init() {
        boolean isDebug = false;
        int logLevel = Log.ERROR;
        for (int level = Log.DEBUG; level <= Log.ERROR; level++) {
            isDebug = Log.isLoggable(TAG, level);
            if (isDebug) {
                logLevel = level;
                break;
            }
        }
        LOG_LEVEL = logLevel;
    }

    /**
     *
     */
    public static void updateShowLevel (int logLevel) {
        LOG_LEVEL = logLevel;
    }

    /**
     * 开启/关闭日志上传功能
     */
    public static void enableUpload(boolean isEnable) {
        enableUpload = isEnable;
    }

    /**
     * 用于开发过程中日志监控，上线后此级别不可开启
     * @param format
     * @param args
     */
    public static void d(String format, Object... args) {
        if (LOG_LEVEL <= Log.DEBUG) {
            Log.d(TAG, buildMessage(format, args));
        }
    }

    /**
     * 用于记录操作信息，程序执行过程等
     * @param format
     * @param args
     */
    public static void i(String format, Object... args) {
        if (LOG_LEVEL <= Log.INFO) {
            Log.i(TAG, buildMessage(format, args));
        }
    }

    /**
     * 用于记录警告行的行为
     * @param format
     * @param args
     */
    public static void w(String format, Object... args) {
        if (LOG_LEVEL <= Log.WARN) {
            Log.w(TAG, buildMessage(format, args));
        }
    }

    /**
     * 用于记录错误信息
     * @param format
     * @param args
     */
    public static void e(String format, Object... args) {
        if (LOG_LEVEL <= Log.ERROR) {
            Log.e(TAG, buildMessage(format, args));
        }
    }

    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || args.length == 0) ? format : String.format(Locale.CHINA,
                format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        StringBuilder info = new StringBuilder();
        if (trace.length >= 3) {
            String callingClass = trace[2].getClassName();
            info.append(callingClass.substring(callingClass.lastIndexOf('.') + 1));
            info.append('.');
            info.append(trace[2].getMethodName());
            info.append(':');
            info.append(trace[2].getLineNumber());
        }
        info.append(": ");
        info.append(msg);

        return info.toString();
    }

}
