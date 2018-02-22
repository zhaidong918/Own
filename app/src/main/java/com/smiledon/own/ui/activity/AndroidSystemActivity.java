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

        dataList = new ArrayList<>();

        AndroidSystemInfo info = new AndroidSystemInfo();
        info.setAlias("O");
        info.setVersion("Android 8.1");
        info.setRelease_date("2017年12月");
        info.setVersion_number("27");
        info.setFull_name_en("Oreo");
        info.setFull_name_ch("奥利奥");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("O");
        info.setVersion("Android 8.0");
        info.setRelease_date("2017年8月");
        info.setVersion_number("26");
        info.setFull_name_en("Oreo");
        info.setFull_name_ch("奥利奥");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("N");
        info.setVersion("Android 7.1");
        info.setRelease_date("2016年12月");
        info.setVersion_number("25");
        info.setFull_name_en("Nougat");
        info.setFull_name_ch("牛轧糖");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("N");
        info.setVersion("Android 7.0");
        info.setRelease_date("2016年8月");
        info.setVersion_number("24");
        info.setFull_name_en("Nougat");
        info.setFull_name_ch("牛轧糖");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("M");
        info.setVersion("Android 6.0");
        info.setRelease_date("2015年5月");
        info.setVersion_number("23");
        info.setFull_name_en("Marshmallow");
        info.setFull_name_ch("棉花糖");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("L");
        info.setVersion("Android 5.1");
        info.setRelease_date("2014年10月");
        info.setVersion_number("22");
        info.setFull_name_en("Lollipop");
        info.setFull_name_ch("棉花糖");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("L");
        info.setVersion("Android 5.0");
        info.setRelease_date("2014年10月");
        info.setVersion_number("21");
        info.setFull_name_en("Lollipop");
        info.setFull_name_ch("棉花糖");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("K");
        info.setVersion("Android 4.4");
        info.setRelease_date("2013年下半年");
        info.setVersion_number("19");
        info.setFull_name_en("Kitkat");
        info.setFull_name_ch("奇巧巧克力");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("J");
        info.setVersion("Android 4.3");
        info.setRelease_date("2012年10月");
        info.setVersion_number("18");
        info.setFull_name_en("Jelly Bean");
        info.setFull_name_ch("果冻豆");
        dataList.add(info);

        info = new AndroidSystemInfo();
        info.setAlias("J");
        info.setVersion("Android 4.3");
        info.setRelease_date("2012年10月");
        info.setVersion_number("18");
        info.setFull_name_en("Jelly Bean");
        info.setFull_name_ch("果冻豆");
        dataList.add(info);

    }
}
