package com.smiledon.own.net;

/**
 * Socket返回的信息基类
 *
 * @author zhaidong
 * @date   2018/5/12 11:40
 */

public class BaseSocketResponse<T> {

    private String error;

    private int id;

    private T result;

    public String getError() {
        return error == null ? "" : error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    };
}
