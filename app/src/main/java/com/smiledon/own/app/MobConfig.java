package com.smiledon.own.app;

import com.smiledon.own.R;

import java.util.HashMap;
import java.util.Map;

/**
 * @author East Chak
 * @date 2018/1/13 15:34
 */

public class MobConfig {


    /**
     * 广播
     */
    public static final String ACTION_REMIND_PM2DOT5 = "action_remind_pm2dot5";



    public static final String APP_KEY = "23b1dfef2c2fe";
    public static final String KEY = "key";
    public static final String API = "http://apicloud.mob.com/";

    /**
     * http://apicloud.mob.com/v1/weather/query?key=appkey&city=通州&province=北京
     *
     */
    public static class Weather{

        public static final String API = "v1/weather/query";

        public static final String CITY = "city";
        public static final String PROV= "province";

    }

    /**
     * http://apicloud.mob.com/v1/cook/menu/search?key=appkey&cid=0010001007&page=1&size=20
     */
    public static class CookMenu{
        public static final String API = "v1/cook/menu/search";

        public static final String CID = "cid";
        public static final String PAGE= "page";
        public static final String SIZE= "size";
    }


    /**
     * http://apicloud.mob.com/environment/query?key=appkey&city=通州&province=北京
     */
    public static class PM2Dot5{
        public static final String API = "environment/query";

        public static final String CITY = "city";
        public static final String PROV= "province";
    }

    private static Map<String, Integer> weatherMap;

    public static int getWeatherImage(String key) {

        // "多云,少云,晴,阴,小雨,雨,雷阵雨,中雨,阵雨,零散阵雨,零散雷雨,小雪,雨夹雪,阵雪,霾",
        if (weatherMap == null) {
            weatherMap = new HashMap<>();
            weatherMap.put("多云", R.drawable.weather_cloudy);
            weatherMap.put("少云", R.drawable.weather_cloudy);
            weatherMap.put("晴", R.drawable.weather_sunny);
            weatherMap.put("阴", R.drawable.weather_cloudy);
            weatherMap.put("小雨", R.drawable.weather_rainy);
            weatherMap.put("雨", R.drawable.weather_rainy);
            weatherMap.put("雷阵雨", R.drawable.weather_lightning);
            weatherMap.put("中雨", R.drawable.weather_pouring);
            weatherMap.put("阵雨", R.drawable.weather_pouring);
            weatherMap.put("零散阵雨", R.drawable.weather_rainy);
            weatherMap.put("零散雷雨", R.drawable.weather_rainy);
            weatherMap.put("小雪", R.drawable.weather_hail);
            weatherMap.put("雨夹雪", R.drawable.weather_hail);
            weatherMap.put("阵雪", R.drawable.weather_hail);
            weatherMap.put("霾", R.drawable.weather_fog);
        }

        try {
            return weatherMap.get(key);
        } catch (NullPointerException e) {
            return -1;
        }
    }
}
