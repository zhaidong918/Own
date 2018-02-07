package com.smiledon.library.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.smiledon.library.R;
import com.smiledon.library.utils.DPUtil;


/**
 * 获取手机验证码
 * @author donz
 *
 */
public class AuthCodeView extends TextView{

	public int seconds = 60;
	public int view_width = 100;
	public int view_height = 30;

	public String enabled_start_text = "获取验证码";
	public String enabled_true_text = "重新获取";


	public int enabled_false_text_color = R.color.white;
	public int enabled_false_text_bg = R.drawable.btn_border_gay_bg;

	public int enabled_true_text_color = R.color.white;
	public int enabled_true_text_bg = R.drawable.btn_border_orange_bg;

	private TextView view;
	public CountDownTimer timer;

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {

			if(seconds == 0){
				view.setText(enabled_true_text);
				view.setTextColor(getResources().getColor(enabled_true_text_color));
				view.setBackgroundResource(enabled_true_text_bg);
				view.setEnabled(true);
				closeTimer();
			}else{
				view.setText(seconds + "秒后重试");
				view.setTextColor(getResources().getColor(enabled_false_text_color));
				view.setBackgroundResource(enabled_false_text_bg);
			}
		};
	};

	public AuthCodeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressWarnings("deprecation")
	public AuthCodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		view = this;
		initView();
	}

	public AuthCodeView(Context context) {
		this(context, null);
	}

	public void startOrNotifyTimer(){
		view.setEnabled(false);
		if(timer == null){
			seconds = 60;
			timer = new CountDownTimer();
			timer.start();
		}else{
			seconds = 60;
		}
	}

	public void closeTimer(){
		if(timer != null){
			seconds = 60;
			timer.interrupt();
			timer = null;
		}
	}



	private void initView() {
		view.setBackgroundResource(enabled_true_text_bg);
		view.setText(enabled_start_text);
		view.setTextColor(getResources().getColor(enabled_true_text_color));
		view.setTextSize(12);
		view.setGravity(Gravity.CENTER);
		view.setWidth(DPUtil.Dp2Px(getContext(), view_width));
		view.setHeight(DPUtil.Dp2Px(getContext(), view_height));
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);  
//		lp.width = DPUtil.Dp2Px(getContext(), width);
//		view.setLayoutParams(lp);  

		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startOrNotifyTimer();
			}
		});
	}

	public class CountDownTimer extends Thread{

		@Override
		public void run() {
			synchronized (this) {
				try {
					while (seconds != 0 ) {
						seconds -- ;
						handler.sendEmptyMessage(0);
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();

				}
			}
		}
	}

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//	}

}
