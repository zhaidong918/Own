package com.smiledon.library.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * ģ��lsitview
 * @author Administrator
 *
 */
public class LinearLayout4ListView extends LinearLayout {

	private BaseAdapter adapter;
	private OnClickListener onClickListener = null;

	public void bindLinearLayout() {
		removeAllViews();
		int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			View v = adapter.getView(i, null, null);
			v.setOnClickListener(this.onClickListener);
			MarginLayoutParams mlp = new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			if(i != 0){
				mlp.topMargin = 10;
			}
			LayoutParams lp = new LayoutParams(mlp);
			v.setLayoutParams(lp);
			addView(v, i);
		}
	}

	public LinearLayout4ListView(Context context) {
		super(context);
	}

	public LinearLayout4ListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public BaseAdapter getAdpater() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adpater) {
		this.adapter = adpater;
		bindLinearLayout();
	}

	public OnClickListener getOnclickListner() {
		return onClickListener;
	}

	public void setOnclickLinstener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void notifyDataSetChanged(){
		bindLinearLayout();
	}
}