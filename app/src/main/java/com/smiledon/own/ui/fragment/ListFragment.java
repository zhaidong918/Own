package com.smiledon.own.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.base.fragment.BaseFragment;
import com.smiledon.own.databinding.FragmentListBinding;
import com.smiledon.own.ui.adapter.CityAdapter;
import com.smiledon.own.utils.ResourcesUtils;
import com.smiledon.own.widgets.DemoCity;
import com.smiledon.own.widgets.TitleItemDecoration;

/**
 * @author East Chak
 * @date 2018/1/19 18:26
 */

public class ListFragment extends BaseFragment {

    FragmentListBinding binding;
    CityAdapter cityAdapter;
    DividerItemDecoration dividerItemDecoration;


    @Override
    public View createContentView() {
        binding = inflate(R.layout.fragment_list);
        return binding.getRoot();
    }

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {

        cityAdapter = new CityAdapter();
        binding.recyclerView.setAdapter(cityAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AppApplication.getInstance()));
        binding.recyclerView.addItemDecoration(new TitleItemDecoration());

        dividerItemDecoration = new DividerItemDecoration(AppApplication.getInstance(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ResourcesUtils.getDrawable(R.drawable.divider));
        binding.recyclerView.addItemDecoration(dividerItemDecoration);
        cityAdapter.addCityList(DemoCity.getDatas());




    }

}
