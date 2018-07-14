package com.diting.market.widgets.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketBinding;

/**
 * 市场
 *
 * Created by SmileDon on 2018/5/8.
 */

public class MarketFragment extends BaseMarketFragment {

    FragmentMarketBinding mBinding;

    FragmentManager mFragmentManager;
    //图标界面
    MarketChartFragment mMarketChartFragment;
    //买卖盘界面
    MarketOrderFragment mMarketOrderFragment;
    //最新成交界面
    MarketNewestDealFragment mMarketNewestDealFragment;

    Fragment mShowFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initEvent();
    }


    private void initData(){
        mFragmentManager = getChildFragmentManager();

        if(mMarketChartFragment == null)
            mMarketChartFragment = new MarketChartFragment();

        if(mMarketOrderFragment == null)
            mMarketOrderFragment = new MarketOrderFragment();

        if(mMarketNewestDealFragment == null)
            mMarketNewestDealFragment = new MarketNewestDealFragment();
    }

    private void initEvent() {

        mBinding.marketRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.chart_btn:
                        updateRadioChecked(mBinding.chartBtn);
                        switchFragment(mMarketChartFragment);
                        break;
                    case R.id.order_btn:
                        updateRadioChecked(mBinding.orderBtn);
                        switchFragment(mMarketOrderFragment);
                        break;
                    case R.id.newest_deal_btn:
                        updateRadioChecked(mBinding.newestDealBtn);
                        switchFragment(mMarketNewestDealFragment);
                        break;
                }
            }
        });

        mBinding.orderBtn.setChecked(true);
    }

    private RadioButton mCurrentRadioButton;

    /**
     * 设置selector 切换时背景.9变形
     */
    private void updateRadioChecked(RadioButton radioButton){

        if(mCurrentRadioButton != null)
            mCurrentRadioButton.setBackgroundResource(R.color.transparent);

        if(radioButton != null)
        radioButton.setBackgroundResource(R.drawable.bg_market_top_btn);

        mCurrentRadioButton = radioButton;
    }

    /**
     * 使用hide和show方法切换Fragment
     * @param fragment 需要切换的fragment
     */
    private void switchFragment(Fragment fragment) {

        if(mShowFragment == null){
            mFragmentManager.beginTransaction().add(R.id.container, fragment).commit();
            mShowFragment = fragment;
        }
        else{
            if (fragment != mShowFragment) {
                if (!fragment.isAdded()) {
                    mFragmentManager.beginTransaction().hide(mShowFragment)
                            .add(R.id.container, fragment).commit();
                }
                else {
                    mFragmentManager.beginTransaction().hide(mShowFragment)
                            .show(fragment).commit();
                }
                mShowFragment = fragment;
            }
        }
    }
}
