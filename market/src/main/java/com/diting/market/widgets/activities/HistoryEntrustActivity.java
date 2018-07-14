package com.diting.market.widgets.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.diting.market.R;
import com.diting.market.databinding.ActivityHistoryEntrustBinding;
import com.diting.market.widgets.adapters.HistoryEntrustOrderAdapter;

/**
 *
 * 历史委托
 *
 * @author zhaidong
 * @date   2018/5/8 11:40
 */

public class HistoryEntrustActivity extends BaseMarketActivity {

    Context mContext;

    ActivityHistoryEntrustBinding mBinding;

    HistoryEntrustOrderAdapter mHistoryEntrustOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_entrust);
        mContext = this;
        initView();
    }


    private void initView() {

        mHistoryEntrustOrderAdapter = new HistoryEntrustOrderAdapter(mContext);
        for (int i = 0; i < 5; i++) {
            mHistoryEntrustOrderAdapter.getItems().add(i%2);
        }
        mBinding.historyEntrustRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line_divider));
        mBinding.historyEntrustRecyclerView.addItemDecoration(dividerItemDecoration);

        mBinding.historyEntrustRecyclerView.setAdapter(mHistoryEntrustOrderAdapter);
    }
}
