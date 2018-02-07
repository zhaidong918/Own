package com.smiledon.own.service.model;

/**
 * @author East Chak
 * @date 2018/1/17 13:46
 */

public class PM2Dot5 {

    /*{
        "aqi": 290,
            "city": "合肥",
            "district": "合肥",
            "fetureData": [
        {
            "aqi": 214,
                "date": "2015-12-24",
                "quality": "重度污染"
        },
        ...
      ],
        "hourData": [
        {
            "aqi": 217,
                "dateTime": "2015-12-2314:00:00"
        },
        {
            "aqi": 250,
                "dateTime": "2015-12-2313:00:00"
        },
        ...
      ],
        "no2": 55,
            "pm25": 167,
            "province": "安徽",
            "quality": "严重污染",
            "so2": 19,
            "updateTime": "2015-12-2315:00:00"
    }*/

    public int aqi;             //空气质量指数
    public int no2;             //
    public int pm25;            //污染物pm2.5
    public int so2;
    public String quality;      //空气质量

}
