package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityAndroidSystemBinding;
import com.smiledon.own.service.model.AndroidSystemInfo;
import com.smiledon.own.ui.adapter.AndroidSystemAdapter;
import com.smiledon.own.utils.ResourcesUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录安卓系统的由来
 *
 * @author zhaidong
 * @date 2018/2/22 16:58
 */

public class AndroidSystemActivity extends BaseActivity {

    ActivityAndroidSystemBinding mBinding;
    AndroidSystemAdapter adapter;
    DividerItemDecoration dividerItemDecoration;
    List<AndroidSystemInfo> dataList;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_android_system);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        initData();

        adapter = new AndroidSystemAdapter(AppApplication.getInstance());
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(AppApplication.getInstance()));

        dividerItemDecoration = new DividerItemDecoration(AppApplication.getInstance(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ResourcesUtils.getDrawable(R.drawable.divider));
        mBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        adapter.getItems().addAll(dataList);

    }

    private void initData() {
        DataSupport.deleteAll(AndroidSystemInfo.class);
        dataList = DataSupport.findAll(AndroidSystemInfo.class);

        if (dataList == null
                || dataList.size() == 0) {

            dataList = new ArrayList<>();

            AndroidSystemInfo info = new AndroidSystemInfo();
            info.setAlias("O");
            info.setVersion("Android 8.1");
            info.setRelease_date("2017年12月");
            info.setVersionNumber("27");
            info.setFull_name_en("Oreo");
            info.setFull_name_ch("奥利奥");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("O");
            info.setVersion("Android 8.0");
            info.setRelease_date("2017年8月");
            info.setVersionNumber("26");
            info.setFull_name_en("Oreo");
            info.setFull_name_ch("奥利奥");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("N");
            info.setVersion("Android 7.1");
            info.setRelease_date("2016年12月");
            info.setVersionNumber("25");
            info.setFull_name_en("Nougat");
            info.setFull_name_ch("牛轧糖");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("N");
            info.setVersion("Android 7.0");
            info.setRelease_date("2016年8月");
            info.setVersionNumber("24");
            info.setFull_name_en("Nougat");
            info.setFull_name_ch("牛轧糖");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("M");
            info.setVersion("Android 6.0");
            info.setRelease_date("2015年5月");
            info.setVersionNumber("23");
            info.setFull_name_en("Marshmallow");
            info.setFull_name_ch("棉花糖");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("L");
            info.setVersion("Android 5.1");
            info.setRelease_date("2014年10月");
            info.setVersionNumber("22");
            info.setFull_name_en("Lollipop");
            info.setFull_name_ch("棉花糖");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("L");
            info.setVersion("Android 5.0");
            info.setRelease_date("2014年10月");
            info.setVersionNumber("21");
            info.setFull_name_en("Lollipop");
            info.setFull_name_ch("棉花糖");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("K");
            info.setVersion("Android 4.4W");
            info.setRelease_date("2013年下半年");
            info.setVersionNumber("20");
            info.setFull_name_en("Kitkat Wear");
            info.setFull_name_ch("奇巧巧克力");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("K");
            info.setVersion("Android 4.4");
            info.setRelease_date("2013年下半年");
            info.setVersionNumber("19");
            info.setFull_name_en("Kitkat");
            info.setFull_name_ch("奇巧巧克力");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("J");
            info.setVersion("Android 4.3");
            info.setRelease_date("2012年10月");
            info.setVersionNumber("18");
            info.setFull_name_en("Jelly Bean");
            info.setFull_name_ch("果冻豆");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("J");
            info.setVersion("Android 4.2");
            info.setRelease_date("2012年10月");
            info.setVersionNumber("17");
            info.setFull_name_en("Jelly Bean");
            info.setFull_name_ch("果冻豆");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("J");
            info.setVersion("Android 4.1");
            info.setRelease_date("2012年6月");
            info.setVersionNumber("16");
            info.setFull_name_en("Jelly Bean");
            info.setFull_name_ch("果冻豆");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("I");
            info.setVersion("Android 4.0.3");
            info.setRelease_date("--");
            info.setVersionNumber("15");
            info.setFull_name_en("IceCreamSandwich");
            info.setFull_name_ch("冰淇凌三明治");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("I");
            info.setVersion("Android 4.0");
            info.setRelease_date("2011年10月");
            info.setVersionNumber("14");
            info.setFull_name_en("IceCreamSandwich");
            info.setFull_name_ch("冰淇凌三明治");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("H");
            info.setVersion("Android 3.2");
            info.setRelease_date("2011年7月");
            info.setVersionNumber("13");
            info.setFull_name_en("Honeycomb");
            info.setFull_name_ch("蜂巢");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("H");
            info.setVersion("Android 3.1");
            info.setRelease_date("2011年5月");
            info.setVersionNumber("12");
            info.setFull_name_en("Honeycomb");
            info.setFull_name_ch("蜂巢");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("H");
            info.setVersion("Android 3.0");
            info.setRelease_date("2011年2月");
            info.setVersionNumber("11");
            info.setFull_name_en("Honeycomb");
            info.setFull_name_ch("蜂巢");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("G");
            info.setVersion("Android 2.3.3");
            info.setRelease_date("--");
            info.setVersionNumber("10");
            info.setFull_name_en("Gingerbread");
            info.setFull_name_ch("姜饼");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("G");
            info.setVersion("Android 2.3");
            info.setRelease_date("2010年12月");
            info.setVersionNumber("9");
            info.setFull_name_en("Gingerbread");
            info.setFull_name_ch("姜饼");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("G");
            info.setVersion("Android 2.2");
            info.setRelease_date("2010年5月");
            info.setVersionNumber("8");
            info.setFull_name_en("Froyo");
            info.setFull_name_ch("冻酸奶");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("E");
            info.setVersion("Android 2.1");
            info.setRelease_date("--");
            info.setVersionNumber("7");
            info.setFull_name_en("Eclair");
            info.setFull_name_ch("松饼");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("--");
            info.setVersion("Android 2.0");
            info.setRelease_date("2009年10月");
            info.setVersionNumber("--");
            info.setFull_name_en("--");
            info.setFull_name_ch("--");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("D");
            info.setVersion("Android 1.6");
            info.setRelease_date("2009年9月");
            info.setVersionNumber("--");
            info.setFull_name_en("Donut");
            info.setFull_name_ch("甜甜圈");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("C");
            info.setVersion("Android 1.5");
            info.setRelease_date("2009年4月");
            info.setVersionNumber("--");
            info.setFull_name_en("Cupcake");
            info.setFull_name_ch("纸杯蛋糕");
            dataList.add(info);

            info = new AndroidSystemInfo();
            info.setAlias("--");
            info.setVersion("Android 1.1");
            info.setRelease_date("2008年9月");
            info.setVersionNumber("--");
            info.setFull_name_en("--");
            info.setFull_name_ch("--");
            dataList.add(info);

            DataSupport.saveAll(dataList);
        }
    }
}
