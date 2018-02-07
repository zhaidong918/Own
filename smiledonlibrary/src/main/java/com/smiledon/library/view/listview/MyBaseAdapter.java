package com.smiledon.library.view.listview;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class MyBaseAdapter extends BaseAdapter {

	protected LayoutInflater inflater = null;
	protected Context context = null;

	public OnItemBtnClickListener itemBtnClickListener = null;

	public void setItemBtnClickListener(OnItemBtnClickListener itemBtnClickListener) {
		this.itemBtnClickListener = itemBtnClickListener;
	}

	public interface OnItemBtnClickListener {
		public void itemBtnClick(Object obj);
	}
			
			
	public OnItemClickListener clickListener = null;

	public void setClickListener(OnItemClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface OnItemClickListener {
		public void itemClick(View view, int positon, int id);
	}

	public MyBaseAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	private List data = null;

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public void addData(List data2) {
		if (data2 == null || data2.size() == 0)
			return;
		for (Object object : data2) {
			data.add(object);
		}
	}
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}
	
}
