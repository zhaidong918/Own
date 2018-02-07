package com.smiledon.own.net;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.app.MobConfig;
import com.smiledon.own.service.model.api.MobService;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author East Chak
 * @date 2018/1/9 15:40
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private ApiService apiService;
    private MobService mobService;

    public static String baseUrl = MobConfig.API;

    private static Context mContext;

    private static RetrofitClient sNewInstance;
    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(
                AppApplication.getInstance());
    }

    public static RetrofitClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getInstance(String url) {
        sNewInstance = new RetrofitClient(AppApplication.getInstance(), url);
        return sNewInstance;
    }

    private RetrofitClient(Context context) {
        this(context, null);
    }

    private RetrofitClient(Context context, String url) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(
//                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//                .cookieJar(new NovateCookieManger(context))
//                .addInterceptor(new BaseInterceptor(mContext))
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        apiService = retrofit.create(ApiService.class);
        mobService = retrofit.create(MobService.class);

    }

    public ApiService getApiService() {
        return  apiService;
    }

    public MobService getMobService() {
        return  mobService;
    }

    public <T> T create(Class<T> tClass) {
        return retrofit.create(tClass);
    }

    public void get(String url, Map parameters, Observer<ResponseBody> observer) {
        apiService
                .executeGet(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void post(String url, Map parameters, Observer<ResponseBody> observer) {
        apiService
                .executePost(url, parameters)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
