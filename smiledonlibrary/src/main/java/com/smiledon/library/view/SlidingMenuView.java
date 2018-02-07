package com.smiledon.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.smiledon.library.R;


/**
 * 仿QQ侧滑菜单显示
 * @author Administrator
 *
 */
public class SlidingMenuView extends HorizontalScrollView{

	private LinearLayout mWrapper;     //滑动布局的第一个子布局
	private ViewGroup mMenuView;
	private ViewGroup mContentView;

	private int mScreenWdith;
	private int mMenuWidth;


	private int mMenuRightPadding = 50; // 初始值
	private boolean once = true;
	private boolean menuIsOpen = false;

	/**
	 * 不使用自定义属性时调用此构造方法
	 * @param context
	 * @param attrs
	 */
	public SlidingMenuView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * 使用自定义属性时调用此构造方法
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		//TypedArray 使用过后一定要调用ta.recycle()回收    获取自定义属性（设置菜单的右边距）
		TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingMenuView, defStyle, 0);
		int count = ta.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = ta.getIndex(i);

			if(attr == R.styleable.SlidingMenuView_rightPadding) {
				mMenuRightPadding = ta.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
								mMenuRightPadding,
								context.getResources().getDisplayMetrics()));
		}
	}

		ta.recycle();

	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
	mScreenWdith = outMetrics.widthPixels;

	//  wm.getDefaultDisplay().getMetrics(outMetrics) 与 context.getResources().getDisplayMetrics()的区别
	//  把dp转化成px
	//		mMenuRightPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, context.getResources().getDisplayMetrics());
	mMenuWidth = mScreenWdith - mMenuRightPadding;

}


	/**
	 * 通过new的时候调用此方法
	 * @param context
	 */
	public SlidingMenuView(Context context) {
		this(context, null);
	}

	/**
	 * 设置子布局和自己的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if(once){

			mWrapper = (LinearLayout) getChildAt(0);
			mMenuView = (ViewGroup) mWrapper.getChildAt(0);
			mContentView = (ViewGroup) mWrapper.getChildAt(1);
			//设置 菜单和内容视图的宽
			mMenuView.getLayoutParams().width = mMenuWidth;
			mContentView.getLayoutParams().width = mScreenWdith;
			once = false;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			//第一次进入隐藏菜单视图
			this.scrollTo(mMenuWidth, 0);
			menuIsOpen = true;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();

		switch (action) {

			case MotionEvent.ACTION_UP:
				int scrollX = this.getScrollX();
				if(scrollX >= mMenuWidth/2){
					this.smoothScrollTo(mMenuWidth, 0);
					menuIsOpen = false;
				}
				else{
					this.smoothScrollTo(0, 0);
					menuIsOpen = true;
				}
				return true;
		}

		return super.onTouchEvent(event);
	}


	private void openMenu(){
		if(menuIsOpen) return;
		this.smoothScrollTo(0, 0);
		menuIsOpen = true;
	}

	private void closeMenu(){
		if(!menuIsOpen) return;
		this.smoothScrollTo(mMenuWidth, 0);
		menuIsOpen = false;
	}

	/**
	 * 菜单开关（外部调用此方法打开或关闭菜单视图）
	 */
	public void toggleMenu(){
		if(menuIsOpen)
			closeMenu();
		else
			openMenu();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		System.out.println("getScrollX---->" + l);

		float scale = l*1.0f/mMenuWidth;      // 通过滑动距离获得一个比例系数
		float contenttScale = 0.8f + 0.2f*scale;  //0.8~1.0

		float menuScale = 1.0f - 0.2f*scale;	//1.0~0.8
		float menuAlpha = 1.0f - 0.2f*scale;	//1.0~0.8
		//nineoldandroids-2.4.0.jar 兼容3.0以下
		//设置菜单显示位置和缩放比例及透明度
		ViewHelper.setTranslationX(mMenuView, l*0.7f);
		ViewHelper.setScaleX(mMenuView, menuScale);
		ViewHelper.setScaleY(mMenuView, menuScale);
		ViewHelper.setAlpha(mMenuView, menuAlpha);
		//设置内容缩放比例和缩放点
		ViewHelper.setPivotX(mContentView, 0);
		ViewHelper.setPivotY(mContentView, mContentView.getHeight()/2);
		ViewHelper.setScaleX(mContentView, contenttScale);
		ViewHelper.setScaleY(mContentView, contenttScale);

	}

}
