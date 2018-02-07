package com.smiledon.own.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.fragment.BaseFragment;
import com.smiledon.own.databinding.FragmentListBinding;
import com.smiledon.own.service.model.RunRecord;
import com.smiledon.own.ui.adapter.CityAdapter;
import com.smiledon.own.ui.adapter.RecordAdapter;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;
import com.smiledon.own.widgets.DemoCity;
import com.smiledon.own.widgets.RecordTitleItemDecoration;
import com.smiledon.own.widgets.TitleItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * @author East Chak
 * @date 2018/1/19 18:26
 */

public class RecordListFragment extends BaseFragment {

    FragmentListBinding binding;
    RecordAdapter recordAdapter;
    DividerItemDecoration dividerItemDecoration;

    List<RunRecord> dataList;


    @Override
    public View createContentView() {
        binding = inflate(R.layout.fragment_list);
        return binding.getRoot();
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {
        dataList = new ArrayList<>();
        RunRecord runRecord;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        for (int i = 0; i < 12; i++) {
            calendar.set(Calendar.MONTH, i);
            for (int j =0; j < i+1; j++) {
                runRecord = new RunRecord(calendar.getTimeInMillis() + i*j*1000000,  calendar.getTimeInMillis() + i*j*1000000, 5845000 + i*j*10000);
                dataList.add(runRecord);
            }
        }

        recordAdapter = new RecordAdapter(AppApplication.getInstance());
        binding.recyclerView.setAdapter(recordAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AppApplication.getInstance()));
        binding.recyclerView.addItemDecoration(new RecordTitleItemDecoration(dataList));

        dividerItemDecoration = new DividerItemDecoration(AppApplication.getInstance(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ResourcesUtils.getDrawable(R.drawable.divider));
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        recordAdapter.getItems().addAll(dataList);
    }


    private long MONTH = 31*24*60*60*1000;
    private long DAY = 24*60*60*1000;
    private long HOUR = 60*60*1000;
    private long MINUTE = 60*1000;

}
