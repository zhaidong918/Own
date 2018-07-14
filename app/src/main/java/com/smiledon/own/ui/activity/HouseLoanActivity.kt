package com.smiledon.own.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import com.smiledon.library.utils.NumberDigitsTextWatcher
import com.smiledon.own.R
import com.smiledon.own.base.activity.BaseActivity
import com.smiledon.own.service.model.api.LoanInfo
import com.smiledon.own.ui.adapter.HouseLoanAdapter
import com.smiledon.own.utils.BigDecimalUtil
import com.smiledon.own.utils.ToastUtils
import com.smiledon.own.widgets.DiffLinearLayoutManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_house_loan.*

/**
 * 房贷计算
 *
 * Created by SmileDon on 2018/5/7.
 */

class HouseLoanActivity : BaseActivity() {


    private lateinit var mCapitalAdapter: HouseLoanAdapter
    private lateinit var mInterestAdapter: HouseLoanAdapter

    private var loanType = CAPITAL

    private val isSuitRules: Boolean
        get() {

            val loadTotal = load_total_et.text.toString()
            val loadMouth = loan_month_et.text.toString()
            val yearRate = year_rate_et.text.toString()
            val advancedMouth = advanced_month_et.text.toString()

            if (TextUtils.isEmpty(loadMouth) || TextUtils.isEmpty(loadTotal) || TextUtils.isEmpty(yearRate)) {
                ToastUtils.showToast("诶！没填完哟。亲~")
                return false
            }

            advancedMonthInt = if (TextUtils.isEmpty(advancedMouth)) {
                0
            } else {
                Integer.parseInt(advancedMouth)
            }

            loadTotalInt = Integer.parseInt(loadTotal) * 10000
            loadMonthInt = Integer.parseInt(loadMouth) * 12
            yearRateDouble = java.lang.Double.parseDouble(yearRate) / 100

            return true
        }


    private var loadTotalInt: Int = 0
    private var loadMonthInt: Int = 0
    private var advancedMonthInt: Int = 0
    private var yearRateDouble: Double = 0.toDouble()

    override fun createContentView(): View {
        return inflateContentView(R.layout.activity_house_loan)
    }

    override fun initView(savedInstanceState: Bundle?) {



        left_layout.post {
            val params = right_layout.layoutParams as LinearLayout.LayoutParams
            params.height = left_layout.height
            right_layout.layoutParams = params
        }

        mCapitalAdapter = HouseLoanAdapter(mContext)
        capital_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        capital_recycler_view.adapter = mCapitalAdapter

        mInterestAdapter = HouseLoanAdapter(mContext)
        interest_recycler_view.layoutManager = DiffLinearLayoutManager(mContext, false)
        interest_recycler_view.adapter = mInterestAdapter


        type_group.setOnCheckedChangeListener {_, checkedId ->
            when (checkedId) {
                R.id.interest_r_btn -> loanType = INTEREST
                R.id.capital_r_btn -> loanType = CAPITAL
            }
        }

        interest_r_btn.isChecked = true

        year_rate_et.addTextChangedListener(NumberDigitsTextWatcher(year_rate_et, 2, 2) { })

        calculate_btn.setOnClickListener {
            if (isSuitRules)
                calculate()
        }

        to_detail_tv.setOnClickListener {

            LoanManager.getInstance().isAdvanced = advancedMonthInt > 0

            val intent = Intent(this@HouseLoanActivity, HouseLoanDetailKotlinActivity::class.java)
            startActivity(intent)
        }

        load_total_et.setText("36")
        loan_month_et.setText("20")
        year_rate_et.setText("4.41")

    }

    private fun calculate() {

        if (to_detail_tv.isShown) {
            syncCalculateCapitalType()
            syncCalculateInterestType()
        } else {
            when (loanType) {
                CAPITAL -> syncCalculateCapitalType()
                INTEREST -> syncCalculateInterestType()
            }
        }
    }


    /**
     * 等额本息计算公式：
     * 〔贷款本金×月利率×（1＋月利率）＾还款月数〕÷〔（1＋月利率）＾还款月数－1〕
     *
     * 等额本金计算公式：
     * 每月还款金额 = （贷款本金 ÷ 还款月数）+（本金 — 已归还本金累计额）× 每月利率
     */

    /**
     * 本金还款的月还款额
     *
     * @param loanInfo
     * @param yearRate      年利率
     * @param loanTotal     贷款总额
     * @param loanMonth     贷款总月份
     * @param currentMouth  贷款当前月0～length-1)
     */
    private fun getCapitalPay(loanInfo: LoanInfo, yearRate: Double, loanTotal: Int, loanMonth: Double, currentMouth: Int) {

        val monthRate = BigDecimalUtil.div(yearRate, 12.0)
        val monthCapital = BigDecimalUtil.div(loanTotal.toDouble(), loanMonth)

        loanInfo.capital = monthCapital
        loanInfo.interest = (loanTotal - monthCapital * currentMouth) * monthRate
        loanInfo.pay = loanInfo.interest + loanInfo.capital
        loanInfo.left_loan = loanTotal - monthCapital * (currentMouth + 1)
    }

    /**
     * 本息还款的月还款额
     *
     * @param loanInfo
     * @param yearRate      年利率
     * @param loanTotal     贷款总额
     * @param loanMonth     贷款总月份
     * @param currentMouth  贷款当前月0～length-1)
     */
    private fun getInterestPay(loanInfo: LoanInfo, yearRate: Double, loanTotal: Int, loanMonth: Double, currentMouth: Int) {

        val monthRate = BigDecimalUtil.div(yearRate, 12.0)

        loanInfo.pay = loanTotal.toDouble() * monthRate * Math.pow(1 + monthRate, loanMonth) / (Math.pow(1 + monthRate, loanMonth) - 1)
        if (currentMouth == 0) {
            loanInfo.interest = loanTotal * monthRate
        } else {
            loanInfo.interest = LoanManager.getInstance().mInterestLoan[currentMouth - 1].left_loan * monthRate
        }
        loanInfo.capital = loanInfo.pay - loanInfo.interest

        if (currentMouth == 0) {
            loanInfo.left_loan = loanTotal - loanInfo.capital
        } else {
            loanInfo.left_loan = LoanManager.getInstance().mInterestLoan[currentMouth - 1].left_loan - loanInfo.capital
        }


    }


    private fun syncCalculateCapitalType() {

        calculate_btn.isEnabled = false

        Observable.create<Boolean> {
            it.onNext(calculateCapitalType())
            it.onComplete()
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mCapitalAdapter.setItemsNotify(LoanManager.getInstance().mCapitalLoan)
                    capital_tv.visibility = View.VISIBLE
                    capital_tv.text = Html.fromHtml(getString(R.string.capital_s, LoanManager.getInstance().mTotalCapitalLixi))

                    calculate_btn.isEnabled = true

                    if (LoanManager.getInstance().mCapitalLoan.size > 0 && LoanManager.getInstance().mInterestLoan.size > 0) {
                        to_detail_tv.visibility = View.VISIBLE
                    }
                }

    }


    private fun syncCalculateInterestType() {

        calculate_btn.isEnabled = false

        Observable.create<Boolean> {
            it.onNext(calculateInterestType())
            it.onComplete()
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { aBoolean ->
                    mInterestAdapter.setItemsNotify(LoanManager.getInstance().mInterestLoan)
                    interest_tv.visibility = View.VISIBLE
                    interest_tv.text = Html.fromHtml(getString(R.string.interest_s, LoanManager.getInstance().mTotalInterestLixi))

                    calculate_btn.isEnabled = true

                    if (LoanManager.getInstance().mCapitalLoan.size > 0 && LoanManager.getInstance().mInterestLoan.size > 0) {
                        to_detail_tv.visibility = View.VISIBLE
                    }
                }

    }


    /**
     * 计算  等额本金
     */
    private fun calculateCapitalType(): Boolean {

        LoanManager.getInstance().mTotalCapital = 0.0
        LoanManager.getInstance().mCapitalLoan.clear()

        for (i in 0 until loadMonthInt - advancedMonthInt) {
            val loanInfo = LoanInfo()
            loanInfo.setMonth(i)
            getCapitalPay(loanInfo, yearRateDouble, loadTotalInt, loadMonthInt.toDouble(), i)
            LoanManager.getInstance().mTotalCapital += loanInfo.pay
            LoanManager.getInstance().mCapitalLoan.add(loanInfo)
        }

        if (advancedMonthInt > 0) {
            val loanInfo = LoanInfo()
            loanInfo.month = "提前还款"
            loanInfo.pay = LoanManager.getInstance().mCapitalLoan[LoanManager.getInstance().mCapitalLoan.size - 1].left_loan
            LoanManager.getInstance().mTotalCapital += loanInfo.pay
            LoanManager.getInstance().mCapitalLoan.add(0, loanInfo)
        }

        LoanManager.getInstance().mTotalCapitalLixi = LoanManager.getInstance().mTotalCapital - loadTotalInt

        return true
    }

    /**
     * 计算  等额本息
     */
    private fun calculateInterestType(): Boolean {

        LoanManager.getInstance().mTotalInterest = 0.0
        LoanManager.getInstance().mInterestLoan.clear()

        for (i in 0 until loadMonthInt - advancedMonthInt) {
            val loanInfo = LoanInfo()
            loanInfo.setMonth(i)
            getInterestPay(loanInfo, yearRateDouble, loadTotalInt, loadMonthInt.toDouble(), i)
            LoanManager.getInstance().mTotalInterest += loanInfo.pay
            LoanManager.getInstance().mInterestLoan.add(loanInfo)
        }

        if (advancedMonthInt > 0) {
            val loanInfo = LoanInfo()
            loanInfo.month = "提前还款"
            loanInfo.pay = LoanManager.getInstance().mInterestLoan[LoanManager.getInstance().mInterestLoan.size - 1].left_loan
            LoanManager.getInstance().mTotalInterest += loanInfo.pay
            LoanManager.getInstance().mInterestLoan.add(0, loanInfo)
        }

        LoanManager.getInstance().mTotalInterestLixi = LoanManager.getInstance().mTotalInterest - loadTotalInt

        return true
    }

    companion object {

        //本金
        const val CAPITAL = 1
        //本息
        const val INTEREST = 2
    }

}
