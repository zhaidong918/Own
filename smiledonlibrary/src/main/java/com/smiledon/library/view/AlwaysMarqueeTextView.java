package com.smiledon.library.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 无限滚动的文本
 * @author Administrator
 *
 */
public class AlwaysMarqueeTextView extends TextView{

	public AlwaysMarqueeTextView(Context context) {
		super(context);
	}
	public AlwaysMarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public AlwaysMarqueeTextView(Context context, AttributeSet attrs,
								 int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 由于textview滚动需要获取焦点，故重写isFocused只返回true
	 */
	@Override
	public boolean isFocused() {
		return true;
	}
}
