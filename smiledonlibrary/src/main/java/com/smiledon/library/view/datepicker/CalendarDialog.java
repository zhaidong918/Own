package com.smiledon.library.view.datepicker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.smiledon.library.R;

import java.util.List;

public class CalendarDialog extends Dialog{

	private CalendarInterface listener;
	private DatePicker mDatePicker;
	private Activity context;
	public CalendarDialog(Activity context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public CalendarDialog(Activity context) {
		super(context, R.style.loading_dialog);
		this.context = context;
	}

	public interface CalendarInterface {
		public void onDateComplete(String date);
	}

	public void setListener(CalendarInterface listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_calendar);

		LinearLayout view = (LinearLayout) findViewById(R.id.calendarLayout);
		DisplayMetrics outMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		view.getLayoutParams().width = outMetrics.widthPixels;

		mDatePicker = (DatePicker) findViewById(R.id.datePicker);
		mDatePicker.setColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
		mDatePicker.isLunarDisplay(true);
		mDatePicker.setOnDateSelected(new OnDateSelected() {
			@Override
			public void selected(List<String> date) {
				CalendarDialog.this.dismiss();
				if(listener != null){
					String result = null;
					if(date != null && date.size() != 0){
						result = date.get(date.size()-1);
					}
					listener.onDateComplete(result);
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		mDatePicker.showToday();
	}
}
