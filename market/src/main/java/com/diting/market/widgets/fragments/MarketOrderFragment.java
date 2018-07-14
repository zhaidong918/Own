package com.diting.market.widgets.fragments;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketOrderBinding;
import com.diting.market.widgets.activities.HistoryEntrustActivity;
import com.diting.market.widgets.adapters.EntrustOrderAdapter;
import com.diting.market.widgets.adapters.OrderPriceAdapter;
import com.diting.market.widgets.view.NewGProgressBar;

/**
 * 市场-买卖盘
 *
 * Created by SmileDon on 2018/5/8.
 */

public class MarketOrderFragment extends BaseMarketFragment {

    FragmentMarketOrderBinding mBinding;

    private OrderPriceAdapter mBuyOrderAdapter;
    private OrderPriceAdapter mSellOrderAdapter;

    private EntrustOrderAdapter mEntrustOrderAdapter;

    private int mOrderType = AppConstant.OrderType.BUY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_order, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    private void initView(){
        initLayout();
        initPriceItem();
        initEntrustOrder();
        updateViewByOrderType();
    }

    private int mOrderTitleHeight;
    private int mEntrustTitleHeight;

    private void initLayout() {

        mBinding.orderTitleLayout.post(new Runnable() {
            @Override
            public void run() {
                mOrderTitleHeight = mBinding.orderTitleLayout.getHeight();

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mBinding.entrustTitleLayout.getLayoutParams();
                layoutParams.setMargins(layoutParams.leftMargin, mOrderTitleHeight, layoutParams.rightMargin, layoutParams.bottomMargin);
                mBinding.entrustTitleLayout.setLayoutParams(layoutParams);
            }
        });

        mBinding.entrustTitleLayout.post(new Runnable() {
            @Override
            public void run() {
                mEntrustTitleHeight = mBinding.entrustTitleLayout.getHeight();
                mBinding.collapsingToolbarLayout.setMinimumHeight(mEntrustTitleHeight);
            }
        });


    }

    private void initPriceItem() {

        mBuyOrderAdapter = new OrderPriceAdapter(mContext, AppConstant.OrderType.BUY);
        mBinding.buyPriceListView.setAdapter(mBuyOrderAdapter);
        mSellOrderAdapter = new OrderPriceAdapter(mContext, AppConstant.OrderType.SELL);
        mBinding.sellPriceListView.setAdapter(mSellOrderAdapter);
    }

    private void initEntrustOrder() {
        mEntrustOrderAdapter = new EntrustOrderAdapter(mContext);
        for (int i = 0; i < 5; i++) {
            mEntrustOrderAdapter.getItems().add(i%2);
        }
        mBinding.entrustRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        mBinding.entrustRecyclerView.addItemDecoration(dividerItemDecoration);

        mBinding.entrustRecyclerView.setAdapter(mEntrustOrderAdapter);
    }

    private void updateViewByOrderType(){

        switch (mOrderType){
            case AppConstant.OrderType.BUY:
                showBuyOrder();
                break;
            case AppConstant.OrderType.SELL:
                showSellOrder();
                break;
        }
    }

    private void initEvent() {

        mBinding.switchOrderTypeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppConstant.OrderType.BUY == mOrderType){
                    mOrderType = AppConstant.OrderType.SELL;
                }
                else if(AppConstant.OrderType.SELL == mOrderType){
                    mOrderType = AppConstant.OrderType.BUY;
                }
                updateViewByOrderType();
            }
        });

        mBinding.progressBar.setOnProgressChangedListener(new NewGProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(String progress) {
                mBinding.rateTv.setText(getString(R.string.percent_s, progress));
            }
        });

        mBinding.historyEntrustBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, HistoryEntrustActivity.class));
            }
        });

    }

    private void showBuyOrder(){
        mBinding.switchOrderTypeBtn.setText(Html.fromHtml(getString(R.string.order_type_buy)));
        mBinding.priceEt.setBackgroundResource(R.drawable.bg_blue_border);
        mBinding.priceEt.setHint(R.string.buy_price);
        mBinding.quantityEt.setBackgroundResource(R.drawable.bg_blue_border);
        mBinding.quantityEt.setHint(R.string.buy_quantity);
        mBinding.placeOrderBtn.setBackgroundResource(R.drawable.bg_blue_body);
        mBinding.placeOrderBtn.setText(R.string.buy_newg);

        mBinding.progressBar.setDotColor(R.color.blue_btn_color);
    }

    private void showSellOrder(){
        mBinding.switchOrderTypeBtn.setText(Html.fromHtml(getString(R.string.order_type_sell)));
        mBinding.priceEt.setBackgroundResource(R.drawable.bg_orange_border);
        mBinding.priceEt.setHint(R.string.sell_price);
        mBinding.quantityEt.setBackgroundResource(R.drawable.bg_orange_border);
        mBinding.quantityEt.setHint(R.string.sell_quantity);
        mBinding.placeOrderBtn.setBackgroundResource(R.drawable.bg_orange_body);
        mBinding.placeOrderBtn.setText(R.string.sell_newg);

        mBinding.progressBar.setDotColor(R.color.orange_color);
    }

}
