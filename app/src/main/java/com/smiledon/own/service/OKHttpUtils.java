package com.smiledon.own.service;


import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by oukenghua on 2018/1/15.
 */

public class OKHttpUtils {

    //定义接口回调
    public interface ServerCallBack{
        void onSuccess(Object object);
    }



    public static <T> void update_password(final String url, final Map<String,Object> map, Class<T> tClass,final ServerCallBack callBack){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        try{
//                    Looper.prepare();
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            // TODO: 2018/1/15 用Map传值
            for(String key : map.keySet()){
                Object o = map.get(key);
                builder.add(key,o.toString());
                Log.d("key:",key);
                Log.d("value:",o.toString());
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //如果你不处理异常信息就不需要onFailed方法了
//                            callBack.onFailed(null);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        String data = response.body().string();
                        // TODO: 2018/1/15 接口回调
                        callBack.onSuccess(new Gson().fromJson(data, tClass));
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
//        }).start();

}