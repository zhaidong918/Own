package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityOwnPlanBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.ui.adapter.OwnPlanAdapter;
import com.smiledon.own.utils.ResourcesUtils;

import java.util.ArrayList;
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
    private List<Plan> dataList;
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

        initModel();
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

        dataList = new ArrayList<>();

        adapter = new OwnPlanAdapter(mContext);

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
    }


    private void initModel() {


        Plan plan = new Plan();
        plan.setIs_complete(false);
        plan.setPlan("临时任务：新增登录页面账号切换的动画效果");

        dataList.add(plan);

        for (int i = 0; i < 30; i++) {
            plan = new Plan();
            plan.setIs_complete(i%5 == 2);
            plan.setPlan(i + "\tAPP打包上线完成本次版本迭代");
            dataList.add(plan);
        }

        adapter.getItems().addAll(dataList);
    }

}
