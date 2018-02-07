package com.smiledon.own.service.model;

import com.amap.api.maps.model.LatLng;

import org.litepal.crud.DataSupport;

/**
 * @author East Chak
 * @date 2018/1/18 18:55
 */

public class Latlng extends DataSupport{

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Latlng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = System.currentTimeMillis();
    }

    private double latitude;
    private double longitude;
    private long time;


    public long getTime() {

        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Latlng{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public LatLng getLatlng() {
        return new LatLng(latitude, longitude);
    }

}
