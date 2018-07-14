package com.smiledon.own.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.smiledon.own.R;
import com.smiledon.own.app.MobConfig;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMainBinding;
import com.smiledon.own.net.WebSocketManager;
import com.smiledon.own.service.OwnService;
import com.smiledon.own.service.model.CookMenu;
import com.smiledon.own.service.model.Weather;
import com.smiledon.own.service.presenter.MobPresenter;
import com.smiledon.own.service.view.IBaseView;
import com.smiledon.own.ui.adapter.ImageAdapter;
import com.smiledon.own.utils.AnimUtils;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ToastUtils;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    protected View createContentView() {
        binding = inflate(R.layout.activity_main);
        return binding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        weatherPresenter.onCreate();
        weatherPresenter.attachView(this);


        initTest();

        initToolbar();
        initNav();
        initRecycleView();

        startService(new Intent(this, OwnService.class));


        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        formatIp(dhcpInfo.gateway);
        formatIp(dhcpInfo.netmask);

        LogUtil.i(Integer.toBinaryString(dhcpInfo.gateway));
        LogUtil.i(Integer.toBinaryString(dhcpInfo.netmask));


        seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.d("progress:" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };


        binding.uploadSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);


       /* binding.frameContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LogUtil.d("binding.rootLayout --- onTouch");
                return binding.progressBar.onParentTouchEvent(event);
            }
        });*/

    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener;

    private void initTest() {
    }

    public void showSuccess(View view) {

        /*final LottieDrawable lottieDrawable = new LottieDrawable();


        LottieComposition.Factory.fromAssetFileName(this, "success.json", composition -> {
            lottieDrawable.setComposition(composition);
            binding.success.setImageDrawable(lottieDrawable);
            binding.success.playAnimation();
        });
*/

    }

    public void formatIp(int i) {

        StringBuilder builder = new StringBuilder();

        builder.append(i & 0xff)
                .append(".")
                .append(i >> 8 & 0xff)
                .append(".")
                .append(i >> 16 & 0xff)
                .append(".")
                .append(i >> 24 & 0xff);

        LogUtil.i(builder.toString());


    }

    private void initPickerView() {

        List<String> options1Items = new ArrayList<>();
        options1Items.add("中国");
        options1Items.add("美国");
        options1Items.add("中国香港");

        //条件选择器
        OptionsPickerView pvOptions = new  OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {

            }
        }) .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
            @Override
            public void onOptionsSelectChanged(int options1, int options2, int options3) {
                String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;

            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("城市选择")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(getResources().getColor(R.color.white))//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.white))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.white))//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.ui_market_bg))//标题背景颜色 Night mode
                .setTextColorCenter(getResources().getColor(R.color.white))
                .setTextColorOut(getResources().getColor(R.color.black_9))
                .setDividerColor(getResources().getColor(R.color.black_9))
                .setBgColor(getResources().getColor(R.color.ui_market_bg))//滚轮背景颜色 Night mode
                .setContentTextSize(24)//滚轮文字大小
                .isCenterLabel(true) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();

        pvOptions.setPicker(options1Items);
        pvOptions.show();
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

//        binding.text.setText(Html.fromHtml(getString(R.string.html_string)));

        StringBuilder builder = new StringBuilder();


        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(2);

        builder.append(nf.format(123456789.1234567));

        double d = Double.parseDouble(builder.toString());

        builder.append("/n").append(d);

        binding.text.setText(builder.toString());
//        ProgressAdapter adapter = new ProgressAdapter(this);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(AppApplication.getInstance(), DividerItemDecoration.VERTICAL);
//        dividerItemDecoration.setDrawable(ResourcesUtils.getDrawable(R.drawable.divider));
//        binding.recyclerView.addItemDecoration(dividerItemDecoration);
//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(AppApplication.getInstance()));
//        binding.recyclerView.setAdapter(adapter);


        String[] one = "1.1".split("\\.");
        String[] two = ".1".split("\\.");
        String[] three = "1.".split("\\.");

        LogUtil.d(Arrays.toString(one));
        LogUtil.d(Arrays.toString(two));
        LogUtil.d(Arrays.toString(three));




       /* binding.editText.setFilters(new InputFilter[]{

                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                        // source:当前输入字符
                        // start: 输入字符的开始位置
                        // end:   输入字符的结束位置

                        // dest：  当前已显示的内容
                        // dstart: 删除--光标结束的位置 （没错，就是结束的位置）
                        // dent:   删除--光标开始的位置 （没错，就是开始的位置）

                        String digits = "0123456789.";
                        String sourceValue = source.toString();
                        String destValue = dest.toString();

                        if(!digits.contains(sourceValue))
                            return "";

                        return null;
                    }
                }

        });*/

        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        list.add("E");
        list.add("F");
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

    public void connect(View view) {

        startActivity(HeapActivity.class);

        /*seekBarChangeListener.onStopTrackingTouch(binding.uploadSeekBar);
        binding.uploadSeekBar.setProgress(13);
        seekBarChangeListener.onStartTrackingTouch(binding.uploadSeekBar);


        new Thread(() -> WebSocketManager.geInstance()).start();*/
    }

    private List<String> list = new ArrayList<>();

    public void Reverse(View view) {

        Collections.reverse(list);

        StringBuilder builder = new StringBuilder();

        for (String s : list) {
            builder.append(s).append(" ");
        }

        binding.text.setText(builder.toString());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.waiting.stopLoadingAnim();
        WebSocketManager.geInstance().close();
    }

    public void sendGson(View view) {
        WebSocketManager.geInstance().sendGsonRequest();
    }

}
