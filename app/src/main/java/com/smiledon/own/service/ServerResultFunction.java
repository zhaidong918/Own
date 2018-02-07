package com.smiledon.own.service;

import com.smiledon.own.service.model.BaseResponse;

import io.reactivex.functions.Function;

/**
 * 服务器约定好的错误码处理
 *
 * @param <T>
 */
public class ServerResultFunction<T extends BaseResponse> implements Function<T, T> {

    @Override
    public T apply(T t) throws Exception {

        //对返回码进行判断，如果不是0，则证明服务器端返回错误信息了，便根据跟服务器约定好的错误码去解析异常
        if ("200".equals(t.retCode)) {
            //如果服务器端有错误信息返回，那么抛出异常，让下面的方法去捕获异常做统一处理
            return t;
        }
        else {
            throw new Exception(t.msg);
        }
    }
}