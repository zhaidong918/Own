package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityWidgetBinding;

/**
 * 各类自带控件样式
 *
 * @author zhaidong
 * @date 2018/2/9 16:03
 */

public class WidgetActivity extends BaseActivity {


    ActivityWidgetBinding mBinding;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_widget);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }
}
