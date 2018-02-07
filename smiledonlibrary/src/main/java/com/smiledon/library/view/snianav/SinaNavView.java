package com.smiledon.library.view.snianav;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaidong on 2017/9/11.
 */

public class SinaNavView extends HorizontalScrollView{

    private int screenWidth = getScreenWidth();

    private float defaultMargin = 50;
    private float defaultTvSize = 18;

    private float navWidth;

    private List<NavTitleBean> navTitles;
    private List<TextView> navTextViews;

    private DynamicLine dynamicLine;

    private INavOnClickListener iNavOnClickListener;

    public SinaNavView(Context context) {
        this(context, null);
    }

    public SinaNavView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SinaNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTitles(List<NavTitleBean> navTitles){
        if(navTitles != null){
            this.navTitles = navTitles;
            init();
        }
    }

    private void init() {
        initView();
    }

    private LinearLayout rootLl;
    private LinearLayout navLl;
    private ViewGroup.LayoutParams rootParams;
    private LinearLayout.LayoutParams navParams;
    private LinearLayout.LayoutParams tvParams;
    private LinearLayout.LayoutParams lineParams;
    private float[] tvWidths;

    private float[] tvStartXs;
    private float[] tvEndXs;

    private void initView() {

        dynamicLine = new DynamicLine(getContext());
        rootParams = getLayoutParams();
        rootParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        rootParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        navParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //添加根布局
        rootLl = new LinearLayout(getContext());
        rootLl.setBackgroundColor(Color.WHITE);
        rootLl.setOrientation(LinearLayout.VERTICAL);
        addView(rootLl);

        //添加title父布局
        navLl = new LinearLayout(getContext());
        navLl.setBackgroundColor(Color.WHITE);
        navLl.setOrientation(LinearLayout.HORIZONTAL);
        navLl.setPadding(0, 20, 0, 20);
        navLl.setLayoutParams(navParams);

        //添加title
        tvWidths = new float[navTitles.size()];
        tvStartXs = new float[navTitles.size()];
        tvEndXs = new float[navTitles.size()];
        navTextViews = new ArrayList<>();
        for (int i = 0; i < navTitles.size(); i++) {
            final NavTitleBean bean = navTitles.get(i);
            TextView tv = new TextView(getContext());
            tv.setTextColor(Color.GRAY);
            tv.setTextSize(defaultTvSize);
            tv.setText(bean.getTitle());

            //设置textview的点击事件
            final int finalI = i;
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(iNavOnClickListener != null)
                        iNavOnClickListener.onTitleOnClick(finalI, bean);
                }
            });

            //测量每个Textview的宽度
            TextPaint tvPaint = tv.getPaint();
            tvWidths[i] = tvPaint.measureText(bean.getTitle());


            //保存每个textview相对父布局的开始x坐标和结束的y坐标
            if(i == 0){
                tvStartXs[0] = defaultMargin;
            }
            else{
                tvStartXs[i] = tvEndXs[i-1] + 2*defaultMargin;
            }
            tvEndXs[i] = tvStartXs[i] + tvWidths[i];

            tvParams.setMargins((int) defaultMargin, 0, (int) defaultMargin, 0);
            tv.setLayoutParams(tvParams);
            navTextViews.add(tv);
            navLl.addView(tv);
        }
        rootLl.addView(navLl);

        //添加线的变动效果
        dynamicLine.setLayoutParams(lineParams);
        rootLl.addView(dynamicLine);

        //初始选中位置
        updateLineView(defaultMargin, defaultMargin + tvWidths[0]);
        updateTitleView(0);

    }

    public void setINavOnClickListener(INavOnClickListener iNavOnClickListener){
        this.iNavOnClickListener = iNavOnClickListener;
    }
    //更新线的位置
    public void updateLineView(float startX, float stopX){
        dynamicLine.updateLineView(startX, stopX);
    }

    int[] location=new int[2];
    int lastSelect = 0;
    boolean isClick = false;
    //更新textview的位置和选中
    public void updateTitleView(int position){

        isClick = Math.abs(lastSelect-position) > 1;

        //获取textview在屏幕中的起始点坐标
        navTextViews.get(position).getLocationOnScreen(location);
        if(location[0] + tvWidths[position] > screenWidth)
            smoothScrollBy(screenWidth/2, 0);
        else if(location[0] < 0)
            smoothScrollBy(-screenWidth/2, 0);

        if(lastSelect == position)
            return;

        navTextViews.get(lastSelect).setTextColor(Color.GRAY);
        tvSelectAnim(navTextViews.get(position));
        navTextViews.get(position).setTextColor(Color.BLACK);
        lastSelect = position;
    }

    private void tvSelectAnim(TextView view) {
        ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 1.3f, 1.0f);
        ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 1.2f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(800);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.playTogether(scaleAnimX, scaleAnimY);
        animatorSet.start();

    }

    /**
     * 结合viewpager更新线
     * @param position
     * @param lastPosition
     * @param positionOffset
     */
    public void updateLineView(int position, int lastPosition,float positionOffset) {

        if(isClick){
            dynamicLine.updateLineView(tvStartXs[lastPosition], tvEndXs[lastPosition]);
            return;
        }

/*        if(lastPosition > position) {
            if(position > 1) {
                if (positionOffset <= 0.5f)
                    dynamicLine.updateLineView(tvStartXs[position], tvEndXs[position] + (2 * defaultMargin + tvWidths[position]) * 2 * positionOffset);
                else {
                    dynamicLine.updateLineView(tvStartXs[position] + (2 * defaultMargin + tvWidths[position] * 2 * (positionOffset - 0.5f)), tvEndXs[position + 1]);
                }
            }
        }
        else{
            if(position + 1 < navTitles.size()) {
                if(positionOffset <= 0.5f)
                    dynamicLine.updateLineView(tvStartXs[position], tvEndXs[position] + (2 * defaultMargin + tvWidths[position + 1]) * 2 *positionOffset);
                else
                    dynamicLine.updateLineView(tvStartXs[position] + (2 * defaultMargin + tvWidths[position]) * 2 * (positionOffset - 0.5f), tvEndXs[position+1]);
            }
        }*/

        //内容向右滑动
        if(lastPosition > position){
            //起始点固定 结束点收缩
            if(positionOffset <= 0.5f)
                dynamicLine.updateLineView(tvStartXs[position], tvEndXs[position] + (tvWidths[lastPosition] + 2*defaultMargin)*2*positionOffset );
                //起始点伸展  结束点固定
            else
                dynamicLine.updateLineView(tvStartXs[position] + (tvWidths[position] + 2*defaultMargin)*2*(positionOffset-0.5f), tvEndXs[lastPosition]);
        }
        else{
//            if(positionOffset > 0.5f)
//                positionOffset = 0.5f;
            if(positionOffset <= 0.5f) {
                if (position + 1 < navTitles.size())
                    dynamicLine.updateLineView(tvStartXs[lastPosition], tvEndXs[position] + (tvWidths[position + 1] + 2 * defaultMargin) * 2 * positionOffset);
            }
            else {
                if (position + 1 < navTitles.size())
                    dynamicLine.updateLineView(tvStartXs[position] + (tvWidths[position] + 2 * defaultMargin) * 2 * (positionOffset - 0.5f), tvEndXs[position + 1]);
            }
        }
    }

    private int getScreenWidth(){
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }


}
