package com.smiledon.own.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * 基于DataBinding的RecyclerView的适配器
 *
 * @param <M>
 * @param <B>
 */
public abstract class BindingAdapter<M, B extends ViewDataBinding> extends RecyclerView.Adapter {

    protected final int EMPTY_VIEW_TYPE = 1;

    protected Context mContext;
    protected List<M> items;

    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected OnItemClickListener onItemClickListener;


    public BindingAdapter(Context context) {
        this.mContext = context;
        this.items = new ArrayList<>();
    }

    public void setItemsNotify(List list){
        setItems(list);
        notifyDataSetChanged();
    }

    public void clearItemsNotify(){

        if(items == null || items.size() == 0)
            return;
        items.clear();
        notifyDataSetChanged();
    }

    public void setItems(List list){
        items = list;
    }

    public void addItemsNotify(List list){
        items.addAll(list);
        notifyDataSetChanged();
    }


    public List<M> getItems() {
        return items;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        recyclerView = (RecyclerView) parent;
        layoutManager = recyclerView.getLayoutManager();
        B binding = DataBindingUtil.inflate(LayoutInflater.from(this.mContext), this.getLayoutResId(viewType), parent, false);
        return new BaseBindingViewHolder(binding.getRoot());


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            B binding = DataBindingUtil.getBinding(holder.itemView);
            this.onBindItem(binding, this.items.get(position), position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        try {
            B binding = DataBindingUtil.getBinding(holder.itemView);
            this.onBindItem(binding, this.items.get(position), position, payloads);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected abstract @LayoutRes
    int getLayoutResId(int viewType);

    protected abstract void onBindItem(B binding, M item, int position);

    protected abstract void onBindItem(B binding, M item, int position, List payloads);


    public class BaseBindingViewHolder extends RecyclerView.ViewHolder {
        public BaseBindingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener<M> {
        void onItemClick(M item, int position);
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }


    /**
     * 获取可见区域的view
     *
     * @param position
     * @return
     * @throws Exception
     */
    public B getViewByPosition(int position) {

        int firstVisibleItem = -1;
        int lastVisibleItem = -1;

        if (layoutManager instanceof LinearLayoutManager) {
            firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            int[] firstVisibleItems = new int[spanCount];
            int[] lastVisibleItems = new int[spanCount];
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstVisibleItems);
            firstVisibleItem = firstVisibleItems[0];
            lastVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastVisibleItems);
            lastVisibleItem = lastVisibleItems[spanCount - 1];
            position -= firstVisibleItem;
        }

        View view = null;

        if (position >= firstVisibleItem && position <= lastVisibleItem) {
            view = layoutManager.findViewByPosition(position);
            return DataBindingUtil.bind(view);
        }
        return null;
    }
}