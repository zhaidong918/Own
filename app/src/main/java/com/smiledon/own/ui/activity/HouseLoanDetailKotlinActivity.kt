package com.smiledon.own.ui.activity

import android.os.Bundle
import android.view.View
import com.smiledon.own.R
import com.smiledon.own.base.activity.BaseActivity
import com.smiledon.own.service.model.api.LoanInfo
import com.smiledon.own.ui.adapter.HouseLoanDetailAdapter
import com.smiledon.own.ui.adapter.HouseLoanDetailKotlinAdapter
import com.smiledon.own.widgets.DiffLinearLayoutManager
import kotlinx.android.synthetic.main.activity_house_loan_detail.*

/**
 * file description
 *
 *@auther ZhaiDong
 *@date  2018/6/13 15:26
 */
class HouseLoanDetailKotlinActivity : BaseActivity(){

    private lateinit var mCapitalAdapter: HouseLoanDetailKotlinAdapter
    private lateinit var mInterestAdapter: HouseLoanDetailKotlinAdapter
    private lateinit var mMonthAdapter: HouseLoanDetailKotlinAdapter
    private lateinit var mDiffAdapter: HouseLoanDetailKotlinAdapter
    private lateinit var mEarningsAdapter: HouseLoanDetailKotlinAdapter

    private val mDiffData: ArrayList<LoanInfo> = ArrayList()
    private val mEarningsData: ArrayList<LoanInfo> = ArrayList()

    override fun createContentView(): View {
        return inflateContentView(R.layout.activity_house_loan_detail)
    }

    override fun initView(savedInstanceState: Bundle?) {

        mCapitalAdapter = HouseLoanDetailKotlinAdapter(mContext)
        capital_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        capital_recycler_view.adapter = mCapitalAdapter

        mInterestAdapter = HouseLoanDetailKotlinAdapter(mContext)
        interest_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        interest_recycler_view.adapter = mInterestAdapter

        mMonthAdapter = HouseLoanDetailKotlinAdapter(mContext, false)
        month_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        month_recycler_view.adapter = mMonthAdapter

        mDiffAdapter = HouseLoanDetailKotlinAdapter(mContext)
        diff_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        diff_recycler_view.adapter = mDiffAdapter

        mEarningsAdapter = HouseLoanDetailKotlinAdapter(mContext)
        earnings_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        earnings_recycler_view.adapter = mEarningsAdapter


        for (i in 0 until LoanManager.getInstance().mCapitalLoan.size){
            var loanInfo = LoanInfo()
            loanInfo.pay = LoanManager.getInstance().mInterestLoan[i].pay - LoanManager.getInstance().mCapitalLoan[i].pay
            mDiffData.add(loanInfo)
        }

        mCapitalAdapter.setItemsNotify(LoanManager.getInstance().mCapitalLoan)
        mInterestAdapter.setItemsNotify(LoanManager.getInstance().mInterestLoan)
        mMonthAdapter.setItemsNotify(LoanManager.getInstance().mCapitalLoan)
        mDiffAdapter.setItemsNotify(mDiffData)

        capital_lixi_tv.text = getString(R.string.s, LoanManager.getInstance().mTotalCapitalLixi)
        interest_lixi_tv.text = getString(R.string.s, LoanManager.getInstance().mTotalInterestLixi)

        lixi_tv.text = getString(R.string.s, LoanManager.getInstance().mTotalInterestLixi - LoanManager.getInstance().mTotalCapitalLixi)

        calculate()

    }

    private val earningsRate = 0.0001
    private var earningsCapital: Double = 0.0
    private var earningsInterest: Double = 0.0

    private fun calculate(){

        loop@ for(i in 0 until mDiffData.size){

            when{
                i == 0 && LoanManager.getInstance().isAdvanced ->{
                    val earnings = LoanInfo()
                    earnings.pay = 0.0
                    mEarningsData.add(earnings)
                    continue@loop
                }
            }

            val loanInfo = mDiffData[i]
            val earnings = LoanInfo()
            earnings.pay = loanInfo.pay * (mDiffData.size - i) * 30 * earningsRate
            mEarningsData.add(earnings)

            when {
                earnings.pay < 0 -> earningsInterest += earnings.pay
                else -> earningsCapital += earnings.pay
            }

        }

        mEarningsAdapter.setItemsNotify(mEarningsData)
        earnings_capital_tv.text = getString(R.string.s, earningsCapital)
        earnings_interest_tv.text = getString(R.string.s, Math.abs(earningsInterest))

        earnings_tv.text = getString(R.string.s, Math.abs(earningsInterest) - Math.abs(earningsCapital))

    }

}