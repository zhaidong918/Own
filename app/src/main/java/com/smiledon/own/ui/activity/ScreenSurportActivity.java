package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.FragmentMarketChartBinding;
import com.smiledon.own.utils.LayoutUtil;

import java.util.List;

/**
 * Created by SmileDon on 2018/5/7.
 */

public class ScreenSurportActivity extends BaseActivity {

    FragmentMarketChartBinding mBinding;
    private String[] kLineTypes;
    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.fragment_market_chart);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTabLayout();
    }

    private void initTabLayout() {

        kLineTypes = getResources().getStringArray(R.array.kline_type);

        for (String kLineType : kLineTypes) {
            TabLayout.Tab tab = mBinding.tabLayout.newTab().setText(kLineType);
            mBinding.tabLayout.addTab(tab);
        }

        mBinding.tabLayout.post(() -> LayoutUtil.setIndicator(mBinding.tabLayout, 20, 20));

    }
}
