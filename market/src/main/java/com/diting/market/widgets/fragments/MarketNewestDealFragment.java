package com.diting.market.widgets.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketNewestDealBinding;
import com.diting.market.databinding.FragmentMarketOrderBinding;
import com.diting.market.widgets.adapters.EntrustOrderAdapter;
import com.diting.market.widgets.adapters.NewestDealOrderAdapter;
import com.diting.market.widgets.adapters.OrderPriceAdapter;

/**
 * 市场-最新成交
 *
 * Created by SmileDon on 2018/5/8.
 */

public class MarketNewestDealFragment extends BaseMarketFragment {

    FragmentMarketNewestDealBinding mBinding;


    private NewestDealOrderAdapter mNewestDealOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_newest_deal, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){
        initNewestDealOrder();
    }

    private void initNewestDealOrder() {
        mNewestDealOrderAdapter = new NewestDealOrderAdapter(mContext);
        for (int i = 0; i < 100; i++) {
            mNewestDealOrderAdapter.getItems().add(i);
        }
        mBinding.newestDealRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mBinding.newestDealRecyclerView.setAdapter(mNewestDealOrderAdapter);
    }

}
