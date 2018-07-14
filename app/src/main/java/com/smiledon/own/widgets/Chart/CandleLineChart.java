package com.smiledon.own.widgets.Chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.smiledon.own.R;

import java.util.ArrayList;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/6/15 16:52
 */

public class CandleLineChart extends CombinedChart {

    public CandleLineChart(Context context) {
        this(context, null);
    }

    public CandleLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CandleLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);


    }



    private void initCandle() {



    }

    private void initLine(int ma){

        ArrayList<LineDataSet> lineDataSetList = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(null, "MA" + ma);
        //参考线是右边Y轴（数据集）
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        //高亮false
        lineDataSet.setHighlightEnabled(false);
        //不画value值
        lineDataSet.setDrawValues(false);
        //线得颜色
        if (ma == 5) {
            lineDataSet.setColor(Color.GREEN);
        } else if (ma == 10) {
            lineDataSet.setColor(Color.GRAY);
        } else {
            lineDataSet.setColor(Color.YELLOW);
        }
        //线宽
        lineDataSet.setLineWidth(1f);
        //不画折线点得圆
        lineDataSet.setDrawCircles(false);

    }

}
