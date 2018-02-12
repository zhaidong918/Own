package com.smiledon.own.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smiledon.own.utils.LogUtil;

/**
 * 基于DataBinding的RecyclerView的适配器
 * @param <M>
 * @param <B>
 */
public abstract class BaseBindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter
{
    protected Context context;
    protected ObservableArrayList<M> items;
    protected ListChangedCallback itemsChangeCallback;

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    public BaseBindingAdapter(Context context)
    {
        this.context = context;
        this.items = new ObservableArrayList<>();
        this.itemsChangeCallback = new ListChangedCallback();
    }

    public ObservableArrayList<M> getItems()
    {
        return items;
    }

    @Override
    public int getItemCount()
    {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        recyclerView = (RecyclerView) parent;
        layoutManager = recyclerView.getLayoutManager();
        B binding = DataBindingUtil.inflate(LayoutInflater.from(this.context), this.getLayoutResId(viewType), parent, false);
        return new BaseBindingViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        B binding = DataBindingUtil.getBinding(holder.itemView);
        this.onBindItem(binding, this.items.get(position), position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        this.items.addOnListChangedCallback(itemsChangeCallback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        this.items.removeOnListChangedCallback(itemsChangeCallback);
    }

    //region 处理数据集变化
    protected void onChanged(ObservableArrayList<M> newItems)
    {
        resetItems(newItems);
        notifyDataSetChanged();
    }

    protected void onItemRangeChanged(ObservableArrayList<M> newItems, int positionStart, int itemCount)
    {
        resetItems(newItems);
        notifyItemRangeChanged(positionStart,itemCount);
    }

    protected void onItemRangeInserted(ObservableArrayList<M> newItems, int positionStart, int itemCount)
    {
        resetItems(newItems);
        notifyItemRangeInserted(positionStart,itemCount);
    }

    protected void onItemRangeMoved(ObservableArrayList<M> newItems)
    {
        resetItems(newItems);
        notifyDataSetChanged();
    }

    protected void onItemRangeRemoved(ObservableArrayList<M> newItems, int positionStart, int itemCount)
    {
        resetItems(newItems);
        notifyItemRangeRemoved(positionStart,itemCount);
    }

    protected void resetItems(ObservableArrayList<M> newItems)
    {
        this.items = newItems;
    }
    //endregion

    protected abstract @LayoutRes int getLayoutResId(int viewType);

    protected abstract void onBindItem(B binding, M item, int position);

    class ListChangedCallback extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<M>>
    {
        @Override
        public void onChanged(ObservableArrayList<M> newItems)
        {
            BaseBindingAdapter.this.onChanged(newItems);
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<M> newItems, int i, int i1)
        {
            BaseBindingAdapter.this.onItemRangeChanged(newItems,i,i1);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<M> newItems, int i, int i1)
        {
            BaseBindingAdapter.this.onItemRangeInserted(newItems,i,i1);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<M> newItems, int i, int i1, int i2)
        {
            BaseBindingAdapter.this.onItemRangeMoved(newItems);
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<M> sender, int positionStart, int itemCount)
        {
            BaseBindingAdapter.this.onItemRangeRemoved(sender,positionStart,itemCount);
        }
    }

    public class BaseBindingViewHolder extends RecyclerView.ViewHolder
    {
        public BaseBindingViewHolder(View itemView)
        {
            super(itemView);
        }
    }

    /**
     * 获取可见区域的view
     * @param position
     * @return
     * @throws Exception
     */
    public View getViewByPosition(int position){

        int firstVisibleItem = -1;
        int lastVisibleItem = -1;

        if(layoutManager instanceof LinearLayoutManager){
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            int[] firstVisibleItems = new int[spanCount];
            int[] lastVisibleItems = new int[spanCount];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstVisibleItems);
            firstVisibleItem = firstVisibleItems[0];
            lastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisibleItems);
            lastVisibleItem = lastVisibleItems[spanCount-1];
            position -= firstVisibleItem;
        }

        View view = null;
//        LogUtil.i("------------------------------------------");
//        LogUtil.i("position: " + position);
//        LogUtil.i("firstVisibleItem: " + firstVisibleItem);
//        LogUtil.i("lastVisibleItem: " + lastVisibleItem);

        if (position >= firstVisibleItem && position <= lastVisibleItem) {
            view = layoutManager.findViewByPosition(position);
        }
        return view;
    }
}