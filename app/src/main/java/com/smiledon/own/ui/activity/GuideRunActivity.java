package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.smiledon.own.R;
import com.smiledon.own.app.RxBus;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityGuideRunBinding;
import com.smiledon.own.helper.RxHelper;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * @author East Chak
 * @date 2018/1/18 21:52
 */

public class GuideRunActivity extends BaseActivity {

    ActivityGuideRunBinding binding;

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_guide_run);
        return binding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initEvent();
    }

    private int countDown = 3;

    private void initEvent() {
        binding.layout.setOnClickListener(v -> {
            countDown();
        });

        binding.backLayout.setOnClickListener(v -> finish());

        binding.recordTv.setOnClickListener(v -> startActivity(RunRecordActivity.class));

        registerEvent();

    }


    private void registerEvent() {

        RxBus.getIntanceBus().addSubscription(RxBus.Event.LOCATION,
                RxBus.getIntanceBus().doSubscribe(AMapLocation.class,
                        aMapLocation -> {

                        }, throwable -> {

                        }));
    }



    /**
     * 倒计时
     */
    private void countDown() {

        Observable.interval(0,1,TimeUnit.SECONDS)
                .take(countDown)
                .map(aLong -> countDown - aLong)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        binding.layout.setEnabled(false);
                        binding.tv.setText(String.valueOf(countDown));
                    }

                    @Override
                    public void onNext(Long value) {
                        binding.tv.setText(String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {
                        binding.tv.setText( "GO");
                        startActivity(MapActivity.class);
                        finish();
                    }
                });
    }


}
