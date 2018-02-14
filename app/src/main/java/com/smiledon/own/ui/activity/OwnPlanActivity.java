package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.app.OwnConfig;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityOwnPlanBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.ui.adapter.OwnPlanAdapter;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 计划页面
 *
 * @author zhaidong
 * @date 2018/2/12 11:35
 */

public class OwnPlanActivity extends BaseActivity{

    private ActivityOwnPlanBinding mBinding;
    private OwnPlanAdapter adapter;
    //    private List<Plan> dataList;
    private DividerItemDecoration dividerItemDecoration;

    private String[] oneTabs;
    private String[] twoTabs;
    private String[] threeTabs;

    private int[] params = new int[3];

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_own_plan);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        initTabs();
        initData();
        initEvent();

    }

    private void initTabs() {

        oneTabs = ResourcesUtils.getStringArray(R.array.tab_one);
        twoTabs = ResourcesUtils.getStringArray(R.array.tab_two);
        threeTabs = ResourcesUtils.getStringArray(R.array.tab_three);

        initTabsTitle(mBinding.oneTl, oneTabs);
        initTabsTitle(mBinding.twoTl, twoTabs);
        initTabsTitle(mBinding.threeTl, threeTabs);
    }

    private void initData() {
//        if(dataList == null)
//            dataList = new ArrayList<>();

        adapter = new OwnPlanAdapter(this);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        dividerItemDecoration = new DividerItemDecoration(AppApplication.getInstance(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ResourcesUtils.getDrawable(R.drawable.divider));
        mBinding.recyclerView.addItemDecoration(dividerItemDecoration);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void initTabsTitle(TabLayout tabLayout, String[] titles) {

        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]).setTag(i));
        }
    }

    private void initEvent() {

        mBinding.oneTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelect(0, tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mBinding.twoTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelect(1, tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mBinding.threeTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabSelect(2, tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void onTabSelect(int tabId, TabLayout.Tab tab) {
        params[tabId] = (int) tab.getTag();
        updateRecycleView();
        adapter.resetLastOpenPosition();
    }


    public void addPlan(View view) {
        startActivity(EditPlanActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRecycleView();
    }

    private void updateRecycleView() {
        adapter.getItems().clear();
        adapter.getItems().addAll(getData());
    }

    StringBuilder builder;
    private List<Plan> getData() {

        builder = new StringBuilder();

        if (params[0] != OwnConfig.Plan.ALL) {
            builder.append("is_complete").append(OwnConfig.EQUALS).append(params[0]).append(OwnConfig.BLANK);
        }

        if (params[1] != OwnConfig.Plan.ALL) {
            if(builder.toString().endsWith(OwnConfig.BLANK))
                builder.append("and ");
            builder.append("level").append(OwnConfig.EQUALS).append(params[1]).append(OwnConfig.BLANK);
        }

        if (params[2] != OwnConfig.Plan.ALL) {
            if(builder.toString().endsWith(OwnConfig.BLANK))
                builder.append("and ");
            builder.append("type").append(OwnConfig.EQUALS).append(params[2]);
        }

        LogUtil.i("执行的SQL语句：Select * From Plan Where " + builder.toString());
        return DataSupport.where(builder.toString()).order("date").find(Plan.class);
    }

}
