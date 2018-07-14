package com.smiledon.own.ui.adapter

import android.content.Context

import com.smiledon.own.R
import com.smiledon.own.databinding.ItemHouseLoanDetailLayoutBinding
import com.smiledon.own.service.model.api.LoanInfo

import java.text.NumberFormat


/**
 * @author East Chak
 * @date 2018/1/16 11:54
 */
class HouseLoanDetailKotlinAdapter(context: Context) : BindingAdapter<LoanInfo, ItemHouseLoanDetailLayoutBinding>(context) {

    private var numberFormat: NumberFormat = NumberFormat.getInstance()

    private var isPay = true

    constructor(context: Context, isPay: Boolean) : this(context) {
        this.isPay = isPay
    }

    init {
        numberFormat.maximumFractionDigits = 2
        numberFormat.isGroupingUsed = false

    }

    override fun getLayoutResId(viewType: Int): Int {
        return R.layout.item_house_loan_detail_layout
    }

    override fun onBindItem(binding: ItemHouseLoanDetailLayoutBinding, item: LoanInfo, position: Int) {

        if (isPay) {
            binding.tv.text = numberFormat.format(item.pay)
            binding.tv.setTextColor(mContext.resources.getColor(R.color.gold_color))
        } else {
            binding.tv.text = item.month
            binding.tv.setTextColor(mContext.resources.getColor(R.color.txt_gray))
        }
    }

    override fun onBindItem(binding: ItemHouseLoanDetailLayoutBinding, item: LoanInfo, position: Int, payloads: List<*>?) {
        if (payloads == null && payloads!!.isEmpty()) {
            onBindItem(binding, item, position)
        }
    }
}


