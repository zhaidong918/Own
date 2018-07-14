package com.smiledon.own.ui.adapter;

import android.content.Context;

import com.smiledon.own.R;
import com.smiledon.own.databinding.ItemHouseLoanDetailLayoutBinding;
import com.smiledon.own.service.model.api.LoanInfo;
import com.smiledon.own.ui.activity.HouseLoanActivity;

import java.text.NumberFormat;
import java.util.List;


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
public class HouseLoanDetailAdapter extends BindingAdapter<LoanInfo, ItemHouseLoanDetailLayoutBinding> {

    NumberFormat numberFormat;

    boolean isPay = true;

    public HouseLoanDetailAdapter(Context context, boolean isPay) {
        this(context);
        this.isPay = isPay;
    }


    public HouseLoanDetailAdapter(Context context) {
        super(context);

        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);

    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_house_loan_detail_layout;
    }

    @Override
    protected void onBindItem(ItemHouseLoanDetailLayoutBinding binding, LoanInfo item, int position){

        if (isPay) {
            binding.tv.setText(numberFormat.format(item.pay));
            binding.tv.setTextColor(mContext.getResources().getColor(R.color.gold_color));
        }
        else {
            binding.tv.setText(item.month);
            binding.tv.setTextColor(mContext.getResources().getColor(R.color.txt_gray));
        }
    }

    @Override
    protected void onBindItem(ItemHouseLoanDetailLayoutBinding binding, LoanInfo item, int position, List payloads) {
        if (payloads == null && payloads.isEmpty()) {
            onBindItem(binding, item, position);
        }
    }
}


