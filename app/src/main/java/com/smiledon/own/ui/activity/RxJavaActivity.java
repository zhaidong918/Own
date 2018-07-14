package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityRxJavaBinding;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.service.OwnObserver;
import com.smiledon.own.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ${DESC}
 *
 * @author zhaidong
 * @date 2018/3/6 16:51
 */

public class RxJavaActivity extends BaseActivity {

    ActivityRxJavaBinding mBinding;
    StringBuilder stringBuilder;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_rx_java);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {


        mBinding.debounceTv.setOnClickListener(v -> {

            Observable.just(20)
                    .debounce(2000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer integer) throws Exception {
                            LogUtil.e("当前数据：" + integer);
                        }

                    });
        });




        mBinding.tv.setOnClickListener(v -> {

            stringBuilder = new StringBuilder();

            Observable.timer(3, TimeUnit.SECONDS)
                    .create(new ObservableOnSubscribe<String>() {
                        @Override
                        public void subscribe(ObservableEmitter<String> e) throws Exception {
                            e.onNext("154");
                            e.onNext("156");
//                            e.onError(new Throwable("i'm wrong"));
                        }
                    })
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(String aLong) throws Exception {
                            stringBuilder.append("map:").append(aLong).append("\n");
                            LogUtil.e(stringBuilder.toString());

                            String x = "";
//                            x.length();
                            return x;
                        }
                    })
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(String integer) throws Exception {

                            Observable observable = Observable.timer(2, TimeUnit.SECONDS)
                                    .create(new ObservableOnSubscribe<String>() {
                                @Override
                                public void subscribe(ObservableEmitter<String> e) throws Exception {
                                    stringBuilder.append("flatMap:").append(1).append("\n");
                                    LogUtil.e(stringBuilder.toString());
                                    e.onNext("1");
                                    stringBuilder.append("flatMap:").append(2).append("\n");
                                    LogUtil.e(stringBuilder.toString());
                                    e.onNext("2");
                                    stringBuilder.append("flatMap:").append(3).append("\n");
                                    LogUtil.e(stringBuilder.toString());

                                    e.onError(new Throwable("i'm flatmap wrong"));

                                    e.onNext("3");
                                    stringBuilder.append("flatMap:").append(4).append("\n");
                                    LogUtil.e(stringBuilder.toString());
                                    e.onNext("4");
                                    stringBuilder.append("flatMap:").append("onComplete").append("\n");
                                    LogUtil.e(stringBuilder.toString());
                                    e.onComplete();
                                }
                            });

                            observable.compose(RxHelper.rxSchedulerHelper())
                                    .subscribe(new Observer<String>() {

                                        @Override
                                        public void onSubscribe(Disposable d) {
                                        }

                                        @Override
                                        public void onNext(String value) {
                                            stringBuilder.append("----------B---------:").append("onNext:").append(value).append("\n");
                                            LogUtil.e(stringBuilder.toString());
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onComplete() {
                                        }
                                    });


                            return observable;
                        }
                    })
                    .compose(RxHelper.rxSchedulerHelper())
                    .subscribe(new Observer<String>() {

                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            stringBuilder.append("----------A---------::").append("onError").append(e.toString()).append("\n");
                            LogUtil.e(stringBuilder.toString());
                        }

                        @Override
                        public void onNext(String value) {
                            stringBuilder.append("----------A---------::").append("onNext").append("\n");
                            LogUtil.e(stringBuilder.toString());
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        });

    }
}
