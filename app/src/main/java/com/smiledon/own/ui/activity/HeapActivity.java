package com.smiledon.own.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityUiBinding;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/29 10:39
 */

public class HeapActivity extends BaseActivity {

    ActivityUiBinding mBinding;


    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_ui);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        new Handler().postDelayed(() -> {

        },1000000);

    }
}
