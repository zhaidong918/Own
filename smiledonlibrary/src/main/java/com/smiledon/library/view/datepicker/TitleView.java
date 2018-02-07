package com.smiledon.library.view.datepicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smiledon.library.R;

/**
 * 标题视图
 * @author XZ
 */
public class TitleView extends LinearLayout implements MonthView.OnPageChangeListener, MonthView.OnSizeChangedListener {
    private String[] monthTitles;
    private TextView tvYear, tvMonth, tvConfirm;
    private OnDateSelected mOnDateSelected;
    private MonthView monthView;

    interface TitleViewClick{
        void onClick();
    }

    public TitleView(Context context) {
        super(context);
        setColor(0xFFE95344);
        setOrientation(HORIZONTAL);

        monthTitles = Language.getLanguage(context).monthTitles();

        LayoutParams llParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        LayoutParams mParams = new LayoutParams(-2, LayoutParams.WRAP_CONTENT);

        tvYear = new TextView(context);
        tvYear.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tvYear.setTextColor(Color.WHITE);
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick();
                }
            }
        });

        tvMonth = new TextView(context);
        tvMonth.setGravity(Gravity.CENTER);
        tvMonth.setTextColor(Color.WHITE);
        tvMonth.setBackgroundResource(R.drawable.lighter_transparent_selector);
        Drawable arrowDrown = getResources().getDrawable(R.drawable.img_arrow_down_white);
        tvMonth.setCompoundDrawablesWithIntrinsicBounds(null, null, arrowDrown, null);
        tvMonth.setCompoundDrawablePadding(5);
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick();
                }
            }
        });

        tvConfirm.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        tvConfirm.setText(Language.getLanguage(context).ensureTitle());
        tvConfirm.setTextColor(getResources().getColor(android.R.color.white));
        tvConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnDateSelected && null != monthView)
                    mOnDateSelected.selected(monthView.getDateSelected());
            }
        });

        addView(tvYear, llParams);
        addView(tvMonth, mParams);
        addView(tvConfirm, llParams);
    }

    private TitleViewClick listener;
    public void setTitleViewClick(TitleViewClick listener){
        this.listener = listener;
    }

    public void setOnDateSelected(OnDateSelected onDateSelected, MonthView monthView) {
        mOnDateSelected = onDateSelected;
        this.monthView = monthView;
    }

    public void setColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public void onMonthChange(int month) {
        tvMonth.setText(monthTitles[month - 1]);
    }

    @Override
    public void onYearChange(int year) {
        tvYear.setText(String.valueOf(year));
    }

    @Override
    public void onSizeChanged(int size) {
        int padding = (int) (size * 1F / 50F);
        int textSizeSmall = (int) (size * 1F / 25F);
        int textSizeLarge = (int) (size * 1F / 18F);

        tvYear.setPadding(padding, padding, 0, padding);
        tvYear.getPaint().setTextSize(textSizeSmall);

        tvMonth.setPadding(10, padding, 10, padding);
        tvMonth.getPaint().setTextSize(textSizeLarge);

        tvConfirm.setPadding(0, padding, padding, padding);
        tvConfirm.getPaint().setTextSize(textSizeSmall);
    }
}
