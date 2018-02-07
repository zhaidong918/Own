package com.smiledon.library.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

	private static final int DEFAULT_DPI = 160;

	private static Context context;
	private static Activity activity;
	private static WindowManager manager;

	private static ScreenUtil screenUtil;

	public ScreenUtil(Activity activity) {
		this.activity = activity;
		this.manager = activity.getWindowManager();
	}

	public static ScreenUtil getInstance(Activity activity) {
		if(screenUtil  == null)
			screenUtil = new ScreenUtil(activity);
		return screenUtil;
	}

	/**
	 *
	 * @return 屏幕的分辨率（宽）
	 */
	public static float getWidthDensity(){
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	/**
	 *
	 * @return 屏幕的分辨率（高）
	 */
	public static float getHeightDensity(){
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	/**
	 *
	 * @return 屏幕的高度
	 */
	public static float getHeightScreen(){
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels * metrics.density;
	}

	/**
	 *
	 * @return 屏幕的宽度
	 */
	public static float getWidthScreen(){
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels * metrics.density;
	}

	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
