package com.smiledon.own.ui.adapter;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.smiledon.own.R;
import com.smiledon.own.app.OwnConfig;
import com.smiledon.own.databinding.ItemPlanLayoutBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.utils.AnimUtils;
import com.smiledon.own.utils.LogUtil;

import org.litepal.crud.DataSupport;

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

        binding.getRoot().setOnLongClickListener(v -> {

            if(lastPosition != -1 && lastPosition != position)
                updateView(lastPosition);

            if (OwnConfig.Plan.TabOne.COMPLETE == item.getIs_complete()) {
                return true;
            }

            lastPosition = binding.cb.isShown() ? -1 : position ;
            updateView(binding, !binding.cb.isShown(), true);

            return true;
        });

        binding.getRoot().setOnClickListener(v -> {
            if (binding.cb.isShown()) {
                binding.cb.performClick();
            }
        });

        binding.cb.setOnClickListener(v -> {
            //获取的是点击后的选中状态
            if (binding.cb.isChecked()) {
                showCompleteDialog(binding, item, position);
            }
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
            AnimUtils.alphaAnim(tagLayout, View.VISIBLE, true);
            AnimUtils.alphaAnim(cb, View.GONE, true);
        }

    }

    private void showCompleteDialog(ItemPlanLayoutBinding binding, Plan item, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(item.getPlan())
                .setNegativeButton("还没完成", (dialog, which) ->{
                    dialog.dismiss();
                    binding.cb.setChecked(false);
                })
                .setPositiveButton("已完成", (dialog, which) -> {
                    item.setIs_complete(OwnConfig.Plan.TabOne.COMPLETE);
                    ContentValues values = new ContentValues();
                    values.put("is_complete", item.getIs_complete());
                    DataSupport.update(Plan.class, values, item.getId());
                })
                .setOnDismissListener(dialog ->{
                    binding.getRoot().performLongClick();
//                        onBindItem(binding, item, position)
                });

        builder.show();
    }
}
