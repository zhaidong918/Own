package com.smiledon.own.widgets.Chart;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;

import java.util.List;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/19 11:19
 */

public class ChartUtil {

    public static void initChart(CombinedChart combinedChart) {

        //不可放大缩小
        combinedChart.setScaleEnabled(false);
        //图表描述语
        Description description = new Description();
        description.setText("");
        description.setPosition(10f,10f);
        combinedChart.setDescription(description);
        //具体线图得描述语
        combinedChart.getLegend().setEnabled(false);

        //X轴
        XAxis xAxis = combinedChart.getXAxis();
        //不画X轴边线
        xAxis.setDrawAxisLine(false);
        //隐藏X value值
        xAxis.setTextSize(0);
        //设置横轴线得颜色和宽度
        xAxis.setGridColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.line_grid_color));
        xAxis.setGridLineWidth(0.5f);
        //X轴标签在空格之间
        xAxis.setCenterAxisLabels(false);
        //轴显示的最小粒度
        xAxis.setGranularity(1f);
        //启用粒度控制
        xAxis.setGranularityEnabled(true);

        //不画左边Y轴
        YAxis yLeftAxis = combinedChart.getAxisLeft();
        yLeftAxis.setEnabled(false);

        //画右边Y轴
        YAxis yRightAxis = combinedChart.getAxisRight();
        //不画Y轴边线
        yRightAxis.setDrawAxisLine(false);
        //设置Y轴value字号字色
        yRightAxis.setTextSize(8f);
        yRightAxis.setTextColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.white_color));
        //自定义Y轴精度
        yRightAxis.setValueFormatter(new DefaultAxisValueFormatter(6));
        //设置纵轴线得颜色和宽度
        yRightAxis.setGridColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.line_grid_color));
        yRightAxis.setGridLineWidth(0.5f);

    }

    public static CandleDataSet initCandleDataSet(List<CandleEntry> candleEntryList) {

        CandleDataSet candleDataSet = new CandleDataSet(candleEntryList, "Kline");
        //参考线是右边Y轴（数据集）
        candleDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        //影线颜色与实体一致
        candleDataSet.setShadowColorSameAsCandle(true);
        //影线宽度
        candleDataSet.setShadowWidth(1f);

        //红跌
        candleDataSet.setDecreasingColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.orange_color));
        //实心蜡烛
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        //绿涨
        candleDataSet.setIncreasingColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.blue_color));
        //空心蜡烛
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);

        // open == close 时的颜色  价格不涨不跌（一字线）颜色
        candleDataSet.setNeutralColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.blue_color));

        // 在图表中的元素上面是否显示value
        candleDataSet.setDrawValues(false);

        // 选中高亮线的线宽
        candleDataSet.setHighlightLineWidth(1f);
        // 选中高亮线的颜色
        candleDataSet.setHighLightColor(ContextCompat.getColor(AppApplication.getInstance(), R.color.high_light_color));

        return candleDataSet;
    }

    private static LineDataSet getLineDataSet(int ma, List<Entry> entryList) {

        LineDataSet lineDataSet = new LineDataSet(entryList, "MA" + ma);
        //参考线是右边Y轴（数据集）
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //高亮false
        lineDataSet.setHighlightEnabled(false);
        //不画value值
        lineDataSet.setDrawValues(false);
        //线得颜色
        if (ma == 5) {
            lineDataSet.setColor(Color.GREEN);
        }
        else if (ma == 10) {
            lineDataSet.setColor(Color.GRAY);
        }
        else {
            lineDataSet.setColor(Color.YELLOW);
        }
        //线宽
        lineDataSet.setLineWidth(1f);
        //不画折线点得圆
        lineDataSet.setDrawCircles(false);

        return lineDataSet;
    }

    private static BarDataSet getBarDataSet(List<BarEntry> barEntryList) {

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Deal volume");
        //设置柱状图颜色
        barDataSet.setColors(R.color.orange_color, R.color.blue_color);
        //不画value值
        barDataSet.setDrawValues(false);

        return barDataSet;
    }

}
