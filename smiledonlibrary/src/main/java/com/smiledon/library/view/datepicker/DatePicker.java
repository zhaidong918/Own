package com.smiledon.library.view.datepicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.Calendar;

/**
 * @author XZ
 */
public class DatePicker extends LinearLayout implements IPick,TitleView.TitleViewClick {
    private MonthView monthView;
    private TitleView titleView;
    private Context context;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundColor(Color.WHITE);
        setOrientation(VERTICAL);

        LayoutParams llParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        titleView = new TitleView(context);
        titleView.setTitleViewClick(this);
        addView(titleView, llParams);

        monthView = new MonthView(context);
        monthView.setOnPageChangeListener(titleView);
        monthView.setOnSizeChangedListener(titleView);
        addView(monthView, llParams);
    }

    private void showDateDialog(Context context){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthView.update(year, monthOfYear, dayOfMonth);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void setOnDateSelected(OnDateSelected onDateSelected) {
        titleView.setOnDateSelected(onDateSelected, monthView);
    }

    @Override
    public void setColor(int color) {
        titleView.setColor(color);
        monthView.setColorMain(color);
    }

    @Override
    public void isLunarDisplay(boolean display) {
        monthView.setLunarShow(display);
    }

    @Override
    public void setEventType(MonthView.EventType type) {
        monthView.setEventType(type);
    }

    public void showToday(){
        monthView.showToday();
    }

    @Override
    public void onClick() {
        showDateDialog(context);
    }
}
