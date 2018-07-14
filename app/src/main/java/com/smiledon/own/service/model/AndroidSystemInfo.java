package com.smiledon.own.service.model;

import com.smiledon.own.utils.DateUtils;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Date;

/**
 * Android系统版本实体类
 *
 * @author zhaidong
 * @date 2018/2/22 13:22
 */

public class AndroidSystemInfo extends DataSupport implements Serializable{

    public static final String TAG = AndroidSystemInfo.class.getSimpleName();

    private int id;

    private String version_number;

    private String version;

    private String release_date;

    private String alias;

    private String full_name_en;

    private String full_name_ch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion_number() {
        return version_number;
    }

    public void setVersion_number(String version_number) {
        this.version_number = version_number;
    }

    public void setVersionNumber(String version_number) {
        this.version_number = "API Level " + version_number;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFull_name_en() {
        return full_name_en;
    }

    public void setFull_name_en(String full_name_en) {
        this.full_name_en = full_name_en;
    }

    public String getFull_name_ch() {
        return full_name_ch;
    }

    public void setFull_name_ch(String full_name_ch) {
        this.full_name_ch = full_name_ch;
    }
}
