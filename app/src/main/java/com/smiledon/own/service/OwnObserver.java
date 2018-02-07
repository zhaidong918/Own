package com.smiledon.own.service;

import com.smiledon.own.service.model.BaseResponse;

import java.net.UnknownHostException;

import io.reactivex.Observer;

/**
 * 自定义观察者
 *
 * @author East Chak
 * @date 2018/1/15 17:08
 */

public abstract class OwnObserver<T extends BaseResponse> implements Observer<T> {


    @Override
    public void onError(Throwable e) {
        if (e instanceof UnknownHostException) {
            onError("网络错误");
        } else {
            e.printStackTrace();
            onError(e);
        }
    }

    public abstract void onError(String e);

}

