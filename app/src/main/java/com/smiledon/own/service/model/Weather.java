package com.smiledon.own.service.model;

import java.util.List;

/**
 * @author East Chak
 * @date 2018/1/13 15:50
 */

public class Weather {

    public String airCondition;     //空气质量
    public String city;             //城市
    public String coldIndex;        //感冒指数
    public String date;             //
    public String distrct;          //
    public String dressingIndex;
    public String exerciseIndex;
    public String humidity;
    public String pollutionIndex;
    public String province;
    public String sunrise;
    public String sunset;
    public String temperature;
    public String time;
    public String updateTime;       //更新时间
    public String washIndex;
    public String weather;
    public String week;
    public String wind;

    public List<Future> future;

    @Override
    public String toString() {
        return "Weather{" +
                "airCondition='" + airCondition + '\'' +
                ", city='" + city + '\'' +
                ", coldIndex='" + coldIndex + '\'' +
                ", date='" + date + '\'' +
                ", distrct='" + distrct + '\'' +
                ", dressingIndex='" + dressingIndex + '\'' +
                ", exerciseIndex='" + exerciseIndex + '\'' +
                ", humidity='" + humidity + '\'' +
                ", pollutionIndex='" + pollutionIndex + '\'' +
                ", province='" + province + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                ", temperature='" + temperature + '\'' +
                ", time='" + time + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", washIndex='" + washIndex + '\'' +
                ", weather='" + weather + '\'' +
                ", week='" + week + '\'' +
                ", wind='" + wind + '\'' +
                ", future=" + future +
                '}';
    }

        public class Future{

        public String date;
        public String dayTime;
        public String night;
        public String temperature;
        public String week;
        public String wind;

        @Override
        public String toString() {
            return "Detail{" +
                    "date='" + date + '\'' +
                    ", dayTime='" + dayTime + '\'' +
                    ", night='" + night + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", week='" + week + '\'' +
                    ", wind='" + wind + '\'' +
                    '}';
        }
    }

}
