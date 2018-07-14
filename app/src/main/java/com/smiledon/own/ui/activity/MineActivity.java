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

    public void showPlan(View view) {
        startActivity(OwnPlanActivity.class);
    }

    public void showAndroidSystemInfo(View view) {
        startActivity(AndroidSystemActivity.class);
    }

    public void rxJava(View view) {
        startActivity(RxJavaActivity.class);
    }

    public void showRecyclerView(View view) {
        startActivity(RecyclerViewActivity.class);
    }

    public void showWebSocket(View view) {
        startActivity(WebSocketActivity.class);
    }

    public void showKline(View view) {
        startActivity(MPChartActivity.class);
    }

    public void showHouseLoan(View view) {
        startActivity(HouseLoanActivity.class);
    }
}
