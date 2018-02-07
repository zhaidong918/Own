package com.smiledon.own.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.app.MobConfig;
import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.net.RetrofitClient;
import com.smiledon.own.receiver.OwnReceiver;
import com.smiledon.own.service.model.BaseResponse;
import com.smiledon.own.service.model.PM2Dot5;
import com.smiledon.own.service.model.Weather;
import com.smiledon.own.service.model.api.MobService;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author East Chak
 * @date 2018/1/17 16:29
 */

public class OwnService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    AlarmManager alarmManager;

    private boolean isOnCreated = false;

    @Override
    public void onCreate() {
        super.onCreate();
        disposable = new CompositeDisposable();
        alarmManager = (AlarmManager) AppApplication.getInstance().getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(this, OwnReceiver.class);
        intent.setAction(MobConfig.ACTION_REMIND_PM2DOT5);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(AppApplication.getInstance(),0, intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1*1000, pendingIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isOnCreated) {
//            getCurrentWeather();
            getPM2Dot5();
        } else {
            isOnCreated = true;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable.clear();
        }
    }

    private CompositeDisposable disposable;
    private PM2Dot5 pm2Dot5;
    private Weather weather;

    private void getPM2Dot5(){

        Map<String, String> params = new HashMap();
        params.put(MobConfig.KEY, MobConfig.APP_KEY);
        params.put(MobConfig.Weather.CITY, "成都");
        params.put(MobConfig.Weather.PROV, "四川");

        RetrofitClient.getInstance().create(MobService.class).getPM2Dot5(params)
                .compose(RxHelper.rxSchedulerHelper())
                .map(new ServerResultFunction())
                .subscribe(new OwnObserver<BaseResponse<List<PM2Dot5>>>() {
                    @Override
                    public void onError(String e) {}

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<List<PM2Dot5>> value) {
                        if (value != null && value.result != null && !value.result.isEmpty()) {
                            pm2Dot5 = value.result.get(0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        showNotification();
                    }
                });
    }

    public void getCurrentWeather() {
        Map<String, String> params = new HashMap();
        params.put(MobConfig.KEY, MobConfig.APP_KEY);
        params.put(MobConfig.Weather.CITY, "成都");
        params.put(MobConfig.Weather.PROV, "四川");

        RetrofitClient.getInstance().create(MobService.class).getCurrentWeather(params)
                .compose(RxHelper.rxSchedulerHelper())
                .map(new ServerResultFunction())
                .subscribe(new OwnObserver<BaseResponse<List<Weather>>>() {
                    @Override
                    public void onError(String e) {}

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<List<Weather>> value) {
                        if (value != null && value.result != null && !value.result.isEmpty()) {
                            weather = value.result.get(0);
                        }
                    }

                    @Override
                    public void onComplete() {
                        showNotification();
                    }
                });


    }

    private long startTime() {

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.SECOND, 30);
        return calendar.getTimeInMillis();
    }


    private void showNotification() {

        NotificationManager notificationManager
                = (NotificationManager) AppApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AppApplication.getInstance(), "1");
        builder.setSmallIcon(R.drawable.weather_cloudy);
        builder.setContentText(pm2Dot5.quality + "\t  PM2.5-" + pm2Dot5.pm25);

        notificationManager.notify(1, builder.build());
    }
}
