package com.smiledon.own.widgets.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smiledon.own.R;
import com.smiledon.own.utils.DisplayUtils;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/5 14:12
 */

public class CommonEditLayout extends LinearLayout {

    public static class Type{

        public static final int ICON = 1;
        public static final int DESCRIPTION = 2;
        public static final int PASSWORD = 3;

    }

    public CommonEditLayout(Context context) {
        this(context, null);
    }

    public CommonEditLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    Context mContext;

    private Paint linePaint;

    private ImageView iconView;
    private TextView descriptionView;
    private EditText inputView;
    private ImageView iconOperateView;
    private CheckBox passwordView;

    private void init() {

        setVerticalGravity(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        iconView = new ImageView(mContext);
        iconView.setImageResource(R.drawable.icon_user);
        LinearLayout.LayoutParams iconParams = new LayoutParams(DisplayUtils.dp2px(25), DisplayUtils.dp2px(25));
        iconView.setLayoutParams(iconParams);
        addView(iconView);
        iconView.setPadding(DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5));

        descriptionView = new TextView(mContext);
        descriptionView.setText("Name");
        descriptionView.setTextColor(Color.parseColor("#8a8a8a"));
        descriptionView.setTextSize(14);
        LinearLayout.LayoutParams descriptionParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        descriptionView.setLayoutParams(descriptionParams);
        addView(descriptionView);

        inputView = new EditText(mContext);
        inputView.setHint("plz input name");
        inputView.setTextColor(Color.parseColor("#666666"));
        inputView.setTextSize(14);
        LinearLayout.LayoutParams inputParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        inputParams.weight = 1.0f;
        inputParams.setMarginStart(DisplayUtils.dp2px(5));
        inputView.setLayoutParams(inputParams);
        inputView.setBackgroundResource(R.color.transparent);
        inputView.setSingleLine(true);
        addView(inputView);

        iconOperateView = new ImageView(mContext);
        iconOperateView.setImageResource(R.drawable.img_delete);
        LinearLayout.LayoutParams iconOperateParams = new LayoutParams(DisplayUtils.dp2px(25), DisplayUtils.dp2px(25));
        iconOperateView.setLayoutParams(iconOperateParams);
        addView(iconOperateView);
        iconOperateView.setPadding(DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5));

        passwordView = new CheckBox(mContext);
        setSelectorState(passwordView);
        LinearLayout.LayoutParams pwdIconParams = new LayoutParams(DisplayUtils.dp2px(25), DisplayUtils.dp2px(25));
        pwdIconParams.setMarginStart(DisplayUtils.dp2px(5));
        passwordView.setLayoutParams(pwdIconParams);
        passwordView.setPadding(DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5), DisplayUtils.dp2px(5));
        addView(passwordView);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(DisplayUtils.dp2px(1));
        linePaint.setTextSize(DisplayUtils.sp2px(14));
    }



    @Override
    public void draw(Canvas canvas) {

        super.draw(canvas);
        canvas.drawText("111", startLineX, getMeasuredHeight()/2, linePaint);
    }

    int startLineX = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        startLineX = 0;

        if (iconView != null) {
            startLineX += iconView.getMeasuredWidth();
        }

        if (descriptionView != null) {
            startLineX += descriptionView.getMeasuredWidth();
        }
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 设置selector的状态
     * @param view
     */
    private void setSelectorState(CheckBox view){

        StateListDrawable listDrawable = new StateListDrawable();

        Drawable visible = mContext.getDrawable(R.drawable.img_visible);
        Drawable invisible = mContext.getDrawable(R.drawable.img_invisible);

        listDrawable.addState(new int[]{android.R.attr.state_checked}, visible);
        listDrawable.addState(new int[]{}, invisible);

        view.setClickable(true);
        view.setButtonDrawable(listDrawable);
        view.setChecked(false);
    }
}
