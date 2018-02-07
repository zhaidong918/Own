package com.smiledon.own.service.model;

import java.util.List;

/**
 * @author East Chak
 * @date 2018/1/16 12:53
 */

public class  BaseListModel<T> {

    public String curPage;  //  返回码
    public String total;  // 返回说明
    public List<T> list;  //返回结果集
}
