package com.smiledon.own.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityWelcomeBinding;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.StringUtils;
import com.smiledon.own.utils.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * @author East Chak
 * @date 2018/1/3 18:33
 */

public class WelcomeActivity extends BaseActivity {


    ActivityWelcomeBinding binding;

    private int mTime = 3;
    private boolean isJump = false;

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_welcome);
        return binding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
//注：魅族pro6s-7.0-flyme6权限没有像类似6.0以上手机一样正常的提示dialog获取运行时权限，而是直接默认给了权限
        binding.countDownTv.setText(mTime + "s");
        initEvent();
        requestPermissions();
//        tryRxJavaTwo();
//        tryRxJavaThree();
//        tryRxJavaFour();
//        tryRxJavaSix();
    }

    private void initEvent() {
        binding.jumpLayout.setOnClickListener(v -> jumpToMain());
    }

    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(this);
        //请求权限全部结果
        rxPermission.request(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (!granted) {
                            ToastUtils.showToast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.");
                        }
                        //不管是否获取全部权限，进入主页面
                        initCountDown();
                    }
                });
        //分别请求权限
        //        rxPermission.requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
        //                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //                Manifest.permission.READ_CALENDAR,
        //                Manifest.permission.READ_CALL_LOG,
        //                Manifest.permission.READ_CONTACTS,
        //                Manifest.permission.READ_PHONE_STATE,
        //                Manifest.permission.READ_SMS,
        //                Manifest.permission.RECORD_AUDIO,
        //                Manifest.permission.CAMERA,
        //                Manifest.permission.CALL_PHONE,
        //                Manifest.permission.SEND_SMS)
        //注：魅族pro6s-7.0-flyme6权限没有像类似6.0以上手机一样正常的提示dialog获取运行时权限，而是直接默认给了权限。魅族pro6s动态获取权限不会回调下面的方法
        //        rxPermission.requestEach(
        //                Manifest.permission.CAMERA,
        //                Manifest.permission.READ_PHONE_STATE,
        //                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        //                Manifest.permission.READ_EXTERNAL_STORAGE,
        //                Manifest.permission.ACCESS_COARSE_LOCATION)
        //                .subscribe(new Consumer<Permission>() {
        //                    @Override
        //                    public void accept(Permission permission) throws Exception {
        //                        if (permission.granted) {
        //                            // 用户已经同意该权限
        //                            Log.d(TAG, permission.name + " is granted.");
        //                        } else if (permission.shouldShowRequestPermissionRationale) {
        //                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
        //                            Log.d(TAG, permission.name + " is denied. More info should
        // be provided.");
        //                        } else {
        //                            // 用户拒绝了该权限，并且选中『不再询问』
        //                            Log.d(TAG, permission.name + " is denied.");
        //                        }
        //                    }
        //                });
    }

     /*

        Observable 和  Observer

        1.onComplete and onError cant do together. just do one onComplete. just do one onError
        2.onComplete or onError, Observer not receive event. but Observable will continue Reverse if have event
        3.Observer not receive event after dispose(Disposable), but Observable will continue Reverse if have event
        4.CompositeDisposable.add(Disposable)  CompositeDisposable.clear();
        5.map 时间转化   flatMap变化多个发送时间 (不保证顺序)  contactMap
        6.zip 合并事件 需要多个接口数据返回后组合 （按顺序组合）
        7.filter (通过条件过滤需要的事件)
        8.sample  sample(2, TimeUnit.SECONDS)  每隔一段时间取出事件
        9.merge  合并数据源头 （比如展示数据 要求本地和网络）
        10.concat 构建多级缓存   takeFirst逐一检索 （内存 硬盘 网络）
        11.distinct 去重（自定义方式去重）  distinctUntilChange(挨着的去重)
        12.first last 只发送第一个或最后一个
        13.take（保留若干数据）takeLast      skip（跳过若干数据）skipLast

        Flowable 和 Subscriber
        多一个参数


        场景化：
        flatMap 接口相互依赖
        merge   合并数据源（同类型 非同类型）
        concat  构建多级缓存 （如加载图片  内存 硬盘 网络）
        timer   定时任务
        interval周期任务
        filter  数据过滤
        throttleFirst  防止抖动操作（按钮短时间点击）
        just    老接口适配 （不需要对方法进行调整直接返回Observable）
        combineLatest  响应式编程  （如:登陆页面 输入信息动态调整按钮可点击行）

        内存优化（轻松实现线程切换）：
        Schedulers.io()            网络请求，文件读写优先此线程
        Schedulers.newThread()     网络请求，文件读写
        Schedulers.computation()   计算量比较大

        内存泄漏：
        主动取消订阅关系
        部分网络请求，接受到数据后判断当前视图是否存在 不存在不更新
        方法：
        1）.基类中利用CompositeSubscription处理批量订阅
        2）.RxLifecycle
         */

    private void tryRxJavaOne() {


        Observable.create(new ObservableOnSubscribe<Integer>() {
            //ObservableEmitter 发射器
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Throwable("我就想发一个错误"));
                e.onComplete();
                e.onNext(4);
                e.onNext(5);
                e.onNext(6);
                e.onNext(7);
                e.onComplete();

            }
        }).subscribe(new Observer<Integer>() {

            Disposable disposable;

            //建立连接
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                LogUtil.i("onSubscribe");
            }

            @Override
            public void onNext(Integer value) {
                LogUtil.i("onNext:" + value);

                if (value == 4)
                    disposable.dispose();
            }

            @Override
            public void onError(Throwable e) {
                LogUtil.i("onError：" + e.toString());
            }

            @Override
            public void onComplete() {
                LogUtil.i("onComplete");
            }
        });

    }

    private void tryRxJavaTwo() {

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            //ObservableEmitter 发射器
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                e.onNext(1);

                e.onComplete();
                e.onNext(2);
                e.onNext(3);
//                e.onError(new Throwable("我就想发一个错误"));
                e.onNext(4);
                e.onNext(5);
                e.onNext(6);
                e.onNext(7);
                e.onComplete();

            }
        });

        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.i("AndroidSchedulers.mainThread():" + Thread.currentThread().getName() + "  integer:" + integer);
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.i("AndroidSchedulers.io():" + Thread.currentThread().getName() + "  integer:" + integer);
                    }
                })
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return "1" + integer;
                    }
                })
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(String s) throws Exception {
                        final List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < 2; i++) {
                            list.add(Integer.parseInt(s));
                        }
                        return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.i("Consumer.accept():" + Thread.currentThread().getName() + "  integer:" + integer);
                    }
                });

    }

    private void tryRxJavaThree() {
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                LogUtil.i("onNext:1");
                e.onNext(1);
                LogUtil.i("onNext:2");
                e.onNext(2);
                LogUtil.i("onNext:3");
                e.onNext(3);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread());

        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                LogUtil.i("onNext:A");
                e.onNext("A");
                LogUtil.i("onNext:B");
                e.onNext("B");
                LogUtil.i("onNext:C");
                e.onNext("C");
                LogUtil.i("onNext:D");
                e.onNext("D");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.i("onSubscribe");
            }

            @Override
            public void onNext(String value) {
                LogUtil.i(value);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                LogUtil.i("onComplete");
            }
        });
    }

    private void tryRxJavaFour() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 100 == 0;
                    }
                })
                .sample(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        LogUtil.i("" + integer);
                    }
                });

    }

    private void tryRxJavaFive() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
                LogUtil.i("onNext:1");
                e.onNext(1);
                LogUtil.i("onNext:2");
                e.onNext(2);
                LogUtil.i("onNext:3");
                e.onNext(3);
                e.onComplete();
            }
        }, BackpressureStrategy.ERROR)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        LogUtil.i(integer + "");
                    }

                    @Override
                    public void onError(Throwable t) {
                        LogUtil.e(t.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void tryRxJavaSix() {
        final int[] count = {0};
        Observable.timer(3, TimeUnit.SECONDS)
                .take(3)
                .compose(RxHelper.<Long>rxSchedulerHelper())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        binding.countDownTv.setText(count[0]++ + "s");
                    }
                });
    }

    private void initCountDown() {

//        RxHelper.createObservable(10)
//                .delay(3, TimeUnit.SECONDS)
//                .compose(RxHelper.<Integer>rxSchedulerHelper())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        binding.countDownTv.setText(integer + "s");
//                    }
//                });

        Observable.interval(1, TimeUnit.SECONDS)
                .take(3)//计时次数
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        Log.i("initCountDown", aLong + "");
                        return mTime - aLong - 1;// 3-0 3-2 3-1
                    }
                })
                .compose(RxHelper.<Long>rxSchedulerHelper())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("subscribe", "subscribe");
                    }

                    @Override
                    public void onNext(Long value) {
                        Log.i("onNext", "onNext");
                        //                        Logger.e("value = " + value);
                        if (value != 0) {
                            String s = String.valueOf(value);
                            binding.countDownTv.setText((StringUtils.isEmpty(s) ? "" : s) + "s");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        Log.i("onComplete", "onComplete");
                        jumpToMain();
                    }
                });
    }


    private void jumpToMain() {
        if (!isJump) {
            isJump = true;
            startActivity(LoginActivity.class);
            finish();
        }
    }

}
