package com.diting.market.widgets.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.diting.market.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 自定义样式的K线图
 *
 * Created by SmileDon on 2018/5/8.
 */

public class NewGCandleStickChart extends CandleStickChart {


    public NewGCandleStickChart(Context context) {
        this(context, null);
    }

    public NewGCandleStickChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewGCandleStickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


        initBackground();
        initZoom();
        initDescription();
        initLegend();

        initXAxis();
        initYAxis();
    }

    private void initBackground() {
        // 是否显示表格颜色
        setDrawGridBackground(true);
        // 设置背景
        setBackgroundColor(getResources().getColor(R.color.transparent));
        // 设置表格背景色
        setGridBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void initZoom() {
        //xy同时缩放
        setPinchZoom(false);
        //双击缩放
        setDoubleTapToZoomEnabled(false);
        //Y轴根据滑动自动缩放
        setAutoScaleMinMaxEnabled(true);
    }

    private void initDescription() {
        //图标描述
        Description description = new Description();
        description.setText("");
        setDescription(description);
    }

    private void initLegend() {

        // 设置比例图标示
        Legend l = getLegend();
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
        labels[0] = "红跌";
        labels[1] = "绿涨";

        int[] colors = new int[2];
        colors[0] = getResources().getColor(R.color.blue_color);
        colors[1] = getResources().getColor(R.color.orange_color);

        l.setExtra(colors, labels);

        // 显不显示标签
        l.setEnabled(false);
    }

    private float mXGridLineWidth = 0.5f;
    private float mYGridLineWidth = 0.5f;
    private float mShadowWidth = 0.5f;
    private float mHighlightLineWidth = 0.5f;

    private void initYAxis() {

        YAxis yRightAxis = getAxisRight();
        yRightAxis.setTextColor(getResources().getColor(R.color.white_color));
//        yRightAxis.setDrawZeroLine(false);
        //不画Y轴的线
        yRightAxis.setDrawAxisLine(false);
        yRightAxis.setGridColor(getResources().getColor(R.color.line_grid_color));
        yRightAxis.setGridLineWidth(mYGridLineWidth);
        yRightAxis.setValueFormatter(new DefaultAxisValueFormatter(6));

        YAxis yLeftAxis = getAxisLeft();
        yLeftAxis.setEnabled(false);
    }

    //临界线那种
    private List<LimitLine> xLimitLineList = new ArrayList<>();;

    private void initXAxis() {

        //横轴的位置
        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(getResources().getColor(R.color.white_color));
        //不画X轴的线
        xAxis.setDrawAxisLine(false);
        xAxis.setGridColor(getResources().getColor(R.color.line_grid_color));
        xAxis.setGridLineWidth(mXGridLineWidth);

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
    private class AxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            return ((int)value + 1) + ":00";
        }
    }



    private CandleDataSet dataSet;

    public CandleDataSet getDataSet(List<CandleEntry> yVals) {

        dataSet = new CandleDataSet(yVals, "DiTing");

        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 影线颜色与实体一致
        dataSet.setShadowColorSameAsCandle(true);

        // 影线
        dataSet.setShadowWidth(mShadowWidth);

        // 红涨，实体
        dataSet.setDecreasingColor(getResources().getColor(R.color.orange_color));
        dataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        // 绿跌，空心
        dataSet.setIncreasingColor(getResources().getColor(R.color.blue_color));
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);

        //open == close 时的颜色
        // 当天价格不涨不跌（一字线）颜色
        dataSet.setNeutralColor(Color.RED);
        // 选中蜡烛时的线宽
        dataSet.setHighlightLineWidth(mHighlightLineWidth);
        dataSet.setHighLightColor(getResources().getColor(R.color.high_light_color));

        // 在图表中的元素上面是否显示数值
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(getResources().getColor(R.color.white_color));

        return dataSet;
    }

}
