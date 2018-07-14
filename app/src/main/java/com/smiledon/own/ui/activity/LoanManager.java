package com.smiledon.own.ui.activity;

import com.smiledon.own.service.model.api.LoanInfo;

import java.util.ArrayList;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/6 11:41
 */

public class LoanManager {

    private static LoanManager instance;

    public static LoanManager getInstance() {
        if (instance == null) {
            synchronized (LoanManager.class){
                if (instance == null) {
                    instance = new LoanManager();
                }
            }
        }
        return instance;
    }

    public ArrayList<LoanInfo> mCapitalLoan = new ArrayList<>();
    public ArrayList<LoanInfo> mInterestLoan = new ArrayList<>();


    //是否提前还款
    public boolean isAdvanced = false;
    //本金总额
    public double mTotalCapital;
    //本息总额
    public double mTotalInterest;

    //本金利息
    public double mTotalCapitalLixi;
    //本息利息
    public double mTotalInterestLixi;

}
