package com.smiledon.own.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityWidgetBinding;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

        initCheckedTextView();

    }

    private void initCheckedTextView() {
        mBinding.checkedTextView.setOnClickListener(v -> mBinding.checkedTextView.toggle());
    }

    private String[] notices;
    private Disposable disposable;

    private void initTextSwitcher() {

        if(notices == null)
            notices = new String[]{"iPhoneX价格终于刷新，速速围观", "毕业了吗？", "工作了吗？", "有年终奖吗?", "有女朋友吗?", "有房有车吗?", "结婚了吗？"};

        long count = 0;

        //配置TextView
        mBinding.textSwitcher.setFactory(() -> {
            TextView textView = new TextView(mContext);
            textView.setSingleLine();
            textView.setTextSize(16);//字号
            textView.setTextColor(ResourcesUtils.getColor(R.color.txt_black));
            textView.setEllipsize(TextUtils.TruncateAt.END);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            return textView;
        });


        Observable.interval(0,2, TimeUnit.SECONDS)
                .map(aLong -> aLong % notices.length)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Long value) {
                        mBinding.textSwitcher.setText(notices[value.intValue()]);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTextSwitcher();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(disposable != null) disposable.dispose();
    }
}
