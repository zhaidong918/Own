package com.smiledon.own.app;

import com.amap.api.location.AMapLocation;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {


    public static class Event{

        public static final int LOCATION = 1;
        public static final int NET_STATUS = 2;

    }


    private HashMap<Integer, CompositeDisposable> mSubscriptionMap;
    private static volatile RxBus mRxBus;
    private final Subject<Object> mSubject;
    //单列模式
    public static RxBus getIntanceBus(){
        if (mRxBus==null){
            synchronized (RxBus.class){
                if(mRxBus==null){
                    mRxBus = new RxBus();
                }
            }
        }
        return mRxBus;
    }
    public RxBus(){
         mSubject = PublishSubject.create().toSerialized();
    }

    public void post(Object o){
        mSubject.onNext(o);
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
     */
    public <T>Flowable<T> getObservable(Class<T> type){
        return mSubject.toFlowable(BackpressureStrategy.BUFFER).ofType(type);
    }
    /**
     * 一个默认的订阅方法
     *
     * @param <T>
     * @param type
     * @param next
     * @param error
     * @return
     */
    public <T> Disposable doSubscribe(Class<T> type, Consumer<T> next, Consumer<Throwable> error){
        return getObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(next,error);
    }
    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 保存订阅后的disposable
     * @param event
     * @param disposable
     */
    public void addSubscription(int event, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        if (mSubscriptionMap.get(event) != null) {
            mSubscriptionMap.get(event).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(event, disposables);
        }
    }

    /**
     * 取消订阅
     * @param event
     */
    public void unSubscribe(int event) {
        if (mSubscriptionMap == null) {
            return;
        }

        if (!mSubscriptionMap.containsKey(event)){
            return;
        }
        if (mSubscriptionMap.get(event) != null) {
            mSubscriptionMap.get(event).dispose();
        }

        mSubscriptionMap.remove(event);
    }
}