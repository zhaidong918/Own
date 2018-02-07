package com.smiledon.own.service.view;

import com.smiledon.own.service.model.CookMenu;

import java.util.List;

/**
 * @author East Chak
 * @date 2018/1/15 14:42
 */

public interface IBaseView extends IView {

     void onSuccess(Object bean);

    void onError(String msg);

    void onImageLoad(List<CookMenu> menuList);
}
