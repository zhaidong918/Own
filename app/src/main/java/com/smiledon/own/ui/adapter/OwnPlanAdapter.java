package com.smiledon.own.ui.adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.smiledon.own.R;
import com.smiledon.own.app.OwnConfig;
import com.smiledon.own.databinding.ItemPlanLayoutBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.ui.activity.EditPlanActivity;
import com.smiledon.own.utils.AnimUtils;

import org.litepal.crud.DataSupport;

/**
 * 计划列表适配器
 *
 * @author zhaidong
 * @date 2018/2/12 13:21
 */

public class OwnPlanAdapter extends BaseBindingAdapter<Plan, ItemPlanLayoutBinding> {

    /** 记录上次打开的位置 */
    private int lastOpenPosition = -1;

    public OwnPlanAdapter(Context context) {
        super(context);
    }

    public void resetLastOpenPosition() {
        lastOpenPosition = -1;
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_plan_layout;
    }

    @Override
    protected void onBindItem(ItemPlanLayoutBinding binding, Plan item, int position) {
        //item出现再视线范围内动画效果
        binding.getRoot().setAnimation(AnimationUtils.loadAnimation(context, R.anim.right_in_anim));

        binding.setPlan(item);

        binding.planDateTv.setText(item.getPlanDate());
        //判断是否是打开详情的item
        updateView(binding, lastOpenPosition == position);

        binding.getRoot().setOnClickListener(v -> {
            boolean isShowBottomLayout = binding.bottomLayout.isShown();
            updateView(binding, !isShowBottomLayout);

            if(lastOpenPosition != -1
                    && lastOpenPosition != position)
                updateView(getViewByPosition(lastOpenPosition), false);
            lastOpenPosition = isShowBottomLayout ? -1 : position;
        });
        //长按删除
        binding.getRoot().setOnLongClickListener(v -> {
            showDeleteDialog(item);
            return true;
        });

        binding.cb.setOnClickListener(v -> {
            //获取的是点击后的状态
            if (binding.cb.isChecked())
                showCompleteDialog(binding, item);
        });

        binding.editTv.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditPlanActivity.class);
            intent.putExtra(Plan.TAG, item);
            context.startActivity(intent);
        });

    }


    /*
      1.利用滑动重新调整非可见Item
      2.对于可见的Item通过view主动设置值
     */

    /**
     * 是否显示所有
     * @param showDetail
     */
    private void updateView(ItemPlanLayoutBinding binding, boolean showDetail) {
        if(binding == null) return;
        binding.tv.setMaxLines(showDetail ? Integer.MAX_VALUE : 8);
        AnimUtils.setVisibleScaleAnim(binding.bottomLayout, showDetail ? View.VISIBLE : View.GONE);
    }

    private void showCompleteDialog(ItemPlanLayoutBinding binding, Plan item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(item.getPlan())
                .setNegativeButton("还要加油", (dialog, which) ->{
                    dialog.dismiss();
                    binding.cb.setChecked(false);
                })
                .setPositiveButton("标记完成", (dialog, which) -> {
                    item.setIs_complete(OwnConfig.Plan.TabOne.COMPLETE);
                    //刷新item
                    binding.setPlan(item);
                    //更新数据源
                    updatePlan(item);
                });

        builder.show();
    }

    private void showDeleteDialog(Plan item) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(item.getPlan())
                .setNegativeButton("暂不移除", (dialog, which) ->{
                    dialog.dismiss();
                })
                .setPositiveButton("移除计划", (dialog, which) -> {
                    items.remove(item);
                    removePlan(item);
                });

        builder.show();
    }

    private void updatePlan(Plan item) {
        ContentValues values = new ContentValues();
        values.put("is_complete", item.getIs_complete());
        DataSupport.update(Plan.class, values, item.getId());
    }

    private void removePlan(Plan item) {
        DataSupport.delete(Plan.class, item.getId());
    }
}
