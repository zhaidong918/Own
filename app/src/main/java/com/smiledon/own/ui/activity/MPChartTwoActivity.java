package com.smiledon.own.ui.activity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMpChartTwoBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 95867 on 2018/4/17.
 */

public class MPChartTwoActivity extends BaseActivity {

    ActivityMpChartTwoBinding mBinding;

    CombinedChart mKlineChart;
    CombinedChart mBarChart;


    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_mp_chart_two);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mKlineChart = mBinding.klineView;
        mBarChart = mBinding.barView;

        initData();

        initBarData();
        initCandleData();

        initChartOffSet();

        initEvent();

    }

    private void initEvent() {

        mKlineChart.setDragDecelerationEnabled(false);
        mBarChart.setDragDecelerationEnabled(false);

        mKlineChart.setOnChartGestureListener(new CoupleChartGestureListener(mKlineChart, new Chart[]{mBarChart}));
        mBarChart.setOnChartGestureListener(new CoupleChartGestureListener(mBarChart, new Chart[]{mKlineChart}));

    }

    private List<CandleEntry> xValues;

    int pre = 100;

    Random random = new Random();
    private void initData() {

        int count = 108;

        xValues=new ArrayList<>();
        for(int i=0;i<count;i++){
            if (i == 0) {
                xValues.add(new CandleEntry(
                        pre + i*3,
                        1f,
                        0.2f,
                        0.01f,
                        0.8f
                ));
            }
            else
                xValues.add(new CandleEntry(
                        pre + i*3,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f,
                        random.nextFloat()*0.3f
                ));
        }
    }

    LineData lineData;

    private LineDataSet initLineData() {

        List<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            if (i == 0) {
                lineEntries.add(new Entry(pre + i*3, 0.8f));
            }
            else
                lineEntries.add(new Entry(pre + i*3, random.nextFloat()*0.3f));
        }
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "均线");

        float x = random.nextFloat() * 3f;

        if (x < 1) {
            lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.blue));
        }
        else if(x < 2){
            lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.red));
        }
        else {
            lineDataSet.setColor(ContextCompat.getColor(mContext, R.color.orange));
        }

        lineDataSet.setCircleColor(ContextCompat.getColor(mContext, R.color.transparent));
        lineDataSet.setCircleHoleColor(ContextCompat.getColor(mContext, R.color.transparent));
        lineDataSet.setValueTextSize(0f);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setHighlightEnabled(false);

        return lineDataSet;
    }

    private CandleData candleData;

    private void initCandleData() {

        CandleDataSet candleDataSet = new CandleDataSet(xValues, "KLine");
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 影线颜色与实体一致
        candleDataSet.setShadowColorSameAsCandle(true);

        // 影线
        candleDataSet.setShadowWidth(1f);

        // 红涨，实体
        candleDataSet.setDecreasingColor(getResources().getColor(R.color.decrease_color));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        // 绿跌，空心
        candleDataSet.setIncreasingColor(getResources().getColor(R.color.increase_color));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);

        //open == close 时的颜色
        // 当天价格不涨不跌（一字线）颜色
        candleDataSet.setNeutralColor(Color.RED);
        // 选中蜡烛时的线宽
        candleDataSet.setHighlightLineWidth(1f);
        candleDataSet.setHighLightColor(getResources().getColor(R.color.highlight_color));

        // 在图表中的元素上面是否显示数值
        candleDataSet.setDrawValues(false);
        candleDataSet.setValueTextColor(getResources().getColor(R.color.label_color));

        candleData = new CandleData(candleDataSet);


        LineData lineData = new LineData();
        lineData.addDataSet(initLineData());
        lineData.addDataSet(initLineData());
        lineData.addDataSet(initLineData());

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(candleData);

        XAxis xAxis = mKlineChart.getXAxis();
        xAxis.setAxisMinimum(-1.5f);
        xAxis.setAxisMaximum(xValues.size()*pre - 1.5f);
        xAxis.setTextSize(0f);
        mKlineChart.setDrawOrder(new CombinedChart.DrawOrder[]{ CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE});
        mKlineChart.setData(combinedData);

        //图标描述
        Description description = new Description();
        description.setText("");
        mKlineChart.setDescription(description);

        mKlineChart.getLegend().setEnabled(false);
        mKlineChart.setScaleEnabled(false);

        mKlineChart.animateXY(1000, 1000);
        mKlineChart.setVisibleXRangeMaximum(24);
        mKlineChart.setVisibleXRangeMinimum(24);

        mKlineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mBarChart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                mBarChart.highlightValue(null);
            }
        });
    }


    private BarData barData;

    private void initBarData() {

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < xValues.size(); i++) {
            barEntries.add(new BarEntry(pre + i*3, random.nextFloat()*0.3f));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "成交量");  // 新建一组柱形图，"LAR"为本组柱形图的Label
        barDataSet.setColor(ContextCompat.getColor(mContext, R.color.green)); // 设置柱形图颜色
        barDataSet.setValueTextColor(ContextCompat.getColor(mContext, R.color.transparent)); //  设置柱形图顶部文本颜色
        barData = new BarData(barDataSet);

        LineData lineData = new LineData();
        lineData.addDataSet(initLineData());
        lineData.addDataSet(initLineData());
        lineData.addDataSet(initLineData());

        CombinedData combinedData = new CombinedData();
        combinedData.setData(lineData);
        combinedData.setData(barData);


        //图标描述
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum(xValues.size() - 0.5f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yRightAxis = mBarChart.getAxisRight();
//        yRightAxis.setTextSize(0f);
        yRightAxis.setTextColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.transparent));

        mBarChart.getLegend().setEnabled(false);
        mBarChart.setScaleEnabled(false);
        mBarChart.setData(combinedData);
        mBarChart.animateXY(1000, 1000);
        mBarChart.setVisibleXRangeMaximum(24);
        mBarChart.setVisibleXRangeMinimum(24);

        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                mKlineChart.highlightValues(new Highlight[]{h});
            }

            @Override
            public void onNothingSelected() {
                mKlineChart.highlightValue(null);
            }
        });
    }

    /**
     * 初始化两个图左右对其 (设置完数据之后调用)
     */
    private void initChartOffSet(){

        float lineLeft = mKlineChart.getViewPortHandler().offsetLeft();
        float barLeft = mBarChart.getViewPortHandler().offsetLeft();
        float lineBottom = mKlineChart.getViewPortHandler().offsetBottom();
        float lineRight = mKlineChart.getViewPortHandler().offsetRight();
        float barRight = mBarChart.getViewPortHandler().offsetRight();
        float barTop = mBarChart.getViewPortHandler().offsetTop();
        float offsetLeft, offsetRight;

        if (barLeft < lineLeft) {
            offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            mBarChart.setExtraLeftOffset(offsetLeft);
        }
        else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            mKlineChart.setExtraLeftOffset(offsetLeft);
        }

        if (barRight < lineRight) {
            offsetRight = Utils.convertPixelsToDp(lineRight - barRight);
            mBarChart.setExtraRightOffset(offsetRight);
        }
        else {
            offsetRight = Utils.convertPixelsToDp(barRight - lineRight);
            mKlineChart.setExtraRightOffset(offsetRight);
        }

        ((LinearLayout.LayoutParams) mKlineChart.getLayoutParams()).bottomMargin = (int) -(lineBottom + barTop);
//        mKlineChart.setLayoutParams(layoutParams);
    }


    public class CoupleChartGestureListener implements OnChartGestureListener {

        private Chart srcChart;
        private Chart[] dstCharts;

        public CoupleChartGestureListener(Chart srcChart, Chart[] dstCharts) {
            this.srcChart = srcChart;
            this.dstCharts = dstCharts;
        }

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            syncCharts();
        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
            syncCharts();
        }

        @Override
        public void onChartLongPressed(MotionEvent me) {
            syncCharts();
        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {
            syncCharts();
        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {
            syncCharts();
        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
            syncCharts();
        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
            syncCharts();
        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {
            syncCharts();
        }

        public void syncCharts() {
            Matrix srcMatrix;
            float[] srcValues = new float[9];
            Matrix dstMatrix;
            float[] dstValues = new float[9];
            // get src chart translation matrix:
            srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
            srcMatrix.getValues(srcValues);
            // apply X axis scaling and position to dst charts:
            for (Chart dstChart : dstCharts) {
                if (dstChart.getVisibility() == View.VISIBLE) {
                    dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                    dstMatrix.getValues(dstValues);

                    dstValues[Matrix.MSCALE_X] = srcValues[Matrix.MSCALE_X];
                    dstValues[Matrix.MSKEW_X] = srcValues[Matrix.MSKEW_X];
                    dstValues[Matrix.MTRANS_X] = srcValues[Matrix.MTRANS_X];
                    dstValues[Matrix.MSKEW_Y] = srcValues[Matrix.MSKEW_Y];
                    dstValues[Matrix.MSCALE_Y] = srcValues[Matrix.MSCALE_Y];
                    dstValues[Matrix.MTRANS_Y] = srcValues[Matrix.MTRANS_Y];
                    dstValues[Matrix.MPERSP_0] = srcValues[Matrix.MPERSP_0];
                    dstValues[Matrix.MPERSP_1] = srcValues[Matrix.MPERSP_1];
                    dstValues[Matrix.MPERSP_2] = srcValues[Matrix.MPERSP_2];

                    dstMatrix.setValues(dstValues);
                    dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
                }
            }
        }
    }

}
