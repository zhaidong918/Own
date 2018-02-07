package com.smiledon.own.service.model.api;

import com.smiledon.own.app.MobConfig;
import com.smiledon.own.service.model.BaseListModel;
import com.smiledon.own.service.model.BaseResponse;
import com.smiledon.own.service.model.CookMenu;
import com.smiledon.own.service.model.PM2Dot5;
import com.smiledon.own.service.model.Weather;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author East Chak
 * @date 2018/1/14 17:15
 */

public interface MobService {

    @GET(MobConfig.Weather.API)
    Observable<BaseResponse<List<Weather>>> getCurrentWeather(@QueryMap Map<String, String> params);

    @GET(MobConfig.CookMenu.API)
    Observable<BaseResponse<BaseListModel<CookMenu>>> getCookImage(@QueryMap Map<String, Object> params);

    @GET(MobConfig.PM2Dot5.API)
    Observable<BaseResponse<List<PM2Dot5>>> getPM2Dot5(@QueryMap Map<String, String> params);

}
