package com.smiledon.own.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.smiledon.own.R;
import com.smiledon.own.app.RxBus;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityMapBinding;
import com.smiledon.own.service.model.Latlng;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ResourcesUtils;
import com.smiledon.own.utils.ToastUtils;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 *
 * @author East Chak
 * @date 2018/1/18 10:35
 */

public class MapActivity extends BaseActivity implements AMapLocationListener,LocationSource{

    private ActivityMapBinding binding;
    private AMap aMap;

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private UiSettings uiSettings;


    private int runStatus = RunStatus.START;

    @Override
    protected void initView(Bundle savedInstanceState) {

        initToolbar();

        binding.mapView.onCreate(savedInstanceState);

        if(aMap == null)
            aMap = binding.mapView.getMap();

        initMapType();
        initLocation();
        showLocation();
        initUiSetting();


        //开启室内地图显示
        aMap.showIndoorMap(true);
        //地图缩放级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        initEvent();
    }

    private void initEvent() {

        List<Latlng> latlngs = DataSupport.findAll(Latlng.class);
        if (latlngs != null && latlngs.size() != 0) {
            binding.replayIv.setVisibility(View.VISIBLE);
        }

        binding.runIv.setOnClickListener(v -> {
            switch (runStatus) {
                case RunStatus.START:
                    binding.runIv.setImageResource(R.drawable.img_run_stop);
                    binding.replayIv.setVisibility(View.GONE);
                    runStatus = RunStatus.RUN;
                    break;
                case RunStatus.RUN:
                    binding.runIv.setImageResource(R.drawable.img_run_start);
                    binding.replayIv.setVisibility(View.VISIBLE);
                    runStatus = RunStatus.STOP;
                    break;
                case RunStatus.STOP:
                    binding.runIv.setImageResource(R.drawable.img_run_stop);
                    binding.replayIv.setVisibility(View.GONE);
                    runStatus = RunStatus.RUN;
                    break;
            }
        });

        binding.replayIv.setOnClickListener(v -> {
            binding.runIv.setVisibility(View.GONE);
            binding.replayIv.setVisibility(View.GONE);
            movePath();
            drawPath();
        });
    }

    @Override
    protected View createContentView() {
        binding = inflate(R.layout.activity_map);
        return binding.getRoot();
    }

    private void initToolbar() {

        binding.appbar.toolbar.setTitle(R.string.amap_map);

        setSupportActionBar(binding.appbar.toolbar);
        binding.appbar.toolbar.setNavigationIcon(R.drawable.img_back);

        binding.appbar.toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    /**
     *  MAP_TYPE_NAVI  导航地图
     *  MAP_TYPE_NIGHT  夜景地图
     *  MAP_TYPE_NORMAL  白昼地图（即普通地图）
     *  MAP_TYPE_SATELLITE 卫星图WW
     */
    private void initMapType() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if(hour > 7 && hour < 19)
            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        else
            aMap.setMapType(AMap.MAP_TYPE_NIGHT);

        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
    }

    /**
     * 初始化定位参数 开启定位
     */
    private void initLocation() {


        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();

    }

    /**
     * 显示定位位置 在地图中
     */
    private void showLocation() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }

    private void initUiSetting() {
        uiSettings = aMap.getUiSettings();
        //设置默认定位按钮是否显示，非必需设置。
        uiSettings.setMyLocationButtonEnabled(true);
        //通过aMap对象设置定位数据源的监听
//        aMap.setLocationSource(this);
        // 可触发定位并显示当前位置
        aMap.setMyLocationEnabled(true);
        //设置默认指南针是否显示
        uiSettings.setCompassEnabled(true);
        //控制比例尺控件是否显示
        uiSettings.setScaleControlsEnabled(true);
        //设置logo位置
        /*
            AMapOptions.LOGO_POSITION_BOTTOM_LEFT       LOGO边缘MARGIN（左边）
            AMapOptions.LOGO_MARGIN_BOTTOM              LOGO边缘MARGIN（底部
            AMapOptions.LOGO_MARGIN_RIGHT               LOGO边缘MARGIN（右边）
            AMapOptions.LOGO_POSITION_BOTTOM_CENTER     LOGO位置（地图底部居中）
            AMapOptions.LOGO_POSITION_BOTTOM_LEFT       LOGO位置（地图左下角）
            AMapOptions.LOGO_POSITION_BOTTOM_RIGHT      LOGO位置（地图右下角）
         */
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        binding.mapView.onDestroy();
        if(mlocationClient != null)
            mlocationClient.stopLocation();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        binding.mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        binding.mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        binding.mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                LogUtil.i("获取纬度：" + aMapLocation.getLatitude());
                LogUtil.i("获取经度：" + aMapLocation.getLongitude());
                ToastUtils.showToast(aMapLocation.getStreet() + aMapLocation.getStreetNum());
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间

                RxBus.getIntanceBus().post(aMapLocation);

                if (runStatus == RunStatus.RUN) {
//                    saveLatLng(aMapLocation);
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    private void saveLatLng(AMapLocation aMapLocation) {
        Latlng latLng = new Latlng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        latLng.save();
    }

    private int duration = 60;

    private  List<LatLng> readLatLngs() {
        List<Latlng> latlngs = DataSupport.findAll(Latlng.class);
        List<LatLng> latLngList = new ArrayList<>();
        for (Latlng latlng : latlngs) {
            LogUtil.i(latlng.toString());
            latLngList.add(latlng.getLatlng());
        }

//        duration = (int) (latlngs.get(latlngs.size()-1).getTime() - latlngs.get(0).getTime());

        return latLngList;
    }

    private double lastV = -1;
    private SmoothMoveMarker smoothMarker;

    private void movePath() {

        if (smoothMarker != null) {
            smoothMarker.removeMarker();
        }

        List<LatLng> latLngList = readLatLngs();
        LatLngBounds bounds = new LatLngBounds(latLngList.get(0), latLngList.get(latLngList.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        smoothMarker = new SmoothMoveMarker(aMap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.icon_run));

        LatLng drivePoint = latLngList.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(latLngList, drivePoint);
        latLngList.set(pair.first, drivePoint);
        List<LatLng> subList = latLngList.subList(pair.first, latLngList.size());

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(duration);
        // 开始滑动
        smoothMarker.startSmoothMove();

        smoothMarker.setMoveListener(v -> {
            if (v == 0) {
                runOnUiThread(() -> {
                    binding.runIv.setVisibility(View.VISIBLE);
                    binding.replayIv.setVisibility(View.VISIBLE);
                });
            }
        });
    }

    Polyline polyline;

    private void drawPath() {

        if (polyline != null) {
            polyline.remove();
        }

        List<LatLng> latLngList = readLatLngs();
        polyline =aMap.addPolyline(new PolylineOptions().
                addAll(latLngList).width(10).color(ResourcesUtils.getColor(R.color.txt_gray)));

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    public static class RunStatus{
        public static final int START = 0;
        public static final int RUN = 1;
        public static final int STOP = 2;
    }
}
