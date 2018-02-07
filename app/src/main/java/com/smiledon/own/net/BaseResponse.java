package com.smiledon.own.net;

import java.io.Serializable;

/**
 * @author East Chak
 * @date 2018/1/9 16:19
 */

public class BaseResponse<T> implements Serializable{

    private int code;

    private String msg;

    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
