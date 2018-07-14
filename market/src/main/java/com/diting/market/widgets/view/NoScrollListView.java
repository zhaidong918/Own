package com.diting.market.widgets.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不可滑动的listView
 *
 * @author zhaidong
 * @date   2018/2/8 11:40
 */
public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {
		super(context);
	}
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public NoScrollListView(Context context, AttributeSet attrs,
                            int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	/**
	 * 重写该方法，Listview在ScrollView中完全显示不滑动
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
