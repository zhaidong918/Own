package com.smiledon.own.ui.adapter;

import android.content.Context;

import com.smiledon.own.R;
import com.smiledon.own.databinding.ItemAndroidSystemLayoutBinding;
import com.smiledon.own.databinding.ItemRecordLayoutBinding;
import com.smiledon.own.service.model.AndroidSystemInfo;
import com.smiledon.own.service.model.RunRecord;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class AndroidSystemAdapter extends BaseBindingAdapter<AndroidSystemInfo, ItemAndroidSystemLayoutBinding> {


    public AndroidSystemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_android_system_layout;
    }

    @Override
    protected void onBindItem(ItemAndroidSystemLayoutBinding binding, AndroidSystemInfo info, int position){

        binding.setInfo(info);
    }
}


