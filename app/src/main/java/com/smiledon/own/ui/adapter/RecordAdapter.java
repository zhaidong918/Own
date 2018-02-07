package com.smiledon.own.ui.adapter;

import android.content.Context;

import com.smiledon.own.R;
import com.smiledon.own.databinding.ItemRecordLayoutBinding;
import com.smiledon.own.service.model.RunRecord;
import com.smiledon.own.utils.LogUtil;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class RecordAdapter extends BaseBindingAdapter<RunRecord, ItemRecordLayoutBinding> {


    public RecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_record_layout;
    }

    @Override
    protected void onBindItem(ItemRecordLayoutBinding binding, RunRecord runRecord, int position){

        binding.runDateTv.setText(runRecord.getDay());
        binding.runTimeTv.setText(runRecord.getRunTime());

    }
}


