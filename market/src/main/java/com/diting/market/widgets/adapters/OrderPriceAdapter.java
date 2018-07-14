package com.diting.market.widgets.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketOrderBuySellPriceItemBinding;
import com.diting.market.widgets.fragments.AppConstant;

/**
 * 图标盘-买入价/卖出价-列表适配器
 *
 * @author zhaidong
 * @date   2018/5/9 11:40
 */

public class OrderPriceAdapter extends BaseListAdapter {

    private int mOrderType;

    private FragmentMarketOrderBuySellPriceItemBinding mBinding;

    public OrderPriceAdapter(Context context, int orderType) {
        super(context);
        this.mOrderType = orderType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_order_buy_sell_price_item, parent, false);
        }

        if(AppConstant.OrderType.BUY == mOrderType){
            mBinding.itemPriceTv.setTextColor(context.getResources().getColor(R.color.blue_color));
            mBinding.itemQuantityTv.setTextColor(context.getResources().getColor(R.color.blue_color));
        }
        else if(AppConstant.OrderType.SELL == mOrderType){
                mBinding.itemPriceTv.setTextColor(context.getResources().getColor(R.color.orange_color));
                mBinding.itemQuantityTv.setTextColor(context.getResources().getColor(R.color.orange_color));
        }


        return mBinding.getRoot();
    }

    @Override
    public int getCount() {
        return 5;
    }
}
