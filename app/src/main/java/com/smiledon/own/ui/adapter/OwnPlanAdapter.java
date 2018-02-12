package com.smiledon.own.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.smiledon.own.R;
import com.smiledon.own.databinding.ItemPlanLayoutBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.utils.AnimUtils;
import com.smiledon.own.utils.LogUtil;

/**
 * 计划列表适配器
 *
 * @author zhaidong
 * @date 2018/2/12 13:21
 */

public class OwnPlanAdapter extends BaseBindingAdapter<Plan, ItemPlanLayoutBinding> {

    /** 记录上次打开的位置 */
    public int lastPosition = -1;

    public OwnPlanAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_plan_layout;
    }

    @Override
    protected void onBindItem(ItemPlanLayoutBinding binding, Plan item, int position) {
        binding.setPlan(item);

//        binding.getRoot().setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_in_anim));

        updateView(binding,  position == lastPosition, false);

        binding.getRoot().setOnClickListener(v -> {

            if(lastPosition != -1 && lastPosition != position)
                updateView(lastPosition);

            lastPosition = binding.cb.isShown() ? -1 : position ;
            updateView(binding, !binding.cb.isShown(), true);

            LogUtil.i("lastPosition : " +lastPosition);

        });

    }

    /**
     * 按照当前的参数决定界面显示
     * @param binding
     * @param isLongClick
     */
    private void updateView(ItemPlanLayoutBinding binding, boolean isLongClick, boolean isAnim) {
        AnimUtils.alphaAnim(binding.tagLayout, isLongClick ? View.GONE : View.VISIBLE, isAnim);
        AnimUtils.alphaAnim(binding.cb, isLongClick ? View.VISIBLE : View.GONE, isAnim);
    }

    /**
     * 更新之前操作的view
     * @param lastPosition
     */
    private void updateView(int lastPosition) {

        View view  = getViewByPosition(lastPosition);
        LogUtil.i("view is null : " + (view == null));
        if (view != null) {
            LinearLayout tagLayout = view.findViewById(R.id.tag_layout);
            CheckBox cb = view.findViewById(R.id.cb);
            tagLayout.setVisibility(View.VISIBLE);
            cb.setVisibility(View.GONE);
//            AnimUtils.alphaAnim(tagLayout, View.VISIBLE, true);
//            AnimUtils.alphaAnim(cb, View.GONE, true);
        }

    }
}
