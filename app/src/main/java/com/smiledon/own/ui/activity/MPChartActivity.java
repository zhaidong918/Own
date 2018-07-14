package com.smiledon.own.ui.activity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMpChartBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 95867 on 2018/4/17.
 */

public class MPChartActivity extends BaseActivity {

    ActivityMpChartBinding mBinding;
    CombinedChart mChart;
    CombinedChart mBarChart;

    private List<LimitLine> xLimitLineList;

    private String[] kLineTypes;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_mp_chart);
        return mBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(Bundle savedInstanceState) {
        mChart = mBinding.chartView;
        mBarChart = mBinding.chartBarView;
        initEvent();
        initTabLayout();

        initData();
        initLineData();
        initDataSet();
        initBarData();

//        initLegend();
//        initDescription();

        initXAxis(mBarChart);
        initYAxis(mBarChart);
        initBackground(mBarChart);

        initXAxis(mChart);
        initYAxis(mChart);
        initBackground(mChart);


        CombinedData combinedData = new CombinedData(); // 创建组合图的数据源
        combinedData.setData(lineData); // 添加折线图数据源
        combinedData.setData(mCandleData);  // 添加柱形图数据源
        mChart.setData(combinedData); // 为组合图设置数据源

        mChart.animateXY(1000, 1000);
        mChart.setVisibleXRangeMaximum(24);
        mChart.setVisibleXRangeMinimum(24);

        CombinedData combinedBarData = new CombinedData(); // 创建组合图的数据源
        combinedBarData.setData(lineData); // 添加折线图数据源
        combinedBarData.setData(barData);  // 添加柱形图数据源
        mBarChart.setData(combinedBarData); // 为组合图设置数据源

        mBarChart.animateXY(1000, 1000);
        mBarChart.setVisibleXRangeMaximum(24);
        mBarChart.setVisibleXRangeMinimum(24);
    }

    private void initTabLayout() {

        /*kLineTypes = getResources().getStringArray(R.array.kline_type);

        for (String kLineType : kLineTypes) {
            TabLayout.Tab tab = mBinding.tabLayout.newTab().setText(kLineType);
            mBinding.tabLayout.addTab(tab);
        }

        mBinding.tabLayout.post(() -> LayoutUtil.setIndicator(mBinding.tabLayout, 20, 20));


        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(false);
        mBinding.cnyPriceTv.setText(numberFormat.format(0.1545454545));*/

    }

    private void initDescription() {
        //图标描述
        Description description = new Description();
        description.setText("公司年度利润");
        description.setPosition(300, 60);
        description.setTextColor(Color.WHITE);
        mChart.setDescription(description);
    }

    private void initLegend() {


        // 设置比例图标示
        Legend l = mChart.getLegend();
        // 显示位置
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        // 样式
        l.setForm(Legend.LegendForm.SQUARE);
        // 字号
        l.setFormSize(6f);
        // 颜色
        l.setTextColor(Color.WHITE);

        // 设置标注的颜色及内容，设置的效果如下图
        String[] labels = new String[2];
        labels[0] = "红涨";
        labels[1] = "绿跌";

        int[] colors = new int[2];
        colors[0] = getResources().getColor(R.color.increase_color);
        colors[1] = getResources().getColor(R.color.decrease_color);

        l.setExtra(colors, labels);

        // 显不显示标签
        l.setEnabled(false);
    }

    private void initYAxis(CombinedChart chart) {

        YAxis yRightAxis = chart.getAxisRight();

//        if (chart == mBarChart) {
        yRightAxis.setEnabled(false);
//        }
//        else {
//            yRightAxis.setTextColor(getResources().getColor(R.color.label_color));
//        yRightAxis.setDrawZeroLine(false);
        //不画Y轴的线
//            yRightAxis.setDrawAxisLine(false);
//            yRightAxis.setValueFormatter(new DefaultAxisValueFormatter(6));
//        }

        YAxis yLeftAxis = chart.getAxisLeft();
        yLeftAxis.setEnabled(false);

        //设置小数点位数

    }

    private void initXAxis(CombinedChart chart) {


        //横轴的位置
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.label_color));
        //不画X轴的线
        xAxis.setDrawAxisLine(false);

        //设置X轴的最大值  最小值
//        xAxis.setAxisMaximum(12.2f);
//        xAxis.setAxisMinimum(2.2f);

        //X轴标签在空格之间
        xAxis.setCenterAxisLabels(false);
        //是否显示标签
        xAxis.setDrawLabels(true);
        //默认显示横轴标签数
//        xAxis.setLabelCount(3);
//        xAxis.setLabelCount(10, true);
        //轴显示的最小粒度
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        //把限制线画在数据后面 true
        xAxis.setDrawLimitLinesBehindData(true);

        for (LimitLine limitLine : xLimitLineList) {
            xAxis.addLimitLine(limitLine);
        }

        xAxis.setValueFormatter(new AxisValueFormatter());

    }

    //自定义横坐标
    private class AxisValueFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            return ((int)value + 1) + ":00";
        }


    }

    CandleData mCandleData;

    private void initDataSet() {

        CandleDataSet mCandleDataSet = new CandleDataSet(yVals, "DiTing");

        mCandleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 影线颜色与实体一致
        mCandleDataSet.setShadowColorSameAsCandle(true);

        // 影线
        mCandleDataSet.setShadowWidth(1f);

        // 红涨，实体
        mCandleDataSet.setDecreasingColor(getResources().getColor(R.color.decrease_color));
        mCandleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        // 绿跌，空心
        mCandleDataSet.setIncreasingColor(getResources().getColor(R.color.increase_color));
        mCandleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);

        //open == close 时的颜色
        // 当天价格不涨不跌（一字线）颜色
        mCandleDataSet.setNeutralColor(Color.RED);
        // 选中蜡烛时的线宽
        mCandleDataSet.setHighlightLineWidth(1f);
        mCandleDataSet.setHighLightColor(getResources().getColor(R.color.highlight_color));

        // 在图表中的元素上面是否显示数值
        mCandleDataSet.setDrawValues(false);
        mCandleDataSet.setValueTextColor(getResources().getColor(R.color.label_color));

        mCandleData = new CandleData(mCandleDataSet);
    }

    private void initBackground(CombinedChart chart) {

        // 是否显示表格颜色
        chart.setDrawGridBackground(true);
        // 设置背景
        chart.setBackgroundColor(getResources().getColor(R.color.ui_market_bg));
        // 设置表格背景色
        chart.setGridBackgroundColor(getResources().getColor(R.color.ui_market_bg));

        // 加边框
        chart.setBorderColor(getResources().getColor(R.color.orange));
        chart.setDrawBorders(false);

        //xy同时缩放
        chart.setPinchZoom(false);
        //双击缩放
        chart.setDoubleTapToZoomEnabled(false);
        //Y轴根据滑动自动缩放
        chart.setAutoScaleMinMaxEnabled(false);


        //高亮选中
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                CandleEntry candleEntry = (CandleEntry) e;
                mBinding.highLightInfoTv.setText(
                        mChart.getXAxis().getValueFormatter().getFormattedValue(h.getX(), mChart.getXAxis()) + "：\t" +
                                "开：" + candleEntry.getOpen() + "\t" +
                                "高：" + candleEntry.getHigh() + "\t" +
                                "低：" + candleEntry.getLow() + "\t" +
                                "收：" + candleEntry.getClose() + "\t"
                );


            }

            @Override
            public void onNothingSelected() {
                mBinding.highLightInfoTv.setText("");
            }
        });

    }

    private List<CandleEntry> yVals;

    Random random = new Random();

    private void initData() {

        int count = 108;

        xLimitLineList = new ArrayList<>();

        LimitLine limitLine;

        yVals=new ArrayList<>();
        for(int i=0;i<count;i++){
            if (i == 0) {
                yVals.add(new CandleEntry(
                        i,
                        1f,
                        0.2f,
                        0.01f,
                        0.8f
                ));
            }
            else
                yVals.add(new CandleEntry(
                        i,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f
                ));

            limitLine = new LimitLine(i,  new DecimalFormat("00.00").format(i));
            limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            limitLine.setLineColor(Color.DKGRAY);
            limitLine.setEnabled(false);
            limitLine.setTextColor(getResources().getColor(R.color.label_color));
//            xLimitLineList.add(limitLine);

        }
    }

    LineData lineData;
    private void initLineData() {

        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < yVals.size(); i++) {
            lineEntries.add(new Entry(i, random.nextFloat()*0.3f));
        }
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "不良率");
        lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.blue));
        lineDataSet.setCircleColor(ContextCompat.getColor(mContext, R.color.transparent));
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(mContext, R.color.transparent));
        lineDataSet.setValueTextSize(0f);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setHighlightEnabled(false);
        lineData = new LineData(lineDataSet);

    }

    private BarData barData;

    private void initBarData() {
        /**
         * 初始化柱形图的数据
         * 此处用suppliers的数量做循环，因为总共所需要的数据源数量应该和标签个数一致
         * 其中BarEntry是柱形图数据源的实体类，包装xy坐标数据
         */
        /******************BarData start********************/
        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < yVals.size(); i++) {
            barEntries.add(new BarEntry(i, random.nextFloat()*0.3f));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");  // 新建一组柱形图，"LAR"为本组柱形图的Label
        barDataSet.setColor(ContextCompat.getColor(mContext, R.color.green)); // 设置柱形图颜色
        barDataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.transparent)); //  设置柱形图顶部文本颜色
        barData = new BarData(barDataSet);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initEvent(){

        mChart.setOnChartGestureListener(new CoupleChartGestureListener(mChart, new Chart[]{mBarChart}));
        mBarChart.setOnChartGestureListener(new CoupleChartGestureListener(mBarChart, new Chart[]{mChart}));

    }


    /**
     * Created by Android on 2015/12/10.
     */
    public class CoupleChartGestureListener  implements OnChartGestureListener {

        private Chart srcChart;
        private Chart[] dstCharts;


        public CoupleChartGestureListener(Chart srcChart, Chart[] dstCharts) {
            this.srcChart = srcChart;
            this.dstCharts = dstCharts;
        }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {
            for (Chart dstChart : dstCharts) {
                if (dstChart.getVisibility() == View.VISIBLE) {
                    if (srcChart.valuesToHighlight()) {
                        dstChart.highlightValue(null);
                    }
                    else{
                        Highlight h = dstChart.getHighlightByTouchPoint(me.getX(), me.getY());
                        dstChart.highlightValue(h);
                    }
                }
            }
        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            syncCharts();

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

            if (srcChart == mChart) {
                mBarChart.setOnChartGestureListener(null);
                mBarChart.setTouchEnabled(false);

            } else if (srcChart == mBarChart) {
                mChart.setOnChartGestureListener(null);
                mChart.setTouchEnabled(false);
            }

            syncCharts();

            if (srcChart == mChart) {
                mBarChart.setOnChartGestureListener(new CoupleChartGestureListener(mBarChart, new Chart[]{mChart}));
                mBarChart.setTouchEnabled(true);
            } else if (srcChart == mBarChart) {
                mChart.setOnChartGestureListener(new CoupleChartGestureListener(mChart, new Chart[]{mBarChart}));
                mChart.setTouchEnabled(true);
            }

        }

        public void syncCharts() {
            Matrix srcMatrix;
            float[] srcVals = new float[9];
            Matrix dstMatrix;
            float[] dstVals = new float[9];

            // get src chart translation matrix:
            srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
            srcMatrix.getValues(srcVals);

            // apply X axis scaling and position to dst charts:
            for (Chart dstChart : dstCharts) {
                if (dstChart.getVisibility() == View.VISIBLE) {
                    dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                    dstMatrix.getValues(dstVals);
                    dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
                    dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
                    dstMatrix.setValues(dstVals);
                    dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
                }
            }
        }
    }
}
