package com.smiledon.own.service.presenter;

import com.smiledon.own.service.view.IView;

/**
 * @author East Chak
 * @date 2018/1/15 14:45
 */

public interface IPresenter {

    void onCreate();

    void onStop();

    void attachView(IView view);

}
