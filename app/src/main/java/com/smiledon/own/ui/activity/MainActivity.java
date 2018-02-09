package com.smiledon.own.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smiledon.own.R;
import com.smiledon.own.app.MobConfig;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMainBinding;
import com.smiledon.own.service.OwnService;
import com.smiledon.own.service.model.CookMenu;
import com.smiledon.own.service.model.Weather;
import com.smiledon.own.service.presenter.MobPresenter;
import com.smiledon.own.service.view.IBaseView;
import com.smiledon.own.ui.adapter.ImageAdapter;
import com.smiledon.own.utils.AnimUtils;
import com.smiledon.own.utils.ToastUtils;

import java.util.List;


/**
 *
 * 主页面
 *
 * @author East Chak
 * @date 2018/1/7 17:18
 */

public class MainActivity extends BaseActivity implements IBaseView{

    ActivityMainBinding binding;
    ImageAdapter imageAdapter;
    MobPresenter weatherPresenter = new MobPresenter();

    @Override
    protected void initView(Bundle savedInstanceState) {

        weatherPresenter.onCreate();
        weatherPresenter.attachView(this);

        initToolbar();
        initNav();
        initRecycleView();

        startService(new Intent(this, OwnService.class));

    }

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_main);
        return binding.getRoot();
    }

    private void initToolbar() {

        binding.appbar.toolbar.setTitle(R.string.index);

        setSupportActionBar(binding.appbar.toolbar);
        binding.appbar.toolbar.setNavigationIcon(R.drawable.img_nav);

        binding.appbar.toolbar.setNavigationOnClickListener(v -> {
            binding.drawerLayout.openDrawer(Gravity.START);
            weatherPresenter.getCurrentWeather();
        });
    }

    private TextView weatherTv;

    private ImageView weatherIv;
    private void initNav() {
        //图标颜色变灰色
        binding.leftNav.setItemIconTintList(null);

        binding.leftNav.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_item_home:
                    weatherPresenter.getCookImageList();
                    break;
                case R.id.nav_item_run:
                    startActivity(GuideRunActivity.class);
                    break;
                case R.id.nav_item_mine:
                    startActivity(MineActivity.class);
                    break;
                case R.id.nav_item_about:
                    break;
            }
            ToastUtils.showToast((String) item.getTitle());
            binding.drawerLayout.closeDrawer(Gravity.START);
            return true;
        });

        weatherTv = binding.leftNav.getHeaderView(0).findViewById(R.id.weather_tv);
        weatherIv = binding.leftNav.getHeaderView(0).findViewById(R.id.weather_iv);
    }

    private void initRecycleView() {
        imageAdapter = new ImageAdapter();
        binding.recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.recyclerView.setAdapter(imageAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        weatherPresenter.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSuccess(Object bean) {
        if (bean instanceof Weather) {
            Weather weather = (Weather) bean;
            AnimUtils.alphaAnim(weatherTv);
            weatherTv.setText(weather.weather + "\t" + weather.temperature);

            int imageId = MobConfig.getWeatherImage(weather.weather);
            if (imageId != -1) {
                AnimUtils.alphaAnim(weatherIv);
                weatherIv.setImageResource(imageId);
            }
        }
    }

    @Override
    public void onError(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void onImageLoad(List<CookMenu> menuList) {
        imageAdapter.addImageList(menuList);
    }
}
