package com.smiledon.own.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smiledon.own.app.MobConfig;
import com.smiledon.own.service.OwnService;
import com.smiledon.own.utils.LogUtil;

/**
 * @author East Chak
 * @date 2018/1/17 21:37
 */

public class OwnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent == null)  return;

        LogUtil.i(intent.getAction());

        if (MobConfig.ACTION_REMIND_PM2DOT5.equals(intent.getAction())) {
            Intent i = new Intent(context, OwnService.class);
            context.startService(i);
        }
    }




}
