package com.smiledon.own.service.presenter;

import com.smiledon.own.service.view.IBaseView;
import com.smiledon.own.service.view.IView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @author East Chak
 * @date 2018/1/15 14:47
 */

public abstract class IBasePresenter implements IPresenter{

    protected CompositeDisposable compositeDisposable;

    protected IBaseView baseView;

    @Override
    public void onCreate() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onStop() {
        if(!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

    @Override
    public void attachView(IView view) {
        baseView = (IBaseView) view;
    }

}
