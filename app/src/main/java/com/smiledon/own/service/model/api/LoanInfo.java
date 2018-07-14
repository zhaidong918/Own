package com.smiledon.own.service.model.api;

import java.io.Serializable;
import java.text.NumberFormat;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/5 19:23
 */

public class LoanInfo implements Serializable{

    NumberFormat numberFormat;

    public LoanInfo() {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setGroupingUsed(false);
    }

    //月份
    public String month;
    //利息
    public double interest;
    //当月偿还本金
    public double capital;
    //月供
    public double pay;
    //剩余本金
    public double left_loan;

    public void setMonth(int month) {
        this.month = "第" + (month+1) + "月";
    }

    public String getPay() {
        return numberFormat.format(pay);
    }

    public String getInterest() {
        return numberFormat.format(interest);
    }

    public String getCapital() {
        return numberFormat.format(capital);
    }

    public String getLeftLoan() {
        return numberFormat.format(left_loan);
    }
}
