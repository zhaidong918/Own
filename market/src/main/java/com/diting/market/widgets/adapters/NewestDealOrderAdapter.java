package com.diting.market.widgets.adapters;

import android.content.Context;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketOrderEntrustItemBinding;
import com.diting.market.databinding.FragmentMarketOrderNewestDealItemBinding;
import com.diting.market.widgets.fragments.AppConstant;

/**
 * 最新成交适配器
 *
 * @author zhaidong
 * @date   2018/5/9 11:40
 */

public class NewestDealOrderAdapter extends BaseBindingAdapter<Integer, FragmentMarketOrderNewestDealItemBinding>{


    public NewestDealOrderAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.fragment_market_order_newest_deal_item;
    }

    @Override
    protected void onBindItem(FragmentMarketOrderNewestDealItemBinding binding, Integer item, int position) {


    }

}
