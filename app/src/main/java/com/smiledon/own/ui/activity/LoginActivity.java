package com.smiledon.own.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityLoginBinding;
import com.smiledon.own.net.RetrofitClient;
import com.smiledon.own.utils.DisplayUtils;
import com.smiledon.own.utils.LogUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;


/**
 * @author East Chak
 * @date 2018/1/7 17:18
 */

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initBackGround();
//        initRx();
//        initEvent();

        Map  map = new HashMap();
        map.put("account", "1111");
        map.put("password", "2222");


       LogUtil.i(getResources().getDisplayMetrics().toString());

       binding.loginBtn.setOnClickListener(v -> {
//            getCurrentFocus().clearFocus();
       });

    }

    private void initBackGround() {
        Bitmap targetBitmap = DisplayUtils.getBlurBitmap(AppApplication.getInstance(), BitmapFactory.decodeResource(getResources(), R.drawable.bg_flash), 25);
        binding.getRoot().setBackground(new BitmapDrawable(targetBitmap));
    }

    private void initRx() {

               RxTextView.text(binding.accountEt).call("");


        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                LogUtil.i("call-->" + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        })
                .subscribeOn(rx.schedulers.Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
//                        binding.accountEt.setVisibility(IView.GONE);
                        LogUtil.i("doOnSubscribe-->" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(rx.schedulers.Schedulers.newThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtil.i("call" + integer);
                        LogUtil.i("call-->" + Thread.currentThread().getName());
                    }
                });



        io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                LogUtil.i("subscribe-->" + Thread.currentThread().getName());
                e.onNext(1);
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        LogUtil.i("doOnSubscribe-->" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer s) throws Exception {
                        LogUtil.i("accept-->" + Thread.currentThread().getName());
                        LogUtil.i(s+ "");
                    }
                });

        Observable.just(1,2,3,4,5)
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtil.i(integer + "");
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtil.i(integer + "");
                    }
                });

        Observable.just(1,2,3,4,5)
                .scan(new Func2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer, Integer integer2) {
                        LogUtil.i("integer:" + integer);
                        LogUtil.i("integer2:" + integer2);
                        return integer + integer2;
                    }
                })
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        LogUtil.i("最终的结果：" + integer);
                    }
                });



        rx.Observable<CharSequence> accountObservable = RxTextView.textChanges(binding.accountEt);
        rx.Observable<CharSequence> passwordObservable = RxTextView.textChanges(binding.passwordEt);

        accountObservable.debounce(1, TimeUnit.SECONDS)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        LogUtil.i("你已经很久没有操作了");
                    }
                });


        rx.Observable.combineLatest(accountObservable, passwordObservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                return isSuitRules(charSequence, charSequence2);
            }
        })
                .compose(this.<Boolean>bindToLifecycle())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.i("解除订阅");
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        binding.loginBtn.setEnabled(aBoolean);
                    }
                });


    }

    private void initEvent() {
        Observable observable = RxView.clicks(binding.loginBtn).publish();

        observable.compose(this.bindToLifecycle())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.i("over1");
                    }
                }).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                LogUtil.i("这是第一种方式");
            }
        });

        observable.compose(this.bindToLifecycle())
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        LogUtil.i("over2");
                    }
                }).subscribe(new Action1() {
            @Override
            public void call(Object o) {
                LogUtil.i("这是第二种方式");
            }
        });
    }

    public Boolean isSuitRules (CharSequence charSequence, CharSequence charSequence2) {
        if (TextUtils.isEmpty(charSequence) || TextUtils.isEmpty(charSequence2)) {
            return false;
        }

        if (charSequence.length() < 6 || charSequence2.length() < 6) {
            return false;
        }

        return true;
    }

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_login);
        return binding.getRoot();
    }


    public void showPastLoginAccount(View view) {

    }

    public void showPassword(View view) {

    }


}
