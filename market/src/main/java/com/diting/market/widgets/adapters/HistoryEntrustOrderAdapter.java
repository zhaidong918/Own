package com.diting.market.widgets.adapters;

import android.content.Context;

import com.diting.market.R;
import com.diting.market.databinding.ActivityHistoryEntrustItemBinding;
import com.diting.market.databinding.FragmentMarketOrderEntrustItemBinding;
import com.diting.market.widgets.fragments.AppConstant;

/**
 * 历史委托单适配器
 *
 * @author zhaidong
 * @date   2018/5/9 11:40
 */

public class HistoryEntrustOrderAdapter extends BaseBindingAdapter<Integer, ActivityHistoryEntrustItemBinding>{


    public HistoryEntrustOrderAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.activity_history_entrust_item;
    }

    @Override
    protected void onBindItem(ActivityHistoryEntrustItemBinding binding, Integer item, int position) {

        if(AppConstant.OrderType.BUY == item){
            binding.itemOrderTypeTv.setTextColor(context.getResources().getColor(R.color.blue_color));
            binding.itemOrderTypeTv.setText(context.getResources().getString(R.string.buy_in));
        }
        else{
            binding.itemOrderTypeTv.setTextColor(context.getResources().getColor(R.color.orange_color));
            binding.itemOrderTypeTv.setText(context.getResources().getString(R.string.sell_out));
        }


    }

}
