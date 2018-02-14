package com.smiledon.own.service.model;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 计划实体类
 *
 * @author zhaidong
 * @date 2018/2/12 13:22
 */

public class Plan extends DataSupport{

    private int id;

    /** 创建时间 */
    private Date create_date;

    /** 计划完成时间 */
    private Date date;

    /** 计划的重要和紧急程度 */
    private int level;

    /** 计划类型 （工作相关 生活相关）*/
    private int type;

    /** 计划内容 */
    private String plan;

    /** 是否完成 */
    private int is_complete;

    /** 未完成原因 */
    private String reason;

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(int is_complete) {
        this.is_complete = is_complete;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
