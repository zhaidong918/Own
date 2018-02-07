package com.smiledon.library.view.banner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 轮播图无限左右滑动
 * @author zhaidong
 *
 */
public class CarouselViewPager extends LinearLayout implements OnPageChangeListener{

	private Context mContext;
	private int dotsCount = 0;					//滚动的点的个数
	private int imageCount = 0;					//滚动视图个数
	private List<ImageView> imageViews;
	private CarouselAdapter carouselAdapter;

	private RadioGroup radioGroup;
	private ViewPager viewPager;
	private AysncTimer aysncTimer;

	private int sleepTime = 5000;
	private int currentIndex = 0;
	private Handler handler;

	private int handlerMessageId = 0;

	private boolean isStop = false;
	private boolean isStopCarousel = false;	//滑动的时候不执行自动轮播

	private int screenWdith;	//屏幕宽度

	/**
	 *  第一次setCurrentItem会调用 onPageSelected
	 *  然后再调用 onPageScrolled
	 *  导致isStopCarousel = true 无法自动滚动
	 */
	private boolean isFirstScroll = true;

	private int radioBtnRadius = 5;

	public CarouselViewPager(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public CarouselViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	public void onPause(){
		handler.removeMessages(handlerMessageId);
	}

	public void onResume(){
		currentIndex = 0;
		handler.sendEmptyMessage(handlerMessageId);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		//得到上级容器为其推荐的宽高和计算模式
		int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);

		// MeasureSpec.EXACTLY 表示设置了精确的值
		// 如果 mode 是 MeasureSpec.EXACTLY 时候，则不是 warp_content 用计算来的值，否则则用上级布局分给它的值
		setMeasuredDimension(
				specWidthMode == MeasureSpec.EXACTLY ? specWidthSize : screenWdith,
				specHeightMode == MeasureSpec.EXACTLY ? specHeightSize : (int)(screenWdith * (9f/16f)));

	}

	/**
	 * 初始化布局 代码添加布局
	 */
	private void initView() {

		WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);
		screenWdith = (int) (metrics.widthPixels * metrics.density);

		radioBtnRadius = Dp2Px(mContext, radioBtnRadius);

		viewPager = new ViewPager(mContext);
		viewPager.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(viewPager);

		LinearLayout layout = new LinearLayout(mContext);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, Dp2Px(mContext, 30));
		layoutParams.topMargin = -Dp2Px(mContext, 30);
		layout.setLayoutParams(layoutParams);
		layout.setGravity(Gravity.CENTER);

		radioGroup = new RadioGroup(mContext);
		radioGroup.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		radioGroup.setOrientation(LinearLayout.HORIZONTAL);
		//		radioGroup.setAlpha(0.7f);
		layout.addView(radioGroup);

		addView(layout);

		aysncTimer = new AysncTimer();

		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				currentIndex ++;
				viewPager.setCurrentItem(currentIndex, true);
				//				Toast.makeText(mContext, "滚动了~", Toast.LENGTH_LONG).show();
			}
		};
	}

	/**
	 * 初始化viewpager 布局 并开启
	 * @param imageUrls
	 */
	public void startAysncTimer(List<Object> imageUrls) {

		if(imageUrls == null
				&& imageUrls.size() == 0){
			return;
		}

		initCarouselDots(imageUrls);
		initImageView(imageUrls);
		initCarouselViewPager(imageUrls);
	}



	/**
	 * 初始化滚动图
	 */
	private void initImageView(List<Object> imageUrls) {

		if(imageUrls.size() == 2){
			imageUrls.addAll(imageUrls);
		}

		imageCount = imageUrls.size();

		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER);

		imageViews = new ArrayList<ImageView>();
		for (int i = 0; i < imageCount; i++) {

			ImageView image = new ImageView(mContext);
			image.setScaleType(ScaleType.FIT_XY);
			image.setLayoutParams(layoutParams);
			Glide.with(mContext).load((Integer)imageUrls.get(i)).into(image);
			imageViews.add(image);
		}
	}

	/**
	 * 初始化滚动的指示点
	 * @param imageUrls
	 */
	private void initCarouselDots(List<Object> imageUrls) {

		dotsCount = imageUrls.size();

		if(dotsCount == 1) return;

		RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(radioBtnRadius, radioBtnRadius);
		params.setMargins(10, 0, 0, 0);

		for (int i = 0; i < dotsCount; i++) {

			RadioButton button = new RadioButton(mContext);
			setSelectorState(button);
			button.setButtonDrawable(android.R.color.transparent);
			button.setEnabled(false);
			radioGroup.addView(button, i, params);
		}
		getRadioButton(0).setChecked(true);

	}

	/**
	 * 初始化viewpager相关事件
	 */
	private void initCarouselViewPager(List<Object> imageUrls) {

		carouselAdapter = new CarouselAdapter();
		viewPager.setAdapter(carouselAdapter);

		if(imageUrls.size() == 1){
			viewPager.setCurrentItem(currentIndex);
		}
		else{
			currentIndex = 100 - 100 % dotsCount;
			//			currentIndex = Integer.MAX_VALUE/2 - Integer.MAX_VALUE/2 % dotsCount; 设置最大值的中间会出现第一页和最后一页滑动 出现跳动
			viewPager.setCurrentItem(currentIndex);
			viewPager.setOnPageChangeListener(this);
			aysncTimer.start();
		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		isStopCarousel = false;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if(isFirstScroll)
			isFirstScroll = false;
		else
			isStopCarousel = true;
	}

	@Override
	public void onPageSelected(int position) {
		isStopCarousel = false;
		currentIndex = position;
		getRadioButton(position%dotsCount).setChecked(true);
	}

	private RadioButton getRadioButton(int position){
		return (RadioButton)(radioGroup.getChildAt(position));
	}

	/**
	 * 销毁 自动滚动
	 */
	public void destoryAysncTimer(){
		isStop = true;
		if(aysncTimer != null){
			aysncTimer.interrupt();
			aysncTimer = null;
		}
	}

	class CarouselAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return imageCount == 1 ? imageCount : Integer.MAX_VALUE;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object object) {
			//			view.removeView(imageViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			position = position % imageCount;

			ViewPager group = (ViewPager) imageViews.get(position).getParent();

			if (group != null) {
				group.removeView(imageViews.get(position));
			}

			container.addView(imageViews.get(position));

			return imageViews.get(position);
		}
	}

	class AysncTimer extends Thread{
		@Override
		public void run() {
			try {
				while (!isStop) {
					Thread.sleep(sleepTime);
					if(!isStopCarousel)
						handler.sendEmptyMessage(handlerMessageId);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置selector的状态
	 * @param view
	 */
	private void setSelectorState(View view){
		StateListDrawable listDrawable = new StateListDrawable();

		GradientDrawable ohterDrawable = getGradientDrawable(view, android.R.color.holo_blue_dark);
		GradientDrawable defaultDrawable = getGradientDrawable(view, android.R.color.darker_gray);

		listDrawable.addState(new int[]{android.R.attr.state_checked}, ohterDrawable);
		listDrawable.addState(new int[]{}, defaultDrawable);

		view.setClickable(true);
		view.setBackground(listDrawable);
	}

	/**
	 * 获取一个 GradientDrawable 对象设置到背景中
	 * @param view
	 * @return
	 */
	private GradientDrawable getGradientDrawable(View view, int color){

		GradientDrawable drawable = new GradientDrawable();
		drawable.setShape(GradientDrawable.OVAL);

		drawable.setCornerRadius(radioBtnRadius);

		drawable.setColor(view.getResources().getColor(color));

		return drawable;
	}


	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

}
