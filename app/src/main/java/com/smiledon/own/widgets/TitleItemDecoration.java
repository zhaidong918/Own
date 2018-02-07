package com.smiledon.own.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.utils.DisplayUtils;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;

import java.util.List;

/**
 *
 * 自定义RecyclerViewb分组标题
 *
 * @author East Chak
 * @date 2018/1/20 12:35
 */

public class TitleItemDecoration extends RecyclerView.ItemDecoration {

    private int mTitleHeight;
    private List<DemoCity> demoCityList;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private Rect mBounds;


    public TitleItemDecoration() {

        mTitleHeight = DisplayUtils.dp2px(30);
        mBounds = new Rect();
        demoCityList = DemoCity.getDatas();

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(ResourcesUtils.getColor(R.color.background_layout));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(ResourcesUtils.getColor(R.color.colorPrimary));
        mTextPaint.setTextSize(ResourcesUtils.getDimen(R.dimen.small_txt_size));
    }

    /**
     * 在item之间留出一个区域
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
        if (position > -1) {
            if (position == 0) {//等于0肯定要有title的
                outRect.set(0, mTitleHeight, 0, 0);
            } else {//其他的通过判断
                if (null != demoCityList.get(position).tag
                        && !demoCityList.get(position).tag.equals(demoCityList.get(position - 1).tag)) {
                    outRect.set(0, mTitleHeight, 0, 0);//不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }

    }

    /**
     * 在对应的区域绘制我们想要的东西
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) {//等于0肯定要有title的
                    drawTitleArea(c, left, right, child, params, position);

                } else {//其他的通过判断
                    if (null != demoCityList.get(position).tag
                            && !demoCityList.get(position).tag.equals(demoCityList.get(position - 1).tag)) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                        //none
                    }
                }
            }
        }

    }

    /**
     * 绘制Title区域背景和文字的方法
     *
     * @param c
     * @param left
     * @param right
     * @param child
     * @param params
     * @param position
     */
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//最先调用，绘制在最下层
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mBackgroundPaint);
        mTextPaint.getTextBounds(demoCityList.get(position).tag, 0, demoCityList.get(position).tag.length(), mBounds);
        c.drawText(demoCityList.get(position).tag, child.getPaddingLeft() + 20, child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mTextPaint);
    }

    /**
     * 绘制出的内容是在RecyclerView的最上层，会遮挡住ItemView
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        onDrawCanvas(c, parent, state);
//        onDrawMoreOver(c, parent, state);

    }

    /**
     * 画出当前的效果
     * @param c
     * @param parent
     * @param state
     */
    private void onDrawCanvas(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int position = ((LinearLayoutManager)(parent.getLayoutManager())).findFirstVisibleItemPosition();

        String tag = demoCityList.get(position).tag;
        //View child = parent.getChildAt(pos);
        View child = parent.findViewHolderForLayoutPosition(position).itemView;//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView

        boolean isTranslate = false;//定义一个flag，Canvas是否位移过的标志

        if ((position + 1) < demoCityList.size()) {//防止数组越界（一般情况不会出现）
            if (null != tag && !tag.equals(demoCityList.get(position + 1).tag)) {//当前第一个可见的Item的tag，不等于其后一个item的tag，说明悬浮的View要切换了
                LogUtil.i("onDrawOver() called with: c = [" + child.getTop() + "]");//当getTop开始变负，它的绝对值，是第一个可见的Item移出屏幕的距离，
                if (child.getHeight() + child.getTop() < mTitleHeight) {//当第一个可见的item在屏幕中还剩的高度小于title区域的高度时，我们也该开始做悬浮Title的“交换动画”
                    c.save();//每次绘制前 保存当前Canvas状态，
                    isTranslate = true;

                    //一种头部折叠起来的视效，个人觉得也还不错~
                    //可与123行 c.drawRect 比较，只有bottom参数不一样，由于 child.getHeight() + child.getTop() < mTitleHeight，所以绘制区域是在不断的减小，有种折叠起来的感觉
                    c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());

                    //类似饿了么点餐时,商品列表的悬停头部切换“动画效果”
                    //上滑时，将canvas上移 （y为负数） ,所以后面canvas 画出来的Rect和Text都上移了，有种切换的“动画”感觉
                    c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
                }
            }
        }

        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mBackgroundPaint);
        mTextPaint.getTextBounds(tag, 0, tag.length(), mBounds);
        c.drawText(tag, child.getPaddingLeft() + 20,
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                mTextPaint);

        if (isTranslate)
            c.restore();//恢复画布到之前保存的状态
    }


    /**
     * 悬浮布局通过layout布局文件
     * @param c
     * @param parent
     * @param state
     */
    private void onDrawMoreOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        View toDrawView = LayoutInflater.from(AppApplication.getInstance()).inflate(R.layout.item_title_layout, parent, false);
        int toDrawWidthSpec;//用于测量的widthMeasureSpec
        int toDrawHeightSpec;//用于测量的heightMeasureSpec
        //拿到复杂布局的LayoutParams，如果为空，就new一个。
        // 后面需要根据这个lp 构建toDrawWidthSpec，toDrawHeightSpec
        ViewGroup.LayoutParams lp = toDrawView.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//这里是根据复杂布局layout的width height，new一个Lp
            toDrawView.setLayoutParams(lp);
        }

        if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec。
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.EXACTLY);
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec。
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight(), View.MeasureSpec.AT_MOST);
        } else {
            //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec。
            toDrawWidthSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        }
        //高度同理
        if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.EXACTLY);
        } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom(), View.MeasureSpec.AT_MOST);
        } else {
            toDrawHeightSpec = View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY);
        }
        //依次调用 measure,layout,draw方法，。将复杂头部显示在屏幕上
        toDrawView.measure(toDrawWidthSpec, toDrawHeightSpec);
        toDrawView.layout(parent.getPaddingLeft(), parent.getPaddingTop(),
                parent.getPaddingLeft() + toDrawView.getMeasuredWidth(), parent.getPaddingTop() + toDrawView.getMeasuredHeight());
        toDrawView.draw(c);
    }
}
