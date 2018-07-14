package com.smiledon.own.ui.adapter;

import android.content.Context;

import com.smiledon.own.R;
import com.smiledon.own.databinding.ItemHouseLoanLayoutBinding;
import com.smiledon.own.service.model.api.LoanInfo;
import com.smiledon.own.ui.activity.HouseLoanActivity;

import java.text.NumberFormat;
import java.util.List;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class HouseLoanAdapter extends BindingAdapter<LoanInfo, ItemHouseLoanLayoutBinding> {


    public HouseLoanAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_house_loan_layout;
    }

    @Override
    protected void onBindItem(ItemHouseLoanLayoutBinding binding, LoanInfo item, int position){

        binding.mouthTv.setText(item.month);
        binding.payTv.setText(item.getPay());

    }

    @Override
    protected void onBindItem(ItemHouseLoanLayoutBinding binding, LoanInfo item, int position, List payloads) {
        if (payloads == null && payloads.isEmpty()) {
            onBindItem(binding, item, position);
        }
    }
}


