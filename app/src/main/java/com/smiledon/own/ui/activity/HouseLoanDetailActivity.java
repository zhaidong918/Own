package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityHouseLoanDetailBinding;
import com.smiledon.own.service.model.api.LoanInfo;
import com.smiledon.own.ui.adapter.HouseLoanDetailAdapter;
import com.smiledon.own.widgets.DiffLinearLayoutManager;

import java.util.ArrayList;

/**
 * 房贷计算
 *
 * Created by SmileDon on 2018/5/7.
 */

public class HouseLoanDetailActivity extends BaseActivity {

    ActivityHouseLoanDetailBinding mBinding;

    HouseLoanDetailAdapter mCapitalAdapter;
    HouseLoanDetailAdapter mInterestAdapter;
    HouseLoanDetailAdapter mMonthAdapter;
    HouseLoanDetailAdapter mDiffAdapter;
    HouseLoanDetailAdapter mEarningsAdapter;

    ArrayList<LoanInfo> mDiffData = new ArrayList<>();
    ArrayList<LoanInfo> mEarningsData = new ArrayList<>();


 

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_house_loan_detail);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mMonthAdapter = new HouseLoanDetailAdapter(mContext, false);
        mBinding.monthRecyclerView.setLayoutManager(new DiffLinearLayoutManager(mContext, false));
        mBinding.monthRecyclerView.setAdapter(mMonthAdapter);

        mDiffAdapter = new HouseLoanDetailAdapter(mContext);
        mBinding.diffRecyclerView.setLayoutManager(new DiffLinearLayoutManager(mContext, false));
        mBinding.diffRecyclerView.setAdapter(mDiffAdapter);

        mCapitalAdapter = new HouseLoanDetailAdapter(mContext);
        mBinding.capitalRecyclerView.setLayoutManager(new DiffLinearLayoutManager(mContext, false));
        mBinding.capitalRecyclerView.setAdapter(mCapitalAdapter);

        mInterestAdapter = new HouseLoanDetailAdapter(mContext);
        mBinding.interestRecyclerView.setLayoutManager(new DiffLinearLayoutManager(mContext, false));
        mBinding.interestRecyclerView.setAdapter(mInterestAdapter);

        mEarningsAdapter = new HouseLoanDetailAdapter(mContext);
        mBinding.earningsRecyclerView.setLayoutManager(new DiffLinearLayoutManager(mContext, false));
        mBinding.earningsRecyclerView.setAdapter(mEarningsAdapter);


        for (int i = 0; i < LoanManager.getInstance().mCapitalLoan.size(); i++) {
            LoanInfo houseLoan = new LoanInfo();
            houseLoan.pay = LoanManager.getInstance().mInterestLoan.get(i).pay - LoanManager.getInstance().mCapitalLoan.get(i).pay;
            mDiffData.add(houseLoan);
        }

        mMonthAdapter.setItemsNotify(LoanManager.getInstance().mCapitalLoan);
        mDiffAdapter.setItemsNotify(mDiffData);
        mCapitalAdapter.setItemsNotify(LoanManager.getInstance().mCapitalLoan);
        mInterestAdapter.setItemsNotify(LoanManager.getInstance().mInterestLoan);

        mBinding.capitalLixiTv.setText(getString(R.string.s, LoanManager.getInstance().mTotalCapitalLixi));
        mBinding.interestLixiTv.setText(getString(R.string.s, LoanManager.getInstance().mTotalInterestLixi));

        mBinding.lixiTv.setText(getString(R.string.s, LoanManager.getInstance().mTotalInterestLixi - LoanManager.getInstance().mTotalCapitalLixi));

        calculate();

    }

    private double earningsRate = 0.0001;

    private double earningsCapital;
    private double earningsInterest;

    /**
     * 计算收益
     *  按照支付包的10000元每天1块钱的收益计算
     */
    public void calculate(){

        for (int i = 0; i < mDiffData.size(); i++) {

            if (i == 0 && LoanManager.getInstance().isAdvanced) {
                LoanInfo earnings = new LoanInfo();
                earnings.pay = 0;
                mEarningsData.add(earnings);
                continue;
            }

            LoanInfo houseLoan = mDiffData.get(i);
            LoanInfo earnings = new LoanInfo();
            earnings.pay = houseLoan.pay * (mDiffData.size() - i) * 30 * earningsRate;
            mEarningsData.add(earnings);

            if (earnings.pay < 0) {
                earningsInterest += earnings.pay;
            }
            else{
                earningsCapital += earnings.pay;
            }
        }
        mEarningsAdapter.setItemsNotify(mEarningsData);
        mBinding.earningsCapitalTv.setText(getString(R.string.s, earningsCapital));
        mBinding.earningsInterestTv.setText(getString(R.string.s, Math.abs(earningsInterest)));

        mBinding.earningsTv.setText(getString(R.string.s, Math.abs(earningsInterest) - Math.abs(earningsCapital)));

    }
}
