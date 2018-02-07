package com.smiledon.own.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.service.model.RunRecord;
import com.smiledon.own.utils.DisplayUtils;
import com.smiledon.own.utils.ResourcesUtils;

import java.util.List;

/**
 *
 * 自定义RecyclerViewb分组标题
 *
 * @author East Chak
 * @date 2018/1/20 12:35
 */

public class RecordTitleItemDecoration extends RecyclerView.ItemDecoration {

    private int mTitleHeight;
    private List<RunRecord> dataList;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;
    private Rect mBounds;


    public RecordTitleItemDecoration(List<RunRecord> dataList) {

        mTitleHeight = DisplayUtils.dp2px(30);
        mBounds = new Rect();
        this.dataList = dataList;

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
                if (isNeedDo(position, -1)) {
                    outRect.set(0, mTitleHeight, 0, 0);//不为空 且跟前一个getGroup()不一样了，说明是新的分类，也要title
                } else {
                    outRect.set(0, 0, 0, 0);
                }
            }
        }

    }

    public boolean isNeedDo(int position, int next) {
        return dataList.get(position).getMouth() != dataList.get(position + next).getMouth();
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
                    if (isNeedDo(position, -1)) {
                        //不为空 且跟前一个getGroup()不一样了，说明是新的分类，也要title
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
        mTextPaint.getTextBounds(dataList.get(position).getGroup(), 0, dataList.get(position).getGroup().length(), mBounds);
        c.drawText(dataList.get(position).getGroup(), child.getPaddingLeft() + 20, child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mTextPaint);
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

        String mouth = dataList.get(position).getGroup();
        //View child = parent.getChildAt(pos);
        View child = parent.findViewHolderForLayoutPosition(position).itemView;//出现一个奇怪的bug，有时候child为空，所以将 child = parent.getChildAt(i)。-》 parent.findViewHolderForLayoutPosition(pos).itemView

        boolean isTranslate = false;//定义一个flag，Canvas是否位移过的标志

        if ((position + 1) < dataList.size()) {//防止数组越界（一般情况不会出现）
            if (isNeedDo(position, +1)) {//当前第一个可见的Item的getGroup()，不等于其后一个item的getGroup()，说明悬浮的View要切换了
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
        mTextPaint.getTextBounds(mouth, 0, mouth.length(), mBounds);
        c.drawText(mouth, child.getPaddingLeft() + 20,
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                mTextPaint);

        if (isTranslate)
            c.restore();//恢复画布到之前保存的状态
    }
}
