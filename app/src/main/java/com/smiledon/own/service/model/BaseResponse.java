package com.smiledon.own.service.model;

/**
 * @author East Chak
 * @date 2018/1/14 17:48
 */

public class BaseResponse<T> {

    public String retCode;  //  返回码
    public String msg;  // 返回说明
    public T result;  //返回结果集
}
