package com.smiledon.own.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * 不可滚动的布局管理器
 * 通过DiffCallBack 更新数据使用此LinearLayoutManager
 *
 * @author zhaidong
 * @date   2018/5/19 11:40
 */
public class DiffLinearLayoutManager extends LinearLayoutManager {

    boolean isScroll = true;

    public DiffLinearLayoutManager(Context context) {
        this(context, LinearLayoutManager.VERTICAL, false);
    }

    public DiffLinearLayoutManager(Context context, boolean isScroll) {
        this(context);
        this.isScroll = isScroll;
    }

    public DiffLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        if(isScroll)
            return super.canScrollVertically();
        else
            return isScroll;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
//                e.printStackTrace();
        }
    }
}