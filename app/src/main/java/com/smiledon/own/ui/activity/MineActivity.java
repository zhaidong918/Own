package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMineBinding;

/**
 * 我的主页
 *
 * @author zhaidong
 * @date 2018/2/9 16:42
 */

public class MineActivity extends BaseActivity {

    ActivityMineBinding mBinding;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_mine);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    public void showOriginView(View view) {
        startActivity(WidgetActivity.class);
    }
}
