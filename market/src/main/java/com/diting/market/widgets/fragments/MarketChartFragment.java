package com.diting.market.widgets.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diting.market.R;
import com.diting.market.databinding.FragmentMarketChartBinding;
import com.diting.market.utils.LayoutUtil;
import com.diting.market.widgets.view.NewGCandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 市场-图表
 *
 * @author zhaidong
 * @date   2018/5/8 11:40
 */

public class MarketChartFragment extends BaseMarketFragment {

    FragmentMarketChartBinding mBinding;

    private String[] kLineTypes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_chart, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView(){

        initTabLayout();
        initChartLayout();
        updateData();

    }

    private void initTabLayout() {

        kLineTypes = getResources().getStringArray(R.array.kline_type);

        for (String kLineType : kLineTypes) {

            TabLayout.Tab tab = mBinding.tabLayout.newTab().setText(kLineType);
            mBinding.tabLayout.addTab(tab);
        }

        mBinding.tabLayout.post(new Runnable() {
            @Override
            public void run() {
                LayoutUtil.setIndicator(mBinding.tabLayout, 20, 20);
            }
        });

        // Checked选中为升
        mBinding.priceTrendTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.priceTrendTv.toggle();
                mBinding.priceTrendTv.setText(
                        (mBinding.priceTrendTv.isChecked() ? "+" : "-")
                                + mBinding.priceTrendTv.getText().toString()
                                .replace("+", "")
                                .replace("-", ""));
            }
        });

    }

    private void updateData(){

        mBinding.cnyPriceTv.setText(getString(R.string.chart_cny_price, "0.1125454"));
        mBinding.maxPriceTv.setText(Html.fromHtml(getString(R.string.max_price_s, "0.1125454")));
        mBinding.minPriceTv.setText(Html.fromHtml(getString(R.string.min_price_s, "0.1125454")));
        mBinding.todayOpenPriceTv.setText(Html.fromHtml(getString(R.string.today_open_price_s, "0.1125454")));
        mBinding.volumeTv.setText(Html.fromHtml(getString(R.string.one_day_volume_s, "0.1125454")));

    }

    NewGCandleStickChart mChart;

    private CandleData data;

    private List<CandleEntry> yVals;


    private void initChartLayout() {

        mChart = mBinding.chartView;

        int count = 24;
        yVals=new ArrayList<>();
        for(int i=0;i<count;i++){
            yVals.add(new CandleEntry(
                    i,
                    20.00045f + i*2,
                    5.00045f + i*2,
                    10.00045f + i*2,
                    15.00045f + i*2
            ));
        }

        //高亮选中
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                CandleEntry candleEntry = (CandleEntry) e;
                mBinding.highLightTv.setText(
                        getString(R.string.high_light_info,
                                mChart.getXAxis().getValueFormatter().getFormattedValue(h.getX(), mChart.getXAxis()),
                                String.valueOf(candleEntry.getOpen()),
                                String.valueOf(candleEntry.getHigh()),
                                String.valueOf(candleEntry.getLow()),
                                String.valueOf(candleEntry.getClose())
                        )
                );
            }

            @Override
            public void onNothingSelected() {
                mBinding.highLightTv.setText("");
            }
        });


        data=new CandleData(mChart.getDataSet(yVals));
        mChart.setData(data);
        mChart.animateXY(1000, 1000);
    }

}
