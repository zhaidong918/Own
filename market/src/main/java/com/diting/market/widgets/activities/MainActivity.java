package com.diting.market.widgets.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.diting.market.R;
import com.diting.market.databinding.ActivityMainBinding;
import com.diting.market.widgets.fragments.MarketFragment;

/**
 * 主页
 *
 * @author zhaidong
 * @date   2018/5/8 11:40
 */
public class MainActivity extends BaseMarketActivity {

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MarketFragment()).commit();
    }
}
