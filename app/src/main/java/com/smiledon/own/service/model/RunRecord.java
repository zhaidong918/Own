package com.smiledon.own.service.model;

import android.util.Log;

import com.smiledon.own.utils.LogUtil;

import org.litepal.crud.DataSupport;

import java.util.Calendar;

/**
 * @author East Chak
 * @date 2018/1/20 19:45
 */

public class RunRecord extends DataSupport {

    public RunRecord(long start_time, long end_time, long run_time) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.run_time = run_time;
    }

    /** 开始时间  */
    public long start_time;

    /** 最终结束时间  */
    public long end_time;

    /** 运动耗时  中途可能暂停 */
    public long run_time;


    /** 运动时长 */
    public String getRunTime() {

        StringBuilder builder = new StringBuilder();
        //转化为s
        long time = run_time/1000;

        int s = (int) (time % 60);
        int m = (int) ((time - s) / 60 % 60);
        int h = (int) ((time - m*60 - s) / 60 / 60);

        if(h < 10) builder.append(0);
        builder.append(h);
        builder.append(" : ");
        if(m < 10) builder.append(0);
        builder.append(m);
        builder.append(" : ");
        if(s < 10) builder.append(0);
        builder.append(s);

        LogUtil.i(builder.toString());

        return builder.toString();
    }

    public int getMouth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start_time);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取运动日期时间点
     * @return
     */
    public String getDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(start_time);

        int day =  calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder builder = new StringBuilder();
        builder.append(day).append("日");
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 0 && hour < 8)
            builder.append("清晨");
        else if(hour < 12)
            builder.append("上午");
        else if(hour < 14)
            builder.append("中午");
        else if(hour < 18)
            builder.append("下午");
        else if(hour <= 24)
            builder.append("傍晚");
        LogUtil.i(builder.toString());
        return builder.toString();
    }

    public String getGroup() {
        return getMouth() + "月";
    }





}
