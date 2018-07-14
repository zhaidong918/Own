package com.smiledon.own.ui.activity;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityRecyclerViewBinding;
import com.smiledon.own.databinding.ItemRecyclerViewLayoutBinding;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.ui.adapter.BaseBindingAdapter;
import com.smiledon.own.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by 95867 on 2018/4/9.
 */

public class RecyclerViewActivity extends BaseActivity {

    private final String LETTERS = "ABC";
    ActivityRecyclerViewBinding mBinding;
    private RecyclerViewAdapter mAdapter;
    private String[] oldDataArrays;
    private List<String> oldDataList;
    private String[] newDataArrays;
    private List<String> newDataList;
    private List<String> oldTestDataList = new ArrayList<>();
    private List<String> newTestDataList = new ArrayList<>();

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_recycler_view);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        // m = 10  n = 8 crash

        // m
        oldDataArrays = new String[]{"A","B","B","C","1","3","V"};
        oldDataList = Arrays.asList(oldDataArrays);

        // n
        //WrapContentLinearLayoutManager  中catch异常
//        newDataArrays = new String[]{"A","B","A","C","A","B","C","B"};
        newDataArrays = new String[]{"A","B","B","C"};

        newDataList = Arrays.asList(newDataArrays);

        mAdapter = new RecyclerViewAdapter();
        mBinding.recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
//        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setAdapter(mAdapter);

        mBinding.setHandler(new Handler());

    }

    private void addCrashTestOldData() {

        newTestDataList = new ArrayList<>();

        if(oldTestDataList.size() < 20){
            oldTestDataList.add(String.valueOf(getRandomLetter()));
            mAdapter.setData(oldTestDataList);
            mAdapter.notifyDataSetChanged();

            Observable.timer(2, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .subscribe(aLong -> addCrashTestNewData());

        }

    }

    private void addCrashTestNewData() {

        mAdapter.setData(oldTestDataList);
        mAdapter.notifyDataSetChanged();

        if(newTestDataList.size() < 20){
            newTestDataList.add(String.valueOf(getRandomLetter()));
            initDiffCallBack(oldTestDataList, newTestDataList);

            Observable.timer(2, TimeUnit.SECONDS)
                    .compose(RxHelper.rxSchedulerHelper())
                    .subscribe(aLong -> addCrashTestNewData());


        }
        else{
            addCrashTestOldData();
        }

    }



    private char getRandomLetter() {
        return LETTERS.charAt((int) (Math.random() * LETTERS.length()));
    }

    private void initDiffCallBack(List<String> oldDataList, List<String> newDataList) {

        //利用DiffUtil.calculateDiff()方法，传入一个规则DiffUtil.Callback对象，和是否检测移动item的 boolean变量，得到DiffUtil.DiffResult 的对象
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(oldDataList, newDataList), true);

        //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter，轻松成为文艺青年
        diffResult.dispatchUpdatesTo(mAdapter);

        mAdapter.setData(newDataList);
    }

    private void initDiffCallBack(List<String> newDataList) {

        //利用DiffUtil.calculateDiff()方法，传入一个规则DiffUtil.Callback对象，和是否检测移动item的 boolean变量，得到DiffUtil.DiffResult 的对象
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(mAdapter.dataList, newDataList), true);

        //利用DiffUtil.DiffResult对象的dispatchUpdatesTo（）方法，传入RecyclerView的Adapter，轻松成为文艺青年
        diffResult.dispatchUpdatesTo(mAdapter);

        mAdapter.setData(newDataList);

    }

    public class Handler {

        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.test_btn:
                    oldTestDataList = new ArrayList<>();
                    showDiff();
                    break;
                case R.id.old_btn:
                    mAdapter.setData(oldDataList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.new_btn:
                    mAdapter.setData(newDataList);
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.update_diff_old_btn:
                    initDiffCallBack(oldDataList);
                    break;
                case R.id.update_diff_new_btn:
                    initDiffCallBack(newDataList);
                    break;
                case R.id.update_item_btn:
                    if (mAdapter.getItemCount() < 4) return;
                    mAdapter.dataList.set(3, mAdapter.dataList.get(3).equals("E") ? "A" : "E");
                    mAdapter.notifyItemChanged(3);
                    break;

                case R.id.update_item_content_btn:
                    if (mAdapter.getItemCount() < 4) return;
                    mAdapter.dataList.set(3, mAdapter.dataList.get(3).equals("D") ? "A" : "D");
                    mAdapter.notifyItemChanged(3, "A");
                    break;

                case R.id.update_part_item_btn:
                    if (mAdapter.getItemCount() < 3) return;
                    mAdapter.dataList.set(1, mAdapter.dataList.get(1).equals("A") ? "B" : "A");
                    mAdapter.dataList.set(2, mAdapter.dataList.get(2).equals("B") ? "A" : "B");
                    mAdapter.notifyItemRangeChanged(1, 2);
                    break;

                case R.id.update_part_item_content_btn:
                    if (mAdapter.getItemCount() < 3) return;
                    mAdapter.dataList.set(1, mAdapter.dataList.get(1).equals("A") ? "B" : "A");
                    mAdapter.dataList.set(2, mAdapter.dataList.get(2).equals("B") ? "A" : "B");
                    mAdapter.notifyItemRangeChanged(1, 2, "A");
                    break;


            }
        }

    }

    private void showDiff() {
        List<String> newData = new ArrayList<>();

        String[] srcPersons = mAdapter.dataList.toArray(new String[0]);
        String[] destPersons= new String[srcPersons.length];

        System.arraycopy(srcPersons, 0, destPersons, 0, destPersons.length);

        newData = Arrays.asList(destPersons);

        Collections.reverse(newData);

        initDiffCallBack(newData);

    }

    private class DiffCallBack extends DiffUtil.Callback {

        private List<String> oldData;
        private List<String> newData;

        public DiffCallBack(List<String> oldDataList, List<String> newDataList) {
            this.oldData = oldDataList;
            this.newData = newDataList;
        }

        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        /**
         * Called by the DiffUtil to decide whether two object represent the same Item.
         * 被DiffUtil调用，用来判断 两个对象是否是相同的Item。
         * For example, if your items have unique ids, this method should check their id equality.
         * 例如，如果你的Item有唯一的id字段，这个方法就 判断id是否相等。
         * 本例判断name字段是否一致
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list
         * @return True if the two items represent the same object or false if they are different.
         */
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }

        /**
         * Called by the DiffUtil when it wants to check whether two items have the same data.
         * 被DiffUtil调用，用来检查 两个item是否含有相同的数据
         * DiffUtil uses this information to detect if the contents of an item has changed.
         * DiffUtil用返回的信息（true false）来检测当前item的内容是否发生了变化
         * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
         * DiffUtil 用这个方法替代equals方法去检查是否相等。
         * so that you can change its behavior depending on your UI.
         * 所以你可以根据你的UI去改变它的返回值
         * For example, if you are using DiffUtil with a
         * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter}, you should
         * return whether the items' visual representations are the same.
         * 例如，如果你用RecyclerView.Adapter 配合DiffUtil使用，你需要返回Item的视觉表现是否相同。
         * This method is called only if {@link #areItemsTheSame(int, int)} returns
         * {@code true} for these items.
         * 这个方法仅仅在areItemsTheSame()返回true时，才调用。
         *
         * @param oldItemPosition The position of the item in the old list
         * @param newItemPosition The position of the item in the new list which replaces the
         *                        oldItem
         * @return True if the contents of the items are the same or false if they are different.
         */
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }

        /**
         * When {@link #areItemsTheSame(int, int)} returns {@code true} for two items and
         * {@link #areContentsTheSame(int, int)} returns false for them, DiffUtil
         * calls this method to get a payload about the change.
         *
         * @param oldItemPosition
         * @param newItemPosition
         * @return
         */
        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            String payload = null;
            if (newData.get(newItemPosition).equals("A")) {
                payload = "B";
            }
            return TextUtils.isEmpty(payload) ? null : payload;

        }
    }


    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

        List<String> dataList = new ArrayList<>();

        public void setData(List<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new RecyclerViewAdapter.MyViewHolder(LayoutInflater.from(AppApplication.getInstance()).inflate(R.layout.item_recycler_view_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.itemTv.setText(dataList.get(position));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {

            if (payloads.isEmpty()) {
                onBindViewHolder(holder, position);
            }
            else {
                String payload = (String) payloads.get(0);//取出我们在getChangePayload（）方法返回的bundle
                if("B".equals(payload)){
                    holder.itemIv.setImageResource(R.drawable.icon_nav_run);
                }
                else if("A".equals(payload)){
                    holder.itemTv.setText(dataList.get(position));
                }
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView itemTv;
            ImageView itemIv;

            public MyViewHolder(View view) {
                super(view);
                itemTv = view.findViewById(R.id.item_tv);
                itemIv = view.findViewById(R.id.item_iv);
            }
        }
    }


    private class WrapContentLinearLayoutManager extends LinearLayoutManager {

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
            setStackFromEnd(true);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }



    private class BindingAdapter extends BaseBindingAdapter<String, ItemRecyclerViewLayoutBinding> {


        public BindingAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutResId(int viewType) {
            return R.layout.item_recycler_view_layout;
        }

        @Override
        protected void onBindItem(ItemRecyclerViewLayoutBinding binding, String item, int position) {
            binding.itemTv.setText(item);
        }
    }


}
