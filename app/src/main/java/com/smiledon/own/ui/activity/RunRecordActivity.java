package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityGuideRunBinding;
import com.smiledon.own.databinding.ActivityRunRecordBinding;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.ui.fragment.ListFragment;
import com.smiledon.own.ui.fragment.RecordListFragment;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * @author East Chak
 * @date 2018/1/18 21:52
 */

public class RunRecordActivity extends BaseActivity {

    ActivityRunRecordBinding binding;
    List<Fragment> fragmentList = new ArrayList<>();
    RecordViewPager recordViewPager;

    String[] tabTitles;

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_run_record);
        return binding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTabLayout();
        initEvent();
        initViewPager();

    }

    private void initViewPager() {

        fragmentList.clear();

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            if(i == 0)
                fragmentList.add(new RecordListFragment());
            else
                fragmentList.add(new ListFragment());
        }

        recordViewPager = new RecordViewPager(getSupportFragmentManager());
        binding.viewPager.setAdapter(recordViewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void initTabLayout() {

        tabTitles = ResourcesUtils.getStringArray(R.array.tab_month);

        for (String tab : tabTitles) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(tab));
        }

    }



    private void initEvent() {

    }

    class RecordViewPager extends FragmentPagerAdapter{

        public RecordViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
