package com.smiledon.library.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.TextView;


public class DrawableUtil {

	/**
	 * 对于不同的shape和selector会有不同的颜色状态和半径等
	 * 如果每次都去写一个xml好像又会写出很多
	 * 所以写此工具类是为了方便快捷的动态设置
	 *
	 */

	// true {STATE_PRESSED}  , false {-STATE_PRESSED}

	public static final int STATE_PRESSED = android.R.attr.state_pressed;
	public static final int STATE_FOCUSED = android.R.attr.state_focused;
	public static final int STATE_ENABLED = android.R.attr.state_enabled;
	public static final int STATE_CHECKED = android.R.attr.state_checked;
	public static final int STATE_SELECTED = android.R.attr.state_selected;


	public DrawableUtil(){}

	/**
	 *  设置一个矩形的背景
	 * @param view        	目标view
	 */
	public static void setShape(View view, ShapeInfo info){
		view.setBackground(getGradientDrawable(view, info));
	}

	/**
	 * 获取一个 GradientDrawable 对象设置到背景中
	 * @param view
	 * @return
	 */
	private static GradientDrawable getGradientDrawable(View view, ShapeInfo info){

		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.RECTANGLE);

		if(info.radius > 0)
			drawable.setCornerRadius(info.radius);


		if(info.strokeColor > 0
				&& info.strokeWidth > 0)

			drawable.setStroke(info.strokeWidth, view.getResources().getColor(info.strokeColor));

		if(info.solidColor > 0)
			drawable.setColor(view.getResources().getColor(info.solidColor));

		return drawable;
	}


	/**
	 * 设置按下的selector
	 * @param view
	 * @param other
	 * @param info
	 */
	public static void setPressSelector(View view, ShapeInfo info, ShapeInfo other){
		int[] states = new int[]{STATE_PRESSED};
		setSelectorState(view, states, info, other);
	}

	/**
	 * 设置选中的selector
	 * @param view
	 */
	public static void setSelectSelector(View view, ShapeInfo info, ShapeInfo other){
		int[] states = new int[]{STATE_SELECTED};
		setSelectorState(view, states, info, other);
	}

	/**
	 * 设置照片的selector
	 * @param view
	 */
	public static StateListDrawable getDrawableSelector(View view, int ohterImage, int defaultImage){

		StateListDrawable listDrawable = new StateListDrawable();

		Drawable ohterDrawable = view.getResources().getDrawable(ohterImage);
		Drawable defaultDrawable = view.getResources().getDrawable(defaultImage);

		listDrawable.addState(new int[]{STATE_PRESSED}, ohterDrawable);
		listDrawable.addState(new int[]{STATE_SELECTED}, ohterDrawable);
		listDrawable.addState(new int[]{STATE_CHECKED}, ohterDrawable);
		listDrawable.addState(new int[]{}, defaultDrawable);
		view.setClickable(true);
		return listDrawable;
	}


	/**
	 * 设置是否可点击的selector
	 * @param view
	 */
	public static void setEnableSelector(View view, ShapeInfo info, ShapeInfo other){
		int[] states = new int[]{STATE_ENABLED};
		setSelectorState(view, states, info, other);
	}

	/**
	 * 设置selector的状态
	 * @param view
	 * @param states
	 */
	private static void setSelectorState(View view, int[] states, ShapeInfo info, ShapeInfo other){
		StateListDrawable listDrawable = new StateListDrawable();

		GradientDrawable ohterDrawable = getGradientDrawable(view, other);
		GradientDrawable defaultDrawable = getGradientDrawable(view, info);

		listDrawable.addState(states, ohterDrawable);
		listDrawable.addState(new int[]{}, defaultDrawable);

		view.setClickable(true);
		view.setBackground(listDrawable);
	}

	/**
	 * 设置文本颜色 (选中或者按下)
	 * @param view			目标view
	 * @param otherColor	seletor状态对应的颜色
	 * @param defaultColor	正常的颜色
	 */
	public static void setTextColorSelector(TextView view , int defaultColor, int otherColor){

		int[][] states = new int[][]{{STATE_PRESSED}, {STATE_CHECKED},new int[]{}};

		otherColor = view.getResources().getColor(otherColor);
		defaultColor = view.getResources().getColor(defaultColor);

		int[] colors = new int[]{otherColor, otherColor, defaultColor};

		ColorStateList colorStateList = new ColorStateList(states, colors);

		view.setClickable(true);
		view.setTextColor(colorStateList);
	}

	/**
	 *  设置文本颜色  (不可点击)
	 * @param view			目标view
	 * @param otherColor	seletor状态对应的颜色
	 * @param defaultColor	正常的颜色
	 */
	public static void setTextColorEnaledSelector(TextView view, int defaultColor, int otherColor){

		int[][] states = new int[][]{{STATE_ENABLED}, new int[]{}};

		otherColor = view.getResources().getColor(otherColor);
		defaultColor = view.getResources().getColor(defaultColor);

		int[] colors = new int[]{otherColor, defaultColor};

		ColorStateList colorStateList = new ColorStateList(states, colors);

		view.setTextColor(colorStateList);
	}


	/**
	 * 获取外部内实列 drawableUtil
	 *
	 * drawableUtil.new ShapeInfo
	 */
	public class ShapeInfo{

		public int radius;	//圆角半径
		public int solidColor;	//填充颜色
		public int strokeWidth;	//边框宽度
		public int strokeColor;	//边框颜色
	}

}
