package com.smiledon.own.service.presenter;

import com.smiledon.own.app.MobConfig;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.net.RetrofitClient;
import com.smiledon.own.service.OwnObserver;
import com.smiledon.own.service.ServerResultFunction;
import com.smiledon.own.service.model.BaseListModel;
import com.smiledon.own.service.model.BaseResponse;
import com.smiledon.own.service.model.CookMenu;
import com.smiledon.own.service.model.PM2Dot5;
import com.smiledon.own.service.model.Weather;
import com.smiledon.own.service.model.api.MobService;
import com.smiledon.own.utils.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * @author East Chak
 * @date 2018/1/15 16:53
 */

public class MobPresenter extends IBasePresenter {

    private Weather weather;
    private List<CookMenu> menuList;
    private PM2Dot5 pm2Dot5;

    public void getCurrentWeather() {
        Map<String, String> params = new HashMap();
        params.put(MobConfig.KEY, MobConfig.APP_KEY);
        params.put(MobConfig.Weather.CITY, "成都");
        params.put(MobConfig.Weather.PROV, "四川");

        RetrofitClient.getInstance().create(MobService.class).getCurrentWeather(params)
                .compose(RxHelper.rxSchedulerHelper())
                .map(new ServerResultFunction())
                .subscribe(new OwnObserver<BaseResponse<List<Weather>>>() {
                    @Override
                    public void onError(String e) {
                        baseView.onError(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<List<Weather>> value) {
                        if (value != null && value.result != null && !value.result.isEmpty()) {
                            weather = value.result.get(0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(weather != null)
                            baseView.onSuccess(weather);
                    }
                });


    }

    public void getCookImageList() {
        Map<String, Object> params = new HashMap();
        params.put(MobConfig.KEY, MobConfig.APP_KEY);
        params.put(MobConfig.CookMenu.CID, "0010001007");
        params.put(MobConfig.CookMenu.PAGE, 1);
        params.put(MobConfig.CookMenu.SIZE, 20);


        RetrofitClient.getInstance().getMobService().getCookImage(params)
                .compose(RxHelper.rxSchedulerHelper())
                .map(new ServerResultFunction())
                .subscribe(new OwnObserver<BaseResponse<BaseListModel<CookMenu>>>() {
                    @Override
                    public void onError(String e) {
                        baseView.onError(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<BaseListModel<CookMenu>> value) {
                        if (value != null && value.result != null && value.result.list != null) {
                            menuList = value.result.list;
                            LogUtil.i(String.valueOf(menuList.size()));
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(menuList != null)
                            baseView.onImageLoad(menuList);
                    }
                });

    }

    public void getPM2Dot5() {
        Map<String, String> params = new HashMap();
        params.put(MobConfig.KEY, MobConfig.APP_KEY);
        params.put(MobConfig.Weather.CITY, "成都");
        params.put(MobConfig.Weather.PROV, "四川");

        RetrofitClient.getInstance().create(MobService.class).getPM2Dot5(params)
                .compose(RxHelper.rxSchedulerHelper())
                .map(new ServerResultFunction())
                .subscribe(new OwnObserver<BaseResponse<List<PM2Dot5>>>() {
                    @Override
                    public void onError(String e) {
                        baseView.onError(e);
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<List<PM2Dot5>> value) {
                        if (value != null && value.result != null && !value.result.isEmpty()) {
                            pm2Dot5 = value.result.get(0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if(pm2Dot5 != null)
                            baseView.onSuccess(pm2Dot5);
                    }
                });
    }
}
