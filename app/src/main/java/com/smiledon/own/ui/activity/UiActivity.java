package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityConstrainLayoutBinding;
import com.smiledon.own.databinding.ActivityUiBinding;

/**
 * Created by SmileDon on 2018/5/7.
 */

public class UiActivity extends BaseActivity {

    ActivityConstrainLayoutBinding mBinding;

    @Override
    protected View createContentView() {
//        mBinding = inflate(R.layout.activity_ui);
        mBinding = inflate(R.layout.activity_constrain_layout);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
