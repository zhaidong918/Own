package com.smiledon.own.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.smiledon.own.app.RxBus;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ToastUtils;

/**
 * 网络监听
 *
 * @author zhaidong
 * @date 2018/2/9 14:52
 */

public class NetStatusReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        LogUtil.i("网络变化监听中..." + intent.getAction());

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        ToastUtils.showToast("网络变化监听中...");

//        RxBus.getIntanceBus().post(networkInfo != null && networkInfo.isAvailable());

    }

}
